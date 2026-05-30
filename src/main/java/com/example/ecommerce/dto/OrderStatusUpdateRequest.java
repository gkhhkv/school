package com.example.ecommerce.dto;

import com.example.ecommerce.domain.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 订单状态更新请求。
 */
@Data
public class OrderStatusUpdateRequest {

    @NotNull(message = "目标状态不能为空")
    private OrderStatus status;
}
