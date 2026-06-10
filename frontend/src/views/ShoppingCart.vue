<template>
  <div class="cart-page">
    <h2>购物车</h2>

    <div v-if="cart.items.length === 0" class="empty">购物车是空的，去首页选购吧</div>
    <template v-else>
      <div class="cart-list">
        <div v-for="item in cart.items" :key="item.product.productId" class="cart-item">
          <span class="item-name">{{ item.product.name }}</span>
          <span class="item-price">¥{{ item.product.price }}</span>
          <div class="item-qty">
            <button @click="cart.updateQuantity(item.product.productId, item.quantity - 1)">-</button>
            <span>{{ item.quantity }}</span>
            <button @click="cart.updateQuantity(item.product.productId, item.quantity + 1)">+</button>
          </div>
          <span class="item-subtotal">¥{{ (Number(item.product.price) * item.quantity).toFixed(2) }}</span>
          <button class="btn-del" @click="cart.removeFromCart(item.product.productId)">删除</button>
        </div>
      </div>

      <div class="shipping-form">
        <h3>收货信息</h3>
        <label>
          收货人：
          <input v-model="contactName" type="text" placeholder="请输入姓名" />
        </label>
        <label>
          手机号：
          <input v-model="contactPhone" type="text" placeholder="请输入手机号" />
        </label>
        <label>
          地址：
          <input v-model="shippingAddress" type="text" placeholder="请输入地址" />
        </label>
      </div>

      <div class="cart-footer">
        <span>共 {{ cart.totalCount }} 件 | 合计 ¥{{ cart.totalPrice.toFixed(2) }}</span>
        <div class="footer-actions">
          <button class="btn-clear" @click="cart.clearCart()">清空购物车</button>
          <button class="btn-submit" :disabled="submitting" @click="submitOrder">
            {{ submitting ? '提交中...' : '提交订单' }}
          </button>
        </div>
      </div>
      <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>
    </template>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '../stores/cart.js'
import request from '../utils/request.js'

const cart = useCartStore()
const router = useRouter()

const contactName = ref('')
const contactPhone = ref('')
const shippingAddress = ref('')
const submitting = ref(false)
const errorMsg = ref('')

async function submitOrder() {
  if (!contactName.value || !contactPhone.value || !shippingAddress.value) {
    errorMsg.value = '请填写完整的收货信息'
    return
  }
  errorMsg.value = ''
  submitting.value = true
  try {
    const items = cart.items.map((i) => ({
      productId: i.product.productId,
      quantity: i.quantity,
    }))
    const order = await request.post('/orders', {
      shippingAddress: shippingAddress.value,
      contactName: contactName.value,
      contactPhone: contactPhone.value,
      items,
    })
    cart.clearCart()
    router.push({ path: '/order-success', query: { orderNo: order.orderNo } })
  } catch (e) {
    errorMsg.value = e.message || '提交订单失败'
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.cart-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 24px;
  font-family: 'Microsoft YaHei', sans-serif;
}
h2 {
  margin: 0 0 20px;
}
.empty {
  text-align: center;
  padding: 80px 0;
  color: #999;
  font-size: 16px;
}
.shipping-form {
  margin-top: 20px;
  padding: 16px;
  background: #fafafa;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  font-size: 14px;
}
.shipping-form h3 {
  margin: 0;
  font-size: 16px;
}
.shipping-form label {
  display: flex;
  align-items: center;
  gap: 8px;
}
.shipping-form input {
  flex: 1;
  padding: 6px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}
.error-msg {
  margin-top: 12px;
  padding: 10px 14px;
  background: #fff3f3;
  color: #d32f2f;
  border-radius: 4px;
  font-size: 14px;
}
.cart-list {
  border-top: 2px solid #333;
}
.cart-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 14px 0;
  border-bottom: 1px solid #eee;
  font-size: 14px;
}
.item-name {
  flex: 1;
}
.item-price {
  color: #999;
  width: 80px;
  text-align: right;
}
.item-qty {
  display: flex;
  align-items: center;
  gap: 8px;
}
.item-qty button {
  width: 28px;
  height: 28px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: #fff;
  cursor: pointer;
  font-size: 16px;
}
.item-qty button:hover {
  border-color: #2196f3;
  color: #2196f3;
}
.item-subtotal {
  width: 90px;
  text-align: right;
  color: #ff4444;
  font-weight: 600;
}
.btn-del {
  padding: 4px 10px;
  border: 1px solid #ff4444;
  border-radius: 4px;
  background: #fff;
  color: #ff4444;
  cursor: pointer;
  font-size: 12px;
}
.btn-del:hover {
  background: #ff4444;
  color: #fff;
}
.cart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  margin-top: 16px;
  font-size: 16px;
  font-weight: 600;
}
.footer-actions {
  display: flex;
  gap: 12px;
}
.btn-clear {
  padding: 8px 20px;
  border: 1px solid #999;
  border-radius: 4px;
  background: #fff;
  color: #666;
  cursor: pointer;
}
.btn-submit {
  padding: 8px 24px;
  border: none;
  border-radius: 4px;
  background: #ff4444;
  color: #fff;
  cursor: pointer;
  font-weight: 600;
}
.btn-submit:hover:not(:disabled) {
  background: #d32f2f;
}
.btn-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
