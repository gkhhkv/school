<template>
  <div class="product-detail">
    <template v-if="loading">加载中...</template>
    <template v-else-if="error" class="error">{{ error }}</template>
    <template v-else-if="product">
      <div class="product-image">
        <img v-if="product.image" :src="product.image" :alt="product.name" />
        <div v-else class="image-placeholder">暂无图片</div>
      </div>
      <div class="product-info">
        <h1 class="product-name">{{ product.name }}</h1>
        <div class="product-tags">
          <PromoLabel :type="product.promoType || ''" />
        </div>
        <p class="product-desc">{{ product.description || '暂无描述' }}</p>
        <div class="product-price">¥{{ product.price }}</div>
        <div class="stock-info">
          库存：<span :class="{ 'low-stock': product.stock < 10 }">{{ product.stock || 0 }}</span> 件
        </div>
        <div v-if="product.promoEndTime" class="promo-section">
          <span class="promo-title">距活动结束</span>
          <Countdown
            :end-time="product.promoEndTime"
            @expired="onPromoExpired"
          />
        </div>
        <button
          class="add-cart-btn"
          :disabled="!product.stock"
          @click="handleAddCart"
        >{{ product.stock ? '加入购物车' : '已售罄' }}</button>
        <div v-if="cartMsg" :class="cartMsg.ok ? 'cart-msg-ok' : 'cart-msg-err'">{{ cartMsg.text }}</div>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import PromoLabel from './PromoLabel.vue'
import Countdown from './Countdown.vue'
import request from '../utils/request.js'
import { useCartStore } from '../stores/cart.js'

const props = defineProps({
  id: {
    type: String,
    required: true,
  },
})

const cart = useCartStore()

const product = ref(null)
const loading = ref(false)
const error = ref('')

async function fetchProduct() {
  loading.value = true
  error.value = ''
  try {
    product.value = await request.get(`/products/${props.id}`)
  } catch (e) {
    error.value = e.message || '加载失败'
  } finally {
    loading.value = false
  }
}

const cartMsg = ref(null)

async function handleAddCart() {
  const result = await cart.addToCart(product.value)
  cartMsg.value = result.ok
    ? { ok: true, text: '已加入购物车' }
    : { ok: false, text: result.msg }
  setTimeout(() => { cartMsg.value = null }, 2000)
}

function onPromoExpired() {
  console.log('倒计时结束')
}

onMounted(() => {
  fetchProduct()
})
</script>

<style scoped>
.product-detail {
  max-width: 800px;
  margin: 40px auto;
  padding: 24px;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  display: flex;
  gap: 24px;
  font-family: 'Microsoft YaHei', sans-serif;
  background: #fff;
}
.product-image {
  flex-shrink: 0;
}
.product-image img {
  width: 240px;
  height: 240px;
  object-fit: contain;
  border-radius: 8px;
  background: #f5f5f5;
}
.image-placeholder {
  width: 240px;
  height: 240px;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #bbb;
  border-radius: 8px;
  font-size: 16px;
}
.product-info {
  flex: 1;
}
.product-name {
  margin: 0 0 8px;
  font-size: 22px;
}
.product-tags {
  margin-bottom: 8px;
}
.product-desc {
  color: #666;
  margin: 0 0 12px;
  font-size: 14px;
}
.product-price {
  font-size: 28px;
  color: #ff4444;
  font-weight: 700;
  margin-bottom: 16px;
}
.promo-section {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  padding: 10px 14px;
  background: #fafafa;
  border-radius: 8px;
}
.promo-title {
  font-size: 14px;
  color: #666;
}
.stock-info {
  font-size: 14px;
  color: #666;
  margin-bottom: 16px;
}
.low-stock {
  color: #ff9800;
  font-weight: 600;
}
.add-cart-btn {
  padding: 10px 32px;
  background: #ff4444;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
}
.add-cart-btn:hover:not(:disabled) {
  background: #d32f2f;
}
.add-cart-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}
.cart-msg-ok {
  margin-top: 10px;
  color: #4caf50;
  font-size: 13px;
}
.cart-msg-err {
  margin-top: 10px;
  color: #ff4444;
  font-size: 13px;
}
</style>
