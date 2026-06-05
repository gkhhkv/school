# Step5 Vue Router + Pinia 购物车 — 技术设计

> 版本: 1.0 | 日期: 2026-06-05

## 1. 技术选型

| 项目 | 选型 | 说明 |
|------|------|------|
| 路由 | Vue Router 4 | Vue 3 官方路由，Hash 模式 |
| 状态管理 | Pinia | Vue 3 官方推荐，组合式 API 风格 |
| 页面 | views/ 目录 | 路由挂载的页面级组件 |

## 2. 路由设计

```js
// src/router/index.js
const routes = [
  { path: '/', name: 'home', component: ProductList },
  { path: '/cart', name: 'cart', component: ShoppingCart },
  { path: '/order-success', name: 'orderSuccess', component: OrderSuccess },
  { path: '/:pathMatch(.*)*', name: 'notFound', component: NotFound },
]
```

使用 `createWebHashHistory` 避免部署时服务器配置问题。

## 3. 购物车 Store 设计

```js
// src/stores/cart.js — defineStore('cart', () => { ... })
```

| 属性/方法 | 类型 | 说明 |
|-----------|------|------|
| items | ref([]) | 购物车项：[{ product, quantity }] |
| addToCart(product) | fn | 已存在则 quantity+1，否则 push |
| removeFromCart(productId) | fn | 按 productId 移除 |
| updateQuantity(productId, qty) | fn | 最小为 1 |
| clearCart() | fn | 清空 items |
| totalCount | computed | 总件数 |
| totalPrice | computed | 总金额 |

## 4. 页面组件

### 4.1 ProductList（改造）

- 每个商品卡片新增"加入购物车"按钮
- 点击后调用 cartStore.addToCart(product)

### 4.2 ShoppingCart（新增）

- 读取 cartStore.items 渲染列表
- + / - / 删除 按钮操作 store
- 提交订单 → clearCart → router.push('/order-success')

### 4.3 OrderSuccess（新增）

- 静态提示页 + router-link 返回首页

### 4.4 NotFound（新增）

- 静态提示页 + router-link 返回首页

## 5. 导航栏

App.vue 顶部导航栏：Logo | 首页 | 购物车(N) | 总金额

## 6. 项目结构（新增/修改）

```
frontend/src/
├── main.js              # 修改：注册 router + pinia
├── App.vue              # 修改：导航栏 + router-view
├── router/
│   └── index.js         # 新增：路由配置
├── stores/
│   └── cart.js          # 新增：购物车 Store
├── components/
│   └── ProductList.vue  # 修改：添加"加入购物车"按钮
└── views/
    ├── ShoppingCart.vue  # 新增
    ├── OrderSuccess.vue  # 新增
    └── NotFound.vue      # 新增
```
