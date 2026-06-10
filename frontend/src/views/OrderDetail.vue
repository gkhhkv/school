<template>
  <div class="order-detail-page">
    <div v-if="loading" class="status-msg">加载中...</div>
    <div v-else-if="error" class="status-msg error">{{ error }}</div>
    <template v-else-if="order">
      <h2>订单详情</h2>
      <div class="info-card">
        <div class="info-row"><span class="label">订单编号</span><span>{{ order.orderNo }}</span></div>
        <div class="info-row"><span class="label">订单状态</span><span class="status">{{ statusLabel(order.status) }}</span></div>
        <div class="info-row"><span class="label">总金额</span><span class="amount">¥{{ order.totalAmount }}</span></div>
        <div class="info-row"><span class="label">下单时间</span><span>{{ formatTime(order.createdAt) }}</span></div>
        <div class="info-row"><span class="label">收货人</span><span>{{ order.contactName }}</span></div>
        <div class="info-row"><span class="label">手机号</span><span>{{ order.contactPhone }}</span></div>
        <div class="info-row"><span class="label">收货地址</span><span>{{ order.shippingAddress }}</span></div>
        <div v-if="order.paidAt" class="info-row"><span class="label">支付时间</span><span>{{ formatTime(order.paidAt) }}</span></div>
        <div v-if="order.shippedAt" class="info-row"><span class="label">发货时间</span><span>{{ formatTime(order.shippedAt) }}</span></div>
      </div>

      <h3 style="margin-top: 24px;">商品明细</h3>
      <div class="items-table">
        <div class="items-header">
          <span>商品名称</span>
          <span>单价</span>
          <span>数量</span>
          <span>小计</span>
        </div>
        <div v-for="item in order.items" :key="item.itemId" class="item-row">
          <span>{{ item.productName || `商品 #${item.productId}` }}</span>
          <span>¥{{ item.unitPrice }}</span>
          <span>{{ item.quantity }}</span>
          <span class="subtotal">¥{{ item.subtotal }}</span>
        </div>
      </div>

      <div class="actions">
        <router-link to="/orders" class="back-link">&larr; 返回订单列表</router-link>
        <button
          v-if="order.status === 'PENDING_PAYMENT'"
          class="btn-cancel"
          :disabled="cancelling"
          @click="doCancel"
        >
          {{ cancelling ? '取消中...' : '取消订单' }}
        </button>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../utils/request.js'

const route = useRoute()
const router = useRouter()

const statusMap = {
  PENDING_PAYMENT: '待支付',
  PAID: '已支付',
  SHIPPED: '已发货',
  COMPLETED: '已完成',
  CANCELLED: '已取消',
}
function statusLabel(s) { return statusMap[s] || s }
function formatTime(t) { return t ? new Date(t).toLocaleString('zh-CN') : '-' }

const order = ref(null)
const loading = ref(false)
const error = ref('')
const cancelling = ref(false)

async function fetchOrder() {
  loading.value = true
  error.value = ''
  try {
    order.value = await request.get(`/orders/${route.params.orderNo}`)
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

async function doCancel() {
  cancelling.value = true
  try {
    await request.put(`/orders/${order.value.orderNo}/cancel`)
    fetchOrder()
  } catch (e) {
    error.value = e.message
  } finally {
    cancelling.value = false
  }
}

onMounted(() => {
  fetchOrder()
})
</script>

<style scoped>
.order-detail-page {
  max-width: 720px;
  margin: 0 auto;
  padding: 24px;
  font-family: 'Microsoft YaHei', sans-serif;
}
h2 { margin: 0 0 20px; font-size: 22px; }
h3 { margin: 0 0 12px; font-size: 16px; }
.status-msg {
  text-align: center;
  padding: 60px 0;
  color: #999;
  font-size: 16px;
}
.status-msg.error { color: #f44336; }
.info-card {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 16px 20px;
}
.info-row {
  display: flex;
  padding: 8px 0;
  border-bottom: 1px solid #f5f5f5;
  font-size: 14px;
}
.info-row:last-child { border-bottom: none; }
.label {
  width: 80px;
  color: #999;
  flex-shrink: 0;
}
.amount { color: #ff4444; font-weight: 700; }
.status { color: #2196f3; font-weight: 600; }
.items-table { border-top: 2px solid #333; }
.items-header {
  display: flex;
  padding: 10px 0;
  font-size: 13px;
  color: #999;
  border-bottom: 1px solid #eee;
}
.items-header span { flex: 1; text-align: center; }
.items-header span:first-child { flex: 2; text-align: left; }
.item-row {
  display: flex;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
  font-size: 14px;
  align-items: center;
}
.item-row span { flex: 1; text-align: center; }
.item-row span:first-child { flex: 2; text-align: left; }
.subtotal { color: #ff4444; font-weight: 600; }
.actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 24px;
}
.back-link {
  color: #2196f3;
  text-decoration: none;
  font-size: 14px;
}
.back-link:hover { text-decoration: underline; }
.btn-cancel {
  padding: 8px 24px;
  border: 1px solid #ff4444;
  border-radius: 4px;
  background: #fff;
  color: #ff4444;
  cursor: pointer;
  font-size: 14px;
}
.btn-cancel:hover:not(:disabled) {
  background: #ff4444;
  color: #fff;
}
.btn-cancel:disabled { opacity: 0.6; cursor: not-allowed; }
</style>
