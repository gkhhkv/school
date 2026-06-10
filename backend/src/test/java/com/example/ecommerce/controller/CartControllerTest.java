package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CartItemResponse;
import com.example.ecommerce.exception.GlobalExceptionHandler;
import com.example.ecommerce.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CartControllerTest {

    private MockMvc mockMvc;
    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = mock(CartService.class);
        CartController controller = new CartController(cartService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                // Simulate interceptor setting userId
                .addFilter((request, response, chain) -> {
                    request.setAttribute("userId", 1L);
                    chain.doFilter(request, response);
                })
                .build();
    }

    @Test
    void should_returnCart_when_userHasItems() throws Exception {
        CartItemResponse item = new CartItemResponse();
        item.setProductId(10L);
        item.setProductName("测试商品");
        item.setPrice(BigDecimal.valueOf(99.00));
        item.setQuantity(2);
        item.setStock(50);

        when(cartService.getCart(anyLong())).thenReturn(List.of(item));

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].productName").value("测试商品"))
                .andExpect(jsonPath("$.data[0].quantity").value(2));
    }

    @Test
    void should_syncCart_when_validRequest() throws Exception {
        doNothing().when(cartService).syncCart(anyLong(), any());

        String json = """
                {"items":[{"productId":10,"quantity":3}]}""";

        mockMvc.perform(put("/api/cart/sync")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void should_clearCart_when_called() throws Exception {
        doNothing().when(cartService).clearCart(anyLong());

        mockMvc.perform(delete("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
}
