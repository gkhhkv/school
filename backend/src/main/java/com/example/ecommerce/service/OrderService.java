package com.example.ecommerce.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.ecommerce.domain.Order;
import com.example.ecommerce.domain.enums.OrderStatus;
import com.example.ecommerce.dto.OrderCreateRequest;

/**
 * 订单服务接口。
 */
public interface OrderService {

    /**
     * 创建订单。
     *
     * @param request 订单创建请求
     * @return 创建的订单
     */
    Order createOrder(OrderCreateRequest request);

    /**
     * 分页查询订单。
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param status   订单状态（可选）
     * @param userId   用户ID
     * @return 分页结果
     */
    IPage<Order> queryOrders(int pageNum, int pageSize, OrderStatus status, Long userId);

    /**
     * 查询订单详情（含商品明细）。
     *
     * @param orderNo 订单编号
     * @return 订单详情
     */
    Order getOrderDetail(String orderNo);

    /**
     * 更新订单状态。
     *
     * @param orderNo   订单编号
     * @param newStatus 新状态
     */
    void updateOrderStatus(String orderNo, OrderStatus newStatus);

    /**
     * 取消订单（仅待付款状态可取消，恢复库存）。
     *
     * @param orderNo 订单编号
     */
    void cancelOrder(String orderNo);
}
