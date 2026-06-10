<template>
  <div class="order-list-page">
    <h2>我的订单</h2>

    <div class="status-bar">
      <button
        v-for="s in statusTabs"
        :key="s.key"
        :class="['status-btn', { active: filterStatus === s.key }]"
        @click="changeStatus(s.key)"
      >
        {{ s.label }}
      </button>
    </div>

    <div v-if="loading" class="status-msg">加载中...</div>
    <div v-else-if="error" class="status-msg error">{{ error }}</div>
    <template v-else>
      <div v-if="orders.length === 0" class="status-msg">暂无订单</div>
      <div v-else class="order-table">
        <div class="table-header">
          <span>订单编号</span>
          <span>金额</span>
          <span>状态</span>
          <span>时间</span>
          <span>操作</span>
        </div>
        <div v-for="o in orders" :key="o.orderNo" class="order-row">
          <span class="order-no">
            <router-link :to="`/order/${o.orderNo}`">{{ o.orderNo }}</router-link>
          </span>
          <span class="order-amount">¥{{ o.totalAmount }}</span>
          <span class="order-status">{{ statusLabel(o.status) }}</span>
          <span class="order-time">{{ formatTime(o.createdAt) }}</span>
          <span class="order-actions">
            <button
              v-if="o.status === 'PENDING_PAYMENT'"
              class="btn-cancel"
              @click="cancelOrder(o.orderNo)"
              :disabled="cancelling === o.orderNo"
            >
              {{ cancelling === o.orderNo ? '取消中...' : '取消' }}
            </button>
            <span v-else class="no-action">-</span>
          </span>
        </div>
      </div>

      <div class="pagination" v-if="totalPages > 0">
        <button :disabled="pageNum <= 1" @click="changePage(pageNum - 1)">上一页</button>
        <span>{{ pageNum }} / {{ totalPages }}</span>
        <button :disabled="pageNum >= totalPages" @click="changePage(pageNum + 1)">下一页</button>
        <span class="total-info">共 {{ total }} 单</span>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import request from '../utils/request.js'

const statusTabs = [
  { key: '', label: '全部' },
  { key: 'PENDING_PAYMENT', label: '待支付' },
  { key: 'PAID', label: '已支付' },
  { key: 'SHIPPED', label: '已发货' },
  { key: 'COMPLETED', label: '已完成' },
  { key: 'CANCELLED', label: '已取消' },
]

const statusMap = {
  PENDING_PAYMENT: '待支付',
  PAID: '已支付',
  SHIPPED: '已发货',
  COMPLETED: '已完成',
  CANCELLED: '已取消',
}

function statusLabel(s) {
  return statusMap[s] || s
}

function formatTime(t) {
  if (!t) return '-'
  return new Date(t).toLocaleString('zh-CN')
}

const orders = ref([])
const loading = ref(false)
const error = ref('')
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const filterStatus = ref('')
const cancelling = ref(null)

const totalPages = computed(() => Math.max(1, Math.ceil(total.value / pageSize.value)))

async function fetchOrders() {
  loading.value = true
  error.value = ''
  try {
    const params = { pageNum: pageNum.value, pageSize: pageSize.value }
    if (filterStatus.value) params.status = filterStatus.value
    const res = await request.get('/orders', { params })
    orders.value = res.records || []
    total.value = res.total || 0
  } catch (e) {
    error.value = e.message
    orders.value = []
  } finally {
    loading.value = false
  }
}

function changePage(n) {
  if (n < 1 || n > totalPages.value) return
  pageNum.value = n
  fetchOrders()
}

function changeStatus(s) {
  filterStatus.value = s
  pageNum.value = 1
  fetchOrders()
}

async function cancelOrder(orderNo) {
  cancelling.value = orderNo
  try {
    await request.put(`/orders/${orderNo}/cancel`)
    fetchOrders()
  } catch (e) {
    error.value = e.message
  } finally {
    cancelling.value = null
  }
}

onMounted(() => {
  fetchOrders()
})
</script>

<style scoped>
.order-list-page {
  max-width: 960px;
  margin: 0 auto;
  padding: 24px;
  font-family: 'Microsoft YaHei', sans-serif;
}
h2 {
  margin: 0 0 16px;
  font-size: 22px;
}
.status-bar {
  display: flex;
  gap: 8px;
  margin-bottom: 20px;
}
.status-btn {
  padding: 6px 16px;
  border: 1px solid #ddd;
  border-radius: 20px;
  background: #fff;
  cursor: pointer;
  font-size: 13px;
  transition: all 0.2s;
}
.status-btn.active {
  background: #2196f3;
  color: #fff;
  border-color: #2196f3;
}
.status-btn:hover:not(.active) {
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
.order-table {
  border-top: 2px solid #333;
}
.table-header {
  display: flex;
  padding: 12px 0;
  font-size: 13px;
  color: #999;
  border-bottom: 1px solid #eee;
}
.table-header span {
  flex: 1;
  text-align: center;
}
.table-header span:first-child {
  flex: 2;
}
.table-header span:last-child {
  flex: 0 0 100px;
}
.order-row {
  display: flex;
  align-items: center;
  padding: 14px 0;
  border-bottom: 1px solid #f0f0f0;
  font-size: 14px;
}
.order-row span {
  flex: 1;
  text-align: center;
}
.order-no {
  flex: 2 !important;
  text-align: left !important;
}
.order-no a {
  color: #2196f3;
  text-decoration: none;
}
.order-no a:hover {
  text-decoration: underline;
}
.order-amount {
  color: #ff4444;
  font-weight: 600;
}
.order-status {
  color: #666;
}
.order-time {
  color: #999;
  font-size: 13px;
}
.order-actions {
  flex: 0 0 100px !important;
}
.btn-cancel {
  padding: 4px 12px;
  border: 1px solid #ff4444;
  border-radius: 4px;
  background: #fff;
  color: #ff4444;
  cursor: pointer;
  font-size: 12px;
}
.btn-cancel:hover:not(:disabled) {
  background: #ff4444;
  color: #fff;
}
.btn-cancel:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.no-action {
  color: #ccc;
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
