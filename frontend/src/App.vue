<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from './stores/cart.js'

const cart = useCartStore()
const router = useRouter()
const userInfo = ref(null)

function loadUser() {
  const raw = localStorage.getItem('userInfo')
  if (raw) {
    try { userInfo.value = JSON.parse(raw) } catch { userInfo.value = null }
  }
}

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  userInfo.value = null
  router.push('/')
}

onMounted(() => {
  loadUser()
  window.addEventListener('storage', loadUser)
})
</script>

<template>
  <header class="nav-bar">
    <router-link to="/" class="nav-logo">电商购物平台</router-link>
    <nav>
      <router-link to="/">首页</router-link>
      <router-link to="/orders">我的订单</router-link>
      <router-link to="/cart">
        购物车<span v-if="cart.totalCount > 0" class="badge">{{ cart.totalCount }}</span>
      </router-link>
      <template v-if="userInfo">
        <span class="user-name">{{ userInfo.username }}</span>
        <a href="#" class="logout-link" @click.prevent="logout">退出</a>
      </template>
      <router-link v-else to="/login">登录</router-link>
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
.user-name {
  color: #333;
  font-size: 14px;
}
.logout-link {
  color: #999 !important;
}
.logout-link:hover {
  color: #ff4444 !important;
}
main {
  min-height: calc(100vh - 56px);
  background: #f8f8f8;
}
</style>
