package com.example.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ecommerce.common.ResultCode;
import com.example.ecommerce.domain.Order;
import com.example.ecommerce.domain.OrderItem;
import com.example.ecommerce.domain.Product;
import com.example.ecommerce.domain.enums.OrderStatus;
import com.example.ecommerce.dto.OrderCreateRequest;
import com.example.ecommerce.dto.OrderItemRequest;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.repository.OrderItemMapper;
import com.example.ecommerce.repository.OrderMapper;
import com.example.ecommerce.repository.ProductMapper;
import com.example.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 订单服务实现。
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private static final DateTimeFormatter ORDER_NO_FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final Random RANDOM = new Random();

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public Order createOrder(OrderCreateRequest request) {
        List<OrderItemRequest> itemRequests = request.getItems();
        List<Long> productIds = itemRequests.stream()
                .map(OrderItemRequest::getProductId)
                .distinct()
                .toList();

        List<Product> products = productMapper.selectBatchIdsForUpdate(productIds);
        if (products.size() != productIds.size()) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "部分商品不存在");
        }
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductId, p -> p));

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : itemRequests) {
            Product product = productMap.get(itemReq.getProductId());
            if (product.getStock() < itemReq.getQuantity()) {
                throw new BusinessException(ResultCode.INSUFFICIENT_STOCK);
            }
            BigDecimal unitPrice = product.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            totalAmount = totalAmount.add(subtotal);

            OrderItem item = new OrderItem();
            item.setProductId(itemReq.getProductId());
            item.setProductName(product.getName());
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(unitPrice);
            item.setSubtotal(subtotal);
            orderItems.add(item);

            product.setStock(product.getStock() - itemReq.getQuantity());
            productMapper.updateById(product);
        }

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(request.getUserId());
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setShippingAddress(request.getShippingAddress());
        order.setContactName(request.getContactName());
        order.setContactPhone(request.getContactPhone());

        orderMapper.insert(order);

        for (OrderItem item : orderItems) {
            item.setOrderId(order.getOrderId());
            orderItemMapper.insert(item);
        }

        order.setItems(orderItems);
        log.info("订单创建成功: orderNo={}, totalAmount={}", order.getOrderNo(), totalAmount);
        return order;
    }

    @Override
    public IPage<Order> queryOrders(int pageNum, int pageSize, OrderStatus status, Long userId) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreatedAt);
        return orderMapper.selectPage(page, wrapper);
    }

    @Override
    public Order getOrderDetail(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        List<OrderItem> items = orderItemMapper.selectByOrderId(order.getOrderId());

        List<Long> productIds = items.stream().map(OrderItem::getProductId).distinct().toList();
        Map<Long, String> nameMap = productMapper.selectBatchIds(productIds).stream()
                .collect(Collectors.toMap(Product::getProductId, Product::getName));
        items.forEach(i -> i.setProductName(nameMap.getOrDefault(i.getProductId(), "未知商品")));

        order.setItems(items);
        return order;
    }

    @Override
    public void updateOrderStatus(String orderNo, OrderStatus newStatus) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        OrderStatus current = order.getStatus();
        current.transitionTo(newStatus);

        order.setStatus(newStatus);
        if (newStatus == OrderStatus.PAID) {
            order.setPaidAt(LocalDateTime.now());
        } else if (newStatus == OrderStatus.SHIPPED) {
            order.setShippedAt(LocalDateTime.now());
        }
        orderMapper.updateById(order);
        log.info("订单状态更新: orderNo={}, {} -> {}", orderNo, current, newStatus);
    }

    @Override
    @Transactional
    public void cancelOrder(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BusinessException(ResultCode.ORDER_CANNOT_CANCEL);
        }

        List<OrderItem> items = orderItemMapper.selectByOrderId(order.getOrderId());
        for (OrderItem item : items) {
            Product product = productMapper.selectById(item.getProductId());
            if (product != null) {
                product.setStock(product.getStock() + item.getQuantity());
                productMapper.updateById(product);
            }
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderMapper.updateById(order);
        log.info("订单已取消: orderNo={}, 库存已恢复", orderNo);
    }

    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(ORDER_NO_FMT);
        String random = String.format("%06d", RANDOM.nextInt(1_000_000));
        return "ORD" + timestamp + random;
    }
}
