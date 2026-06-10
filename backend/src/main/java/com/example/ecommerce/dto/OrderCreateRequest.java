package com.example.ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 创建订单请求。
 */
@Data
public class OrderCreateRequest {

    private Long userId;

    @NotBlank(message = "收货地址不能为空")
    private String shippingAddress;

    @NotBlank(message = "联系人不能为空")
    private String contactName;

    @NotBlank(message = "联系电话不能为空")
    private String contactPhone;

    @NotEmpty(message = "订单商品不能为空")
    @Valid
    private List<OrderItemRequest> items;
}
