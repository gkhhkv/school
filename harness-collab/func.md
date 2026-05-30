# 功能资产总表

> 最后更新: 2026-05-30

## 订单模块

| API | 方法 | 路径 | 说明 |
|-----|------|------|------|
| 创建订单 | POST | /api/orders | 库存校验 → 金额计算 → 库存扣减 |
| 分页查询 | GET | /api/orders | 支持状态筛选 |
| 订单详情 | GET | /api/orders/{orderNo} | 含商品明细 |
| 更新状态 | PUT | /api/orders/{orderNo}/status | 状态机校验 |
| 取消订单 | PUT | /api/orders/{orderNo}/cancel | 仅待付款 / 恢复库存 |

## 数据库表

| 表名 | 说明 | 状态 |
|------|------|------|
| user | 用户表 | 已建 |
| product | 商品表 | 已建 |
| `order` | 订单主表 | 已建 |
| order_item | 订单明细表 | 已建 |
| cart | 购物车表 | 已建 |
