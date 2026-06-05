<script setup>
import { useCartStore } from './stores/cart.js'

const cart = useCartStore()
</script>

<template>
  <header class="nav-bar">
    <router-link to="/" class="nav-logo">电商购物平台</router-link>
    <nav>
      <router-link to="/">首页</router-link>
      <router-link to="/cart">
        购物车<span v-if="cart.totalCount > 0" class="badge">{{ cart.totalCount }}</span>
      </router-link>
    </nav>
  </header>
  <main>
    <router-view />
  </main>
</template>

<style scoped>
.nav-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  height: 56px;
  background: #fff;
  border-bottom: 1px solid #eee;
  font-family: 'Microsoft YaHei', sans-serif;
}
.nav-logo {
  font-size: 18px;
  font-weight: 700;
  color: #333;
  text-decoration: none;
}
nav {
  display: flex;
  gap: 24px;
}
nav a {
  color: #333;
  text-decoration: none;
  font-size: 14px;
  position: relative;
}
nav a:hover,
nav a.router-link-active {
  color: #2196f3;
}
.badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 18px;
  height: 18px;
  margin-left: 4px;
  padding: 0 5px;
  border-radius: 9px;
  background: #ff4444;
  color: #fff;
  font-size: 11px;
  vertical-align: top;
}
main {
  min-height: calc(100vh - 56px);
  background: #f8f8f8;
}
</style>
