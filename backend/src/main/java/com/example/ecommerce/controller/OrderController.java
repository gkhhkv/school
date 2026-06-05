package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.ecommerce.common.Result;
import com.example.ecommerce.domain.Order;
import com.example.ecommerce.domain.enums.OrderStatus;
import com.example.ecommerce.dto.OrderCreateRequest;
import com.example.ecommerce.dto.OrderStatusUpdateRequest;
import com.example.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单控制器。
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "订单管理", description = "订单增删改查接口")
public class OrderController {

    private final OrderService orderService;

    /**
     * 创建订单。
     *
     * @param request 订单创建请求
     * @return 创建的订单
     */
    @PostMapping
    @Operation(summary = "创建订单")
    public Result<Order> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        Order order = orderService.createOrder(request);
        return Result.success(order);
    }

    /**
     * 分页查询订单。
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param status   订单状态（可选）
     * @return 分页结果
     */
    @GetMapping
    @Operation(summary = "分页查询订单")
    public Result<IPage<Order>> queryOrders(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) OrderStatus status) {
        IPage<Order> page = orderService.queryOrders(pageNum, pageSize, status);
        return Result.success(page);
    }

    /**
     * 查询订单详情。
     *
     * @param orderNo 订单编号
     * @return 订单详情
     */
    @GetMapping("/{orderNo}")
    @Operation(summary = "查询订单详情")
    public Result<Order> getOrderDetail(@PathVariable String orderNo) {
        Order order = orderService.getOrderDetail(orderNo);
        return Result.success(order);
    }

    /**
     * 更新订单状态。
     *
     * @param orderNo 订单编号
     * @param request 状态更新请求
     * @return 空结果
     */
    @PutMapping("/{orderNo}/status")
    @Operation(summary = "更新订单状态")
    public Result<Void> updateOrderStatus(
            @PathVariable String orderNo,
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        orderService.updateOrderStatus(orderNo, request.getStatus());
        return Result.success(null);
    }

    /**
     * 取消订单。
     *
     * @param orderNo 订单编号
     * @return 空结果
     */
    @PutMapping("/{orderNo}/cancel")
    @Operation(summary = "取消订单")
    public Result<Void> cancelOrder(@PathVariable String orderNo) {
        orderService.cancelOrder(orderNo);
        return Result.success(null);
    }
}
