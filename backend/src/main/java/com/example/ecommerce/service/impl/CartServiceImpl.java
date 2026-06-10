package com.example.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ecommerce.domain.Cart;
import com.example.ecommerce.domain.Product;
import com.example.ecommerce.dto.CartItemResponse;
import com.example.ecommerce.dto.CartSyncRequest;
import com.example.ecommerce.repository.CartMapper;
import com.example.ecommerce.repository.ProductMapper;
import com.example.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 购物车服务实现。
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    @Override
    public List<CartItemResponse> getCart(Long userId) {
        List<Cart> cartItems = cartMapper.selectList(
                new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, userId));
        if (cartItems.isEmpty()) {
            return List.of();
        }
        List<Long> productIds = cartItems.stream().map(Cart::getProductId).toList();
        List<Product> products = productMapper.selectBatchIds(productIds);
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductId, p -> p));

        List<CartItemResponse> result = new ArrayList<>();
        for (Cart cart : cartItems) {
            Product p = productMap.get(cart.getProductId());
            if (p == null) continue;
            CartItemResponse item = new CartItemResponse();
            item.setProductId(p.getProductId());
            item.setProductName(p.getName());
            item.setPrice(p.getPrice());
            item.setImage(p.getImage());
            item.setStock(p.getStock());
            item.setQuantity(cart.getQuantity());
            result.add(item);
        }
        return result;
    }

    @Override
    @Transactional
    public void syncCart(Long userId, CartSyncRequest request) {
        cartMapper.delete(new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, userId));
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            for (CartSyncRequest.CartSyncItem item : request.getItems()) {
                Cart cart = new Cart();
                cart.setUserId(userId);
                cart.setProductId(item.getProductId());
                cart.setQuantity(item.getQuantity());
                cartMapper.insert(cart);
            }
        }
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        cartMapper.delete(new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, userId));
    }
}
