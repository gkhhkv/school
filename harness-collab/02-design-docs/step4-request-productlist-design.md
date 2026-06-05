# Step4 异步请求封装与商品列表 — 技术设计

> 版本: 1.0 | 日期: 2026-06-05

## 1. 技术选型

| 项目 | 选型 | 说明 |
|------|------|------|
| HTTP 客户端 | Axios | Promise 风格，支持拦截器 |
| 代理方案 | Vite server.proxy | 开发环境转发 /api → localhost:8080 |
| 状态管理 | Vue 3 reactive/ref | 本步不引入 Pinia，状态局部管理 |
| CSS | Scoped CSS | 组件样式隔离 |

## 2. 模块设计

### 2.1 request.js

```
src/utils/request.js
```

- 创建 Axios 实例，baseURL: `/api`，timeout: 10000ms
- 响应拦截器：解包 Result 格式，成功返回 data，失败 reject

### 2.2 ProductList 组件

```
src/components/ProductList.vue
```

**内部状态:**

| 状态 | 类型 | 说明 |
|------|------|------|
| products | ref([]) | 商品列表 |
| loading | ref(false) | 加载中 |
| error | ref('') | 错误信息 |
| pageNum | ref(1) | 当前页 |
| pageSize | ref(8) | 每页条数 |
| total | ref(0) | 总数 |
| totalPages | computed | 总页数(向上取整) |
| keyword | ref('') | 搜索关键词 |

**分类映射:**

| 按钮文字 | keyword 值 |
|----------|-----------|
| 全部 | '' |
| 数码 | 数码 |
| 服装 | 服装 |
| 食品 | 食品 |
| 家居 | 家居 |

**方法:**

| 方法 | 说明 |
|------|------|
| fetchProducts() | 调用 GET /api/products 获取数据 |
| changePage(n) | 切换页码 |
| changeCategory(kw) | 切换分类（重置页码为1） |

### 2.3 Vite 代理配置

```js
// vite.config.js
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
    }
  }
}
```

## 3. 项目结构（新增/修改）

```
frontend/
├── vite.config.js          # 修改：添加 proxy 配置
├── package.json            # 修改：新增 axios 依赖
└── src/
    ├── App.vue             # 修改：挂载 ProductList
    ├── utils/
    │   └── request.js      # 新增：Axios 封装
    └── components/
        └── ProductList.vue # 新增：商品列表分页
```
