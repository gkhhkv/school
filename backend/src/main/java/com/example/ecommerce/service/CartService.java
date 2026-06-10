package com.example.ecommerce.service;

import com.example.ecommerce.dto.CartItemResponse;
import com.example.ecommerce.dto.CartSyncRequest;

import java.util.List;

/**
 * 购物车服务接口。
 */
public interface CartService {

    /**
     * 获取用户购物车，含商品详情。
     *
     * @param userId 用户ID
     * @return 购物车条目列表
     */
    List<CartItemResponse> getCart(Long userId);

    /**
     * 同步购物车（全量替换）。
     *
     * @param userId  用户ID
     * @param request 同步请求
     */
    void syncCart(Long userId, CartSyncRequest request);

    /**
     * 清空购物车。
     *
     * @param userId 用户ID
     */
    void clearCart(Long userId);
}
