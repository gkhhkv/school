package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ecommerce.domain.Product;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.exception.GlobalExceptionHandler;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.support.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;
    @InjectMocks
    private ProductController controller;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void should_returnCreatedProduct_when_validRequest() throws Exception {
        Product product = TestDataFactory.createTestProduct(1L, "新商品", 99.00, 100);
        when(productService.createProduct(any())).thenReturn(product);

        String body = """
                {"name":"新商品","category":"数码","price":99.00,"stock":100,"image":"/img/p1.jpg"}""";

        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.name").value("新商品"));
    }

    @Test
    void should_return400_when_missingName() throws Exception {
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"price\":99.00,\"stock\":100}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return400_when_negativePrice() throws Exception {
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"商品\",\"price\":-1,\"stock\":100}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_returnPaginatedProducts_when_query() throws Exception {
        when(productService.queryProducts(anyInt(), anyInt(), any(), any())).thenReturn(new Page<>(1, 10));
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void should_returnProduct_when_exists() throws Exception {
        Product product = TestDataFactory.createTestProduct(1L, "商品1", 99.00, 100);
        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("商品1"));
    }

    @Test
    void should_returnError_when_productNotFound() throws Exception {
        when(productService.getProductById(999L))
                .thenThrow(new BusinessException(404, "商品不存在"));

        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    void should_updateProduct_when_validRequest() throws Exception {
        Product product = TestDataFactory.createTestProduct(1L, "更新后", 199.00, 200);
        when(productService.updateProduct(anyLong(), any())).thenReturn(product);

        String body = """
                {"name":"更新后","category":"服装","price":199.00,"stock":200,"image":"/img/p1.jpg"}""";

        mockMvc.perform(put("/api/products/1").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("更新后"));
    }

    @Test
    void should_deleteProduct_when_exists() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void should_returnError_when_deleteNonExistent() throws Exception {
        doThrow(new BusinessException(404, "商品不存在"))
                .when(productService).deleteProduct(999L);

        mockMvc.perform(delete("/api/products/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404));
    }
}
