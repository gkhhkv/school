<template>
  <div class="product-detail">
    <div class="product-image">
      <div class="image-placeholder">商品图片</div>
    </div>
    <div class="product-info">
      <h1 class="product-name">{{ product.name }}</h1>
      <div class="product-tags">
        <PromoLabel :type="product.promoType" />
      </div>
      <p class="product-desc">{{ product.description }}</p>
      <div class="product-price">¥{{ product.price }}</div>
      <div class="promo-section">
        <span class="promo-title">距活动结束</span>
        <Countdown
          :end-time="product.promoEndTime"
          @expired="onPromoExpired"
        />
      </div>
      <div class="controls">
        <label>
          促销类型：
          <select v-model="product.promoType">
            <option value="">无促销</option>
            <option value="seckill">限时秒杀</option>
            <option value="fullreduction">满减优惠</option>
            <option value="new">新品首发</option>
            <option value="hot">热销爆款</option>
          </select>
        </label>
        <label>
          结束时间（分钟）：
          <input
            type="number"
            v-model.number="endMinutes"
            min="0"
            max="120"
            style="width:60px"
          />
        </label>
        <button @click="updateEndTime">更新倒计时</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import PromoLabel from './PromoLabel.vue'
import Countdown from './Countdown.vue'

const product = reactive({
  id: 1,
  name: '华为 MateBook X Pro 2024',
  price: 8999,
  description: '14.2英寸 OLED 原色全面屏 | 酷睿 Ultra 9 | 32GB + 2TB | 艺术品级轻薄机身',
  promoType: 'seckill',
  promoEndTime: Date.now() + 3600000,
})

const endMinutes = ref(1)

function updateEndTime() {
  product.promoEndTime = Date.now() + endMinutes.value * 60000
}

function onPromoExpired() {
  console.log('倒计时结束')
}
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
.controls {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
  font-size: 14px;
}
.controls select,
.controls input {
  padding: 4px 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.controls button {
  padding: 6px 14px;
  background: #2196f3;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.controls button:hover {
  background: #1976d2;
}
</style>
