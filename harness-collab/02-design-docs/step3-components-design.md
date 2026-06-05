# Step3 组件开发技术设计

> 版本: 1.0 | 日期: 2026-06-05

## 1. 技术选型

| 项目 | 选型 | 说明 |
|------|------|------|
| 前端框架 | Vue 3 (Composition API) | `<script setup>` 语法糖 |
| 构建工具 | Vite | 快速开发体验 |
| CSS | Scoped CSS | 组件样式隔离 |

## 2. 组件设计

### 2.1 PromoLabel 组件

**Props:**

| Prop | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| type | String | 否 | '' | 促销类型：seckill/fullreduction/new/hot |

**颜色映射:**

| 促销类型 | 显示文字 | 背景色 | 文字色 |
|----------|----------|--------|--------|
| seckill | 限时秒杀 | #ff4444 | #fff |
| fullreduction | 满减优惠 | #ff9800 | #fff |
| new | 新品首发 | #2196f3 | #fff |
| hot | 热销爆款 | #4caf50 | #fff |

**显示逻辑:** type 为空时组件不渲染任何内容（v-if）

### 2.2 Countdown 组件

**Props:**

| Prop | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| endTime | Number/String | 是 | — | 活动结束时间（时间戳ms或ISO字符串） |

**Emit:**

| 事件 | 参数 | 触发时机 |
|------|------|----------|
| expired | — | 倒计时归零时 |

**内部状态:**

| 状态 | 类型 | 说明 |
|------|------|------|
| days/hours/minutes/seconds | ref<number> | 剩余时间 |
| isExpired | ref<boolean> | 是否已过期 |

**生命周期:** onMounted 启动 setInterval (1000ms)，onUnmounted 执行 clearInterval

### 2.3 ProductDetail 父组件

**本地数据（模拟）:**

```js
const product = reactive({
  id: 1,
  name: '华为 MateBook X Pro',
  price: 8999,
  image: '',
  description: '超轻薄高性能笔记本',
  promoType: 'seckill',
  promoEndTime: Date.now() + 3600000, // 1小时后
})
```

## 3. 项目结构

```
frontend/
├── index.html
├── package.json
├── vite.config.js
└── src/
    ├── main.js
    ├── App.vue
    └── components/
        ├── PromoLabel.vue
        ├── Countdown.vue
        └── ProductDetail.vue
```
