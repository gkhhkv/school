<template>
  <div class="order-success">
    <div class="icon">&#10003;</div>
    <h2>下单成功！</h2>
    <p>感谢您的购买，我们将尽快为您发货。</p>
    <router-link to="/" class="back-link">返回首页</router-link>
  </div>
</template>

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
  margin: 0 0 24px;
}
.back-link {
  display: inline-block;
  padding: 10px 32px;
  background: #2196f3;
  color: #fff;
  border-radius: 4px;
  text-decoration: none;
}
.back-link:hover {
  background: #1976d2;
}
</style>
