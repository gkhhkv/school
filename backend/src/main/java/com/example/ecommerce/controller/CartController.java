package com.example.ecommerce.controller;

import com.example.ecommerce.common.Result;
import com.example.ecommerce.dto.CartItemResponse;
import com.example.ecommerce.dto.CartSyncRequest;
import com.example.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 购物车控制器。
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "购物车管理", description = "购物车增删改查接口")
public class CartController {

    private final CartService cartService;

    /**
     * 获取当前用户购物车。
     *
     * @param httpReq HTTP 请求
     * @return 购物车条目列表
     */
    @GetMapping
    @Operation(summary = "获取购物车")
    public Result<List<CartItemResponse>> getCart(HttpServletRequest httpReq) {
        Long userId = (Long) httpReq.getAttribute("userId");
        List<CartItemResponse> items = cartService.getCart(userId);
        return Result.success(items);
    }

    /**
     * 同步购物车（全量替换）。
     *
     * @param request 同步请求
     * @param httpReq HTTP 请求
     * @return 空结果
     */
    @PutMapping("/sync")
    @Operation(summary = "同步购物车")
    public Result<Void> syncCart(@Valid @RequestBody CartSyncRequest request,
                                  HttpServletRequest httpReq) {
        Long userId = (Long) httpReq.getAttribute("userId");
        cartService.syncCart(userId, request);
        return Result.success(null);
    }

    /**
     * 清空购物车。
     *
     * @param httpReq HTTP 请求
     * @return 空结果
     */
    @DeleteMapping
    @Operation(summary = "清空购物车")
    public Result<Void> clearCart(HttpServletRequest httpReq) {
        Long userId = (Long) httpReq.getAttribute("userId");
        cartService.clearCart(userId);
        return Result.success(null);
    }
}
