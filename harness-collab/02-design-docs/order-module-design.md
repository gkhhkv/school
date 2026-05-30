# 订单模块技术设计

> 版本: 1.0 | 日期: 2026-05-30

## 1. 四层架构映射

| 层 | 类 | 职责 |
|----|-----|------|
| Controller | OrderController | 接收 HTTP 请求，参数校验，调用 Service |
| Service | OrderService / OrderServiceImpl | 业务逻辑：库存校验、金额计算、状态流转 |
| Repository | OrderMapper / OrderItemMapper | 数据库访问（MyBatis Plus BaseMapper） |
| Domain | Order / OrderItem / OrderStatus | 实体映射与状态机 |

## 2. 数据库设计

### 2.1 订单主表 (`order`)

| 列名 | 类型 | 约束 | 说明 |
|------|------|------|------|
| order_id | BIGINT AUTO_INCREMENT | PK | 代理主键 |
| order_no | VARCHAR(32) | UNIQUE, NOT NULL | 业务订单号 ORD+时间戳+随机数 |
| user_id | BIGINT | FK→user.user_id, NOT NULL | 下单用户 |
| total_amount | DECIMAL(12,2) | NOT NULL | 订单总金额 |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'PENDING_PAYMENT' | 订单状态 |
| paid_at | DATETIME | NULL | 支付时间 |
| shipped_at | DATETIME | NULL | 发货时间 |
| shipping_address | VARCHAR(255) | NOT NULL | 收货地址 |
| contact_name | VARCHAR(50) | NOT NULL | 联系人 |
| contact_phone | VARCHAR(20) | NOT NULL | 联系电话 |
| created_at | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | DEFAULT CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

### 2.2 订单明细表 (order_item)

| 列名 | 类型 | 约束 | 说明 |
|------|------|------|------|
| item_id | BIGINT AUTO_INCREMENT | PK | 明细主键 |
| order_id | BIGINT | FK→order.order_id, NOT NULL | 所属订单 |
| product_id | BIGINT | FK→product.product_id, NOT NULL | 商品 |
| quantity | INT | NOT NULL | 购买数量 |
| unit_price | DECIMAL(10,2) | NOT NULL | 下单时单价 |
| subtotal | DECIMAL(12,2) | NOT NULL | 小计(unit_price * quantity) |

## 3. 状态机设计

```
PENDING_PAYMENT → PAID        (付款)
PENDING_PAYMENT → CANCELLED   (超时/手动取消)
PAID            → SHIPPED     (商家发货)
PAID            → CANCELLED   (退款取消)
SHIPPED         → COMPLETED   (确认收货)
COMPLETED       → (终态)
CANCELLED       → (终态)
```

## 4. 订单号生成策略

格式: `ORD` + `yyyyMMddHHmmss` + 6位随机数 (例: `ORD20260530143025123456`)
- 前缀 ORD 便于识别
- 时间戳到秒保证趋势递增
- 6位随机数降低并发碰撞概率

## 5. API 设计

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/orders | 创建订单 |
| GET | /api/orders?pageNum=1&pageSize=10&status= | 分页查询 |
| GET | /api/orders/{orderNo} | 订单详情 |
| PUT | /api/orders/{orderNo}/status | 更新状态 |
| PUT | /api/orders/{orderNo}/cancel | 取消订单 |
