<template>
  <div class="not-found">
    <h1>404</h1>
    <p>页面不存在</p>
    <router-link to="/" class="back-link">返回首页</router-link>
  </div>
</template>

<style scoped>
.not-found {
  text-align: center;
  padding: 100px 24px;
  font-family: 'Microsoft YaHei', sans-serif;
}
h1 {
  font-size: 64px;
  color: #ddd;
  margin: 0;
}
p {
  color: #999;
  margin: 12px 0 24px;
  font-size: 18px;
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
