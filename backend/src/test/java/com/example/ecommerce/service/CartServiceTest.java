package com.example.ecommerce.service;

import com.example.ecommerce.domain.Cart;
import com.example.ecommerce.domain.Product;
import com.example.ecommerce.dto.CartItemResponse;
import com.example.ecommerce.dto.CartSyncRequest;
import com.example.ecommerce.repository.CartMapper;
import com.example.ecommerce.repository.ProductMapper;
import com.example.ecommerce.service.impl.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartMapper cartMapper;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void should_returnCartItems_when_userHasCart() {
        Cart cartItem = new Cart();
        cartItem.setCartId(1L);
        cartItem.setUserId(1L);
        cartItem.setProductId(10L);
        cartItem.setQuantity(2);

        Product product = new Product();
        product.setProductId(10L);
        product.setName("测试商品");
        product.setPrice(BigDecimal.valueOf(99.00));
        product.setImage("/img/test.jpg");
        product.setStock(50);

        when(cartMapper.selectList(any())).thenReturn(List.of(cartItem));
        when(productMapper.selectBatchIds(anyList())).thenReturn(List.of(product));

        List<CartItemResponse> result = cartService.getCart(1L);

        assertEquals(1, result.size());
        CartItemResponse item = result.get(0);
        assertEquals(10L, item.getProductId());
        assertEquals("测试商品", item.getProductName());
        assertEquals(2, item.getQuantity());
    }

    @Test
    void should_syncCart_when_replacingAllItems() {
        CartSyncRequest.CartSyncItem syncItem = new CartSyncRequest.CartSyncItem();
        syncItem.setProductId(10L);
        syncItem.setQuantity(3);
        CartSyncRequest request = new CartSyncRequest();
        request.setItems(List.of(syncItem));

        when(cartMapper.delete(any())).thenReturn(1);
        when(cartMapper.insert(any(Cart.class))).thenReturn(1);

        cartService.syncCart(1L, request);

        verify(cartMapper).delete(any());
        verify(cartMapper).insert(any(Cart.class));
    }

    @Test
    void should_clearCart_when_called() {
        when(cartMapper.delete(any())).thenReturn(1);

        cartService.clearCart(1L);

        verify(cartMapper).delete(any());
    }
}
