package com.example.ecommerce.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 购物车条目响应，含商品详情。
 */
@Data
public class CartItemResponse {

    private Long productId;
    private String productName;
    private BigDecimal price;
    private String image;
    private Integer stock;
    private Integer quantity;
}
