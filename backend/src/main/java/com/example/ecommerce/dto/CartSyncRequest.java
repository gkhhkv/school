package com.example.ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 购物车同步请求。
 */
@Data
public class CartSyncRequest {

    @Valid
    private List<CartSyncItem> items;

    @Data
    public static class CartSyncItem {
        @NotNull(message = "商品ID不能为空")
        private Long productId;

        @Min(value = 1, message = "数量不能小于1")
        private Integer quantity;
    }
}
