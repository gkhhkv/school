package com.example.ecommerce.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ecommerce.domain.Product;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.repository.ProductMapper;
import com.example.ecommerce.service.impl.ProductServiceImpl;
import com.example.ecommerce.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void should_createProductSuccessfully() {
        Product product = TestDataFactory.createTestProduct(null, "新商品", 50.00, 100);
        when(productMapper.insert(any(Product.class))).thenReturn(1);

        Product result = productService.createProduct(product);

        assertNotNull(result);
        assertEquals("新商品", result.getName());
        verify(productMapper).insert(any(Product.class));
    }

    @Test
    void should_queryProductsWithPagination() {
        when(productMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(new Page<>(1, 10));
        var result = productService.queryProducts(1, 10, null, null);
        assertNotNull(result);
    }

    @Test
    void should_queryProductsWithCategory() {
        when(productMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(new Page<>(1, 10));
        var result = productService.queryProducts(1, 10, "数码", null);
        assertNotNull(result);
    }

    @Test
    void should_getProductById_when_exists() {
        Product product = TestDataFactory.createTestProduct(1L, "商品1", 99.00, 100);
        when(productMapper.selectById(1L)).thenReturn(product);

        Product result = productService.getProductById(1L);

        assertEquals("商品1", result.getName());
    }

    @Test
    void should_throwException_when_productNotFound() {
        when(productMapper.selectById(999L)).thenReturn(null);
        assertThrows(BusinessException.class, () -> productService.getProductById(999L));
    }

    @Test
    void should_updateProductSuccessfully() {
        Product existing = TestDataFactory.createTestProduct(1L, "旧名称", 99.00, 100);
        Product update = TestDataFactory.createTestProduct(null, "新名称", 199.00, 200);

        when(productMapper.selectById(1L)).thenReturn(existing);
        when(productMapper.updateById(any(Product.class))).thenReturn(1);

        Product result = productService.updateProduct(1L, update);

        assertEquals("新名称", result.getName());
        verify(productMapper).updateById(any(Product.class));
    }

    @Test
    void should_deleteProductSuccessfully() {
        Product product = TestDataFactory.createTestProduct(1L, "商品1", 99.00, 100);
        when(productMapper.selectById(1L)).thenReturn(product);
        when(productMapper.deleteById(1L)).thenReturn(1);

        productService.deleteProduct(1L);

        verify(productMapper).deleteById(1L);
    }

    @Test
    void should_throwException_when_deleteNonExistent() {
        when(productMapper.selectById(999L)).thenReturn(null);
        assertThrows(BusinessException.class, () -> productService.deleteProduct(999L));
    }
}
