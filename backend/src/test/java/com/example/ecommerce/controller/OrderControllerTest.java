package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ecommerce.common.ResultCode;
import com.example.ecommerce.domain.Order;
import com.example.ecommerce.domain.enums.OrderStatus;
import com.example.ecommerce.dto.OrderCreateRequest;
import com.example.ecommerce.dto.OrderItemRequest;
import com.example.ecommerce.dto.OrderStatusUpdateRequest;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.exception.GlobalExceptionHandler;
import com.example.ecommerce.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // ========== POST /api/orders ==========

    @Test
    void should_returnCreatedOrder_when_validCreateRequest() throws Exception {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setUserId(1L);
        request.setShippingAddress("测试地址");
        request.setContactName("测试");
        request.setContactPhone("13800138000");
        request.setItems(List.of(itemReq(1L, 2)));

        Order order = new Order();
        order.setOrderNo("ORD20260530120000000001");
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        when(orderService.createOrder(any())).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.orderNo").value("ORD20260530120000000001"));
    }

    @Test
    void should_return400_when_createRequestMissingUserId() throws Exception {
        String body = "{\"shippingAddress\":\"addr\",\"contactName\":\"name\",\"contactPhone\":\"138\",\"items\":[{\"productId\":1,\"quantity\":1}]}";

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return400_when_createRequestEmptyItems() throws Exception {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setUserId(1L);
        request.setShippingAddress("地址");
        request.setContactName("联系人");
        request.setContactPhone("13800138000");
        request.setItems(Collections.emptyList());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_returnBusinessError_when_stockInsufficient() throws Exception {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setUserId(1L);
        request.setShippingAddress("测试地址");
        request.setContactName("测试");
        request.setContactPhone("13800138000");
        request.setItems(List.of(itemReq(1L, 999)));

        when(orderService.createOrder(any()))
                .thenThrow(new BusinessException(ResultCode.INSUFFICIENT_STOCK));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(2001));
    }

    // ========== GET /api/orders ==========

    @Test
    void should_returnPaginatedOrders_when_queryWithoutFilters() throws Exception {
        Page<Order> page = new Page<>(1, 10);
        when(orderService.queryOrders(anyInt(), anyInt(), any())).thenReturn(page);

        mockMvc.perform(get("/api/orders")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void should_returnOrdersFilteredByStatus_when_queryWithStatus() throws Exception {
        Page<Order> page = new Page<>(1, 10);
        when(orderService.queryOrders(1, 10, OrderStatus.PAID)).thenReturn(page);

        mockMvc.perform(get("/api/orders")
                        .param("pageNum", "1")
                        .param("pageSize", "10")
                        .param("status", "PAID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(orderService).queryOrders(1, 10, OrderStatus.PAID);
    }

    // ========== GET /api/orders/{orderNo} ==========

    @Test
    void should_returnOrderDetail_when_validOrderNo() throws Exception {
        Order order = new Order();
        order.setOrderNo("ORD123");
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        when(orderService.getOrderDetail("ORD123")).thenReturn(order);

        mockMvc.perform(get("/api/orders/ORD123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.orderNo").value("ORD123"));
    }

    @Test
    void should_returnError_when_orderNotFound() throws Exception {
        when(orderService.getOrderDetail("INVALID"))
                .thenThrow(new BusinessException(ResultCode.ORDER_NOT_FOUND));

        mockMvc.perform(get("/api/orders/INVALID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(2002));
    }

    // ========== PUT /api/orders/{orderNo}/status ==========

    @Test
    void should_updateStatus_when_validTransition() throws Exception {
        OrderStatusUpdateRequest req = new OrderStatusUpdateRequest();
        req.setStatus(OrderStatus.PAID);

        mockMvc.perform(put("/api/orders/ORD123/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void should_returnError_when_invalidStatusTransition() throws Exception {
        OrderStatusUpdateRequest req = new OrderStatusUpdateRequest();
        req.setStatus(OrderStatus.COMPLETED);

        doThrow(new BusinessException(ResultCode.INVALID_STATUS_TRANSITION))
                .when(orderService).updateOrderStatus(anyString(), any());

        mockMvc.perform(put("/api/orders/ORD123/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(2003));
    }

    @Test
    void should_return400_when_statusUpdateMissingStatus() throws Exception {
        mockMvc.perform(put("/api/orders/ORD123/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ========== PUT /api/orders/{orderNo}/cancel ==========

    @Test
    void should_cancelOrder_when_orderIsPending() throws Exception {
        mockMvc.perform(put("/api/orders/ORD123/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void should_returnError_when_cancelUncancelableOrder() throws Exception {
        doThrow(new BusinessException(ResultCode.ORDER_CANNOT_CANCEL))
                .when(orderService).cancelOrder("ORD123");

        mockMvc.perform(put("/api/orders/ORD123/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(2004));
    }

    private static OrderItemRequest itemReq(Long productId, int quantity) {
        OrderItemRequest req = new OrderItemRequest();
        req.setProductId(productId);
        req.setQuantity(quantity);
        return req;
    }
}
