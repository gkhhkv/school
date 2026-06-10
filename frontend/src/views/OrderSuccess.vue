<template>
  <div class="order-success">
    <div class="icon">&#10003;</div>
    <h2>下单成功！</h2>
    <p v-if="orderNo">订单编号：{{ orderNo }}</p>
    <p>感谢您的购买，我们将尽快为您发货。</p>
    <div class="links">
      <router-link v-if="orderNo" :to="`/order/${orderNo}`" class="link-primary">查看订单</router-link>
      <router-link to="/orders" class="link-secondary">我的订单</router-link>
      <router-link to="/" class="link-secondary">返回首页</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const orderNo = ref('')

onMounted(() => {
  orderNo.value = route.query.orderNo || ''
})
</script>

<style scoped>
.order-success {
  text-align: center;
  padding: 80px 24px;
  font-family: 'Microsoft YaHei', sans-serif;
}
.icon {
  width: 72px;
  height: 72px;
  line-height: 72px;
  margin: 0 auto 20px;
  border-radius: 50%;
  background: #4caf50;
  color: #fff;
  font-size: 36px;
}
h2 {
  margin: 0 0 8px;
  font-size: 24px;
}
p {
  color: #666;
  margin: 0 0 8px;
}
.links {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 24px;
  flex-wrap: wrap;
}
.link-primary {
  display: inline-block;
  padding: 10px 32px;
  background: #ff4444;
  color: #fff;
  border-radius: 4px;
  text-decoration: none;
}
.link-primary:hover {
  background: #d32f2f;
}
.link-secondary {
  display: inline-block;
  padding: 10px 32px;
  background: #2196f3;
  color: #fff;
  border-radius: 4px;
  text-decoration: none;
}
.link-secondary:hover {
  background: #1976d2;
}
</style>
