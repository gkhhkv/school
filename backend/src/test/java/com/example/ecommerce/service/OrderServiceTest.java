package com.example.ecommerce.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ecommerce.common.ResultCode;
import com.example.ecommerce.domain.Order;
import com.example.ecommerce.domain.OrderItem;
import com.example.ecommerce.domain.Product;
import com.example.ecommerce.domain.enums.OrderStatus;
import com.example.ecommerce.dto.OrderCreateRequest;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.repository.OrderItemMapper;
import com.example.ecommerce.repository.OrderMapper;
import com.example.ecommerce.repository.ProductMapper;
import com.example.ecommerce.service.impl.OrderServiceImpl;
import com.example.ecommerce.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    // ========== createOrder ==========

    @Test
    void should_createOrderSuccessfully_when_stockIsSufficient() {
        Product product = TestDataFactory.createTestProduct(1L, "商品1", 99.00, 100);
        OrderCreateRequest request = TestDataFactory.createOrderCreateRequest(1L, 1L, 2);

        when(productMapper.selectBatchIds(anyList())).thenReturn(List.of(product));
        when(productMapper.updateById(any(Product.class))).thenReturn(1);
        when(orderMapper.insert(any(Order.class))).thenReturn(1);
        when(orderItemMapper.insert(any(OrderItem.class))).thenReturn(1);

        Order order = orderService.createOrder(request);

        assertNotNull(order);
        assertNotNull(order.getOrderNo());
        assertTrue(order.getOrderNo().startsWith("ORD"));
        assertEquals(OrderStatus.PENDING_PAYMENT, order.getStatus());
        assertEquals(0, BigDecimal.valueOf(198.00).compareTo(order.getTotalAmount()));
        assertEquals(1, order.getItems().size());
        assertEquals(98, product.getStock());
        verify(orderMapper).insert(any(Order.class));
        verify(orderItemMapper).insert(any(OrderItem.class));
    }

    @Test
    void should_throwException_when_productNotFound() {
        OrderCreateRequest request = TestDataFactory.createOrderCreateRequest(1L, 999L, 1);
        when(productMapper.selectBatchIds(anyList())).thenReturn(List.of());

        BusinessException ex = assertThrows(BusinessException.class, () -> orderService.createOrder(request));
        assertEquals(ResultCode.NOT_FOUND.getCode(), ex.getCode());
    }

    @Test
    void should_throwInsufficientStockException_when_stockIsZero() {
        Product product = TestDataFactory.createTestProduct(1L, "商品1", 99.00, 0);
        OrderCreateRequest request = TestDataFactory.createOrderCreateRequest(1L, 1L, 1);

        when(productMapper.selectBatchIds(anyList())).thenReturn(List.of(product));

        BusinessException ex = assertThrows(BusinessException.class, () -> orderService.createOrder(request));
        assertEquals(ResultCode.INSUFFICIENT_STOCK.getCode(), ex.getCode());
    }

    @Test
    void should_throwInsufficientStockException_when_quantityExceedsStock() {
        Product product = TestDataFactory.createTestProduct(1L, "商品1", 99.00, 3);
        OrderCreateRequest request = TestDataFactory.createOrderCreateRequest(1L, 1L, 5);

        when(productMapper.selectBatchIds(anyList())).thenReturn(List.of(product));

        BusinessException ex = assertThrows(BusinessException.class, () -> orderService.createOrder(request));
        assertEquals(ResultCode.INSUFFICIENT_STOCK.getCode(), ex.getCode());
    }

    @Test
    void should_calculateTotalAmountCorrectly_when_multipleItems() {
        Product p1 = TestDataFactory.createTestProduct(1L, "商品1", 100.00, 50);
        Product p2 = TestDataFactory.createTestProduct(2L, "商品2", 50.00, 100);
        OrderCreateRequest request = new OrderCreateRequest();
        request.setUserId(1L);
        request.setShippingAddress("地址");
        request.setContactName("联系人");
        request.setContactPhone("13800138000");
        request.setItems(List.of(
                TestDataFactory.createOrderItemRequest(1L, 2),
                TestDataFactory.createOrderItemRequest(2L, 3)
        ));

        when(productMapper.selectBatchIds(anyList())).thenReturn(List.of(p1, p2));
        when(productMapper.updateById(any(Product.class))).thenReturn(1);
        when(orderMapper.insert(any(Order.class))).thenReturn(1);
        when(orderItemMapper.insert(any(OrderItem.class))).thenReturn(1);

        Order order = orderService.createOrder(request);

        assertEquals(0, BigDecimal.valueOf(350.00).compareTo(order.getTotalAmount()));
    }

    // ========== queryOrders ==========

    @Test
    void should_returnPaginatedOrders_when_queryWithoutStatus() {
        when(orderMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(new Page<>(1, 10));

        IPage<Order> result = orderService.queryOrders(1, 10, null);

        assertNotNull(result);
        verify(orderMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    void should_filterOrdersByStatus_when_statusProvided() {
        when(orderMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(new Page<>(1, 5));

        orderService.queryOrders(1, 5, OrderStatus.PAID);

        verify(orderMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    // ========== getOrderDetail ==========

    @Test
    void should_returnOrderWithItems_when_orderExists() {
        String orderNo = "ORD20260530120000000001";
        Order order = TestDataFactory.createTestOrder(1L, orderNo, 1L, OrderStatus.PENDING_PAYMENT);
        List<OrderItem> items = List.of(
                TestDataFactory.createTestOrderItem(1L, 1L, 1L, 2, BigDecimal.valueOf(99.00))
        );

        when(orderMapper.selectByOrderNo(orderNo)).thenReturn(order);
        when(orderItemMapper.selectByOrderId(1L)).thenReturn(items);

        Order result = orderService.getOrderDetail(orderNo);

        assertNotNull(result);
        assertNotNull(result.getItems());
        assertEquals(1, result.getItems().size());
    }

    @Test
    void should_throwOrderNotFoundException_when_orderNoDoesNotExist() {
        when(orderMapper.selectByOrderNo("INVALID")).thenReturn(null);

        BusinessException ex = assertThrows(BusinessException.class, () -> orderService.getOrderDetail("INVALID"));
        assertEquals(ResultCode.ORDER_NOT_FOUND.getCode(), ex.getCode());
    }

    // ========== updateOrderStatus ==========

    @Test
    void should_updateStatusToPaid_when_transitionIsValid() {
        String orderNo = "ORD20260530120000000001";
        Order order = TestDataFactory.createTestOrder(1L, orderNo, 1L, OrderStatus.PENDING_PAYMENT);

        when(orderMapper.selectByOrderNo(orderNo)).thenReturn(order);
        when(orderMapper.updateById(any(Order.class))).thenReturn(1);

        orderService.updateOrderStatus(orderNo, OrderStatus.PAID);

        assertEquals(OrderStatus.PAID, order.getStatus());
        assertNotNull(order.getPaidAt());
        verify(orderMapper).updateById(order);
    }

    @Test
    void should_throwInvalidTransitionException_when_statusTransitionInvalid() {
        String orderNo = "ORD20260530120000000001";
        Order order = TestDataFactory.createTestOrder(1L, orderNo, 1L, OrderStatus.PENDING_PAYMENT);

        when(orderMapper.selectByOrderNo(orderNo)).thenReturn(order);

        assertThrows(BusinessException.class, () -> orderService.updateOrderStatus(orderNo, OrderStatus.COMPLETED));
    }

    @Test
    void should_throwOrderNotFound_when_updateNonExistentOrder() {
        when(orderMapper.selectByOrderNo("INVALID")).thenReturn(null);
        assertThrows(BusinessException.class, () -> orderService.updateOrderStatus("INVALID", OrderStatus.PAID));
    }

    // ========== cancelOrder ==========

    @Test
    void should_cancelOrderAndRestoreStock_when_orderIsPending() {
        String orderNo = "ORD20260530120000000001";
        Order order = TestDataFactory.createTestOrder(1L, orderNo, 1L, OrderStatus.PENDING_PAYMENT);
        Product product = TestDataFactory.createTestProduct(1L, "商品1", 99.00, 50);
        OrderItem item = TestDataFactory.createTestOrderItem(1L, 1L, 1L, 3, BigDecimal.valueOf(99.00));

        when(orderMapper.selectByOrderNo(orderNo)).thenReturn(order);
        when(orderItemMapper.selectByOrderId(1L)).thenReturn(List.of(item));
        when(productMapper.selectById(1L)).thenReturn(product);
        when(productMapper.updateById(any(Product.class))).thenReturn(1);
        when(orderMapper.updateById(any(Order.class))).thenReturn(1);

        orderService.cancelOrder(orderNo);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertEquals(53, product.getStock());
    }

    @Test
    void should_throwCannotCancelException_when_orderIsNotPending() {
        String orderNo = "ORD20260530120000000001";
        Order order = TestDataFactory.createTestOrder(1L, orderNo, 1L, OrderStatus.PAID);

        when(orderMapper.selectByOrderNo(orderNo)).thenReturn(order);

        BusinessException ex = assertThrows(BusinessException.class, () -> orderService.cancelOrder(orderNo));
        assertEquals(ResultCode.ORDER_CANNOT_CANCEL.getCode(), ex.getCode());
    }
}
