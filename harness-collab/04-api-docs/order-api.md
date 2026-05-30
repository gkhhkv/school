# 订单模块 API 文档

> 版本: 1.0 | 基础路径: `/api/orders`

## 接口列表

### 1. 创建订单

**POST** `/api/orders`

**请求体:**
```json
{
  "userId": 1,
  "shippingAddress": "北京市朝阳区测试路100号",
  "contactName": "张三",
  "contactPhone": "13800138000",
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

**响应:**
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "orderId": 1,
    "orderNo": "ORD20260530143025123456",
    "userId": 1,
    "totalAmount": 198.00,
    "status": "PENDING_PAYMENT",
    "shippingAddress": "北京市朝阳区测试路100号",
    "contactName": "张三",
    "contactPhone": "13800138000",
    "items": [...]
  }
}
```

**错误码:** 2001(库存不足), 400(参数校验失败)

---

### 2. 分页查询订单

**GET** `/api/orders?pageNum=1&pageSize=10&status=PENDING_PAYMENT`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pageNum | int | 否 | 页码(默认1) |
| pageSize | int | 否 | 每页条数(默认10) |
| status | string | 否 | 订单状态筛选 |

**响应:**
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "records": [...],
    "total": 100,
    "size": 10,
    "current": 1
  }
}
```

---

### 3. 查询订单详情

**GET** `/api/orders/{orderNo}`

**响应:** 同创建订单返回结构，包含 `items` 明细列表。

**错误码:** 2002(订单不存在)

---

### 4. 更新订单状态

**PUT** `/api/orders/{orderNo}/status`

**请求体:**
```json
{
  "status": "PAID"
}
```

**响应:** `{ "code": 200, "message": "成功", "data": null }`

**允许的状态转换:**
- PENDING_PAYMENT → PAID
- PAID → SHIPPED
- SHIPPED → COMPLETED

**错误码:** 2002(订单不存在), 2003(无效状态转换)

---

### 5. 取消订单

**PUT** `/api/orders/{orderNo}/cancel`

**响应:** `{ "code": 200, "message": "成功", "data": null }`

**限制:** 仅 PENDING_PAYMENT 状态可取消，取消后自动恢复库存。

**错误码:** 2002(订单不存在), 2004(订单无法取消)
