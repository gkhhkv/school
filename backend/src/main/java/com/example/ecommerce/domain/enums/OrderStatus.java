package com.example.ecommerce.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.example.ecommerce.common.ResultCode;
import com.example.ecommerce.exception.BusinessException;

/**
 * 订单状态枚举。
 */
public enum OrderStatus {

    PENDING_PAYMENT("待付款"),
    PAID("已付款"),
    SHIPPED("已发货"),
    COMPLETED("已完成"),
    CANCELLED("已取消");

    @EnumValue
    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 校验当前状态是否可转换到目标状态。
     *
     * @param target 目标状态
     * @return true 表示允许转换
     */
    public boolean canTransitionTo(OrderStatus target) {
        if (target == null) {
            return false;
        }
        return switch (this) {
            case PENDING_PAYMENT -> target == PAID || target == CANCELLED;
            case PAID -> target == SHIPPED || target == CANCELLED;
            case SHIPPED -> target == COMPLETED;
            case COMPLETED, CANCELLED -> false;
        };
    }

    /**
     * 执行状态转换，转换不合法时抛出异常。
     *
     * @param target 目标状态
     * @throws BusinessException 状态转换不合法
     */
    public void transitionTo(OrderStatus target) {
        if (!canTransitionTo(target)) {
            throw new BusinessException(ResultCode.INVALID_STATUS_TRANSITION);
        }
    }
}
