package com.example.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建商品请求。
 */
@Data
public class ProductCreateRequest {

    @NotBlank(message = "商品名称不能为空")
    private String name;

    @NotNull(message = "商品单价不能为空")
    @Min(value = 0, message = "单价不能小于0")
    private BigDecimal price;

    @NotNull(message = "库存数量不能为空")
    @Min(value = 0, message = "库存不能小于0")
    private Integer stock;

    private String image;
}
