package com.example.ecommerce.support;

import com.example.ecommerce.domain.Order;
import com.example.ecommerce.domain.OrderItem;
import com.example.ecommerce.domain.Product;
import com.example.ecommerce.domain.User;
import com.example.ecommerce.domain.enums.OrderStatus;
import com.example.ecommerce.dto.OrderCreateRequest;
import com.example.ecommerce.dto.OrderItemRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 测试数据工厂。
 */
public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static User createTestUser(Long id) {
        User user = new User();
        user.setUserId(id);
        user.setUsername("testuser" + id);
        user.setPassword("password");
        user.setPhone("13800138000");
        user.setAddress("测试地址");
        return user;
    }

    public static Product createTestProduct(Long id, String name, double price, int stock) {
        Product product = new Product();
        product.setProductId(id);
        product.setName(name);
        product.setCategory("数码");
        product.setPrice(BigDecimal.valueOf(price));
        product.setStock(stock);
        product.setImage("/img/p" + id + ".jpg");
        return product;
    }

    public static OrderItemRequest createOrderItemRequest(Long productId, int quantity) {
        OrderItemRequest req = new OrderItemRequest();
        req.setProductId(productId);
        req.setQuantity(quantity);
        return req;
    }

    public static OrderCreateRequest createOrderCreateRequest(Long userId, Long productId, int quantity) {
        OrderCreateRequest req = new OrderCreateRequest();
        req.setUserId(userId);
        req.setShippingAddress("测试收货地址");
        req.setContactName("测试联系人");
        req.setContactPhone("13800138000");
        OrderItemRequest itemReq = createOrderItemRequest(productId, quantity);
        req.setItems(List.of(itemReq));
        return req;
    }

    public static Order createTestOrder(Long orderId, String orderNo, Long userId, OrderStatus status) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalAmount(BigDecimal.valueOf(99.00));
        order.setStatus(status);
        order.setShippingAddress("测试地址");
        order.setContactName("测试联系人");
        order.setContactPhone("13800138000");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        return order;
    }

    public static OrderItem createTestOrderItem(Long itemId, Long orderId, Long productId, int quantity, BigDecimal unitPrice) {
        OrderItem item = new OrderItem();
        item.setItemId(itemId);
        item.setOrderId(orderId);
        item.setProductId(productId);
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);
        item.setSubtotal(unitPrice.multiply(BigDecimal.valueOf(quantity)));
        return item;
    }
}
