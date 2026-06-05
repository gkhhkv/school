# 功能资产总表

> 最后更新: 2026-06-05

## 订单模块

| API | 方法 | 路径 | 说明 |
|-----|------|------|------|
| 创建订单 | POST | /api/orders | 库存校验 → 金额计算 → 库存扣减 |
| 分页查询 | GET | /api/orders | 支持状态筛选 |
| 订单详情 | GET | /api/orders/{orderNo} | 含商品明细 |
| 更新状态 | PUT | /api/orders/{orderNo}/status | 状态机校验 |
| 取消订单 | PUT | /api/orders/{orderNo}/cancel | 仅待付款 / 恢复库存 |

## 商品模块

| API | 方法 | 路径 | 说明 |
|-----|------|------|------|
| 创建商品 | POST | /api/products | 参数校验 |
| 分页查询 | GET | /api/products | 支持关键词搜索 |
| 商品详情 | GET | /api/products/{id} | 按 ID 查询 |
| 更新商品 | PUT | /api/products/{id} | 全字段更新 |
| 删除商品 | DELETE | /api/products/{id} | 物理删除 |

## 前端组件

| 组件 | 文件 | 说明 |
|------|------|------|
| 促销标签 | PromoLabel.vue | 接收促销类型 prop，展示对应颜色标签；无促销时隐藏 |
| 倒计时 | Countdown.vue | 接收 endTime prop，每秒更新天/时/分/秒；到期 emit expired |
| 商品详情 | ProductDetail.vue | 父组件，集成促销标签和倒计时，支持动态切换传值验证 |
| Axios 请求封装 | request.js | Axios 实例，baseURL + 请求/响应拦截器，解包 Result 格式 |
| 商品列表 | ProductList.vue | 商品卡片分页列表 + 分类筛选 + 加入购物车按钮 |
| 路由配置 | router/index.js | Hash 模式：首页/购物车/下单成功/404 |
| 购物车 Store | stores/cart.js | Pinia 状态：增/删/改数量/清空/总价 |
| 购物车页 | ShoppingCart.vue | 购物车列表 + 数量调整 + 提交订单 |
| 下单成功 | OrderSuccess.vue | 下单成功提示 + 返回首页 |
| 404 页面 | NotFound.vue | 页面不存在提示 + 返回首页 |

## 数据库表

| 表名 | 说明 | 状态 |
|------|------|------|
| user | 用户表 | 已建 |
| product | 商品表 | 已建 |
| `order` | 订单主表 | 已建 |
| order_item | 订单明细表 | 已建 |
| cart | 购物车表 | 已建 |
