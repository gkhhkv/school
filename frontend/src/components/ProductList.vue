<template>
  <div class="product-list-page">
    <h2 class="page-title">商品列表</h2>

    <div class="category-bar">
      <button
        v-for="cat in categories"
        :key="cat.key"
        :class="['cat-btn', { active: keyword === cat.key }]"
        @click="changeCategory(cat.key)"
      >
        {{ cat.label }}
      </button>
    </div>

    <div v-if="loading" class="status-msg">加载中...</div>
    <div v-else-if="error" class="status-msg error">{{ error }}</div>
    <template v-else>
      <div v-if="products.length === 0" class="status-msg">暂无商品</div>
      <div v-else class="product-grid">
        <div v-for="p in products" :key="p.productId" class="product-card">
          <div class="card-img">
            <img v-if="p.image" :src="p.image" :alt="p.name" />
            <div v-else class="img-placeholder">暂无图片</div>
          </div>
          <div class="card-body">
            <span class="card-name">{{ p.name }}</span>
            <span class="card-price">¥{{ p.price }}</span>
          </div>
        </div>
      </div>

      <div class="pagination" v-if="totalPages > 0">
        <button :disabled="pageNum <= 1" @click="changePage(pageNum - 1)">上一页</button>
        <span>{{ pageNum }} / {{ totalPages }}</span>
        <button :disabled="pageNum >= totalPages" @click="changePage(pageNum + 1)">下一页</button>
        <span class="total-info">共 {{ total }} 件</span>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import request from '../utils/request.js'

const categories = [
  { key: '', label: '全部' },
  { key: '数码', label: '数码' },
  { key: '服装', label: '服装' },
  { key: '食品', label: '食品' },
  { key: '家居', label: '家居' },
]

const products = ref([])
const loading = ref(false)
const error = ref('')
const pageNum = ref(1)
const pageSize = ref(8)
const total = ref(0)
const keyword = ref('')

const totalPages = computed(() => Math.max(1, Math.ceil(total.value / pageSize.value)))

async function fetchProducts() {
  loading.value = true
  error.value = ''
  try {
    const params = { pageNum: pageNum.value, pageSize: pageSize.value }
    if (keyword.value) params.keyword = keyword.value
    const res = await request.get('/products', { params })
    products.value = res.records || []
    total.value = res.total || 0
  } catch (e) {
    error.value = e.message
    products.value = []
  } finally {
    loading.value = false
  }
}

function changePage(n) {
  if (n < 1 || n > totalPages.value) return
  pageNum.value = n
  fetchProducts()
}

function changeCategory(kw) {
  keyword.value = kw
  pageNum.value = 1
  fetchProducts()
}

onMounted(() => {
  fetchProducts()
})
</script>

<style scoped>
.product-list-page {
  max-width: 960px;
  margin: 0 auto;
  padding: 24px;
  font-family: 'Microsoft YaHei', sans-serif;
}
.page-title {
  margin: 0 0 16px;
  font-size: 22px;
}
.category-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}
.cat-btn {
  padding: 6px 18px;
  border: 1px solid #ddd;
  border-radius: 20px;
  background: #fff;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}
.cat-btn.active {
  background: #2196f3;
  color: #fff;
  border-color: #2196f3;
}
.cat-btn:hover:not(.active) {
  border-color: #2196f3;
  color: #2196f3;
}
.status-msg {
  text-align: center;
  padding: 60px 0;
  color: #999;
  font-size: 16px;
}
.status-msg.error {
  color: #f44336;
}
.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}
.product-card {
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
  transition: box-shadow 0.2s;
}
.product-card:hover {
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
}
.card-img {
  width: 100%;
  height: 180px;
  background: #fafafa;
  display: flex;
  align-items: center;
  justify-content: center;
}
.card-img img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}
.img-placeholder {
  color: #ccc;
  font-size: 14px;
}
.card-body {
  padding: 10px 12px;
}
.card-name {
  display: block;
  font-size: 14px;
  color: #333;
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.card-price {
  font-size: 18px;
  color: #ff4444;
  font-weight: 700;
}
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  margin-top: 24px;
  font-size: 14px;
}
.pagination button {
  padding: 6px 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: #fff;
  cursor: pointer;
}
.pagination button:disabled {
  color: #ccc;
  cursor: not-allowed;
}
.pagination button:hover:not(:disabled) {
  border-color: #2196f3;
  color: #2196f3;
}
.total-info {
  color: #999;
}
</style>
