<template>
  <div class="login-page">
    <div class="login-card">
      <h2>用户登录</h2>
      <form @submit.prevent="doLogin">
        <label>
          用户名
          <input v-model="username" type="text" placeholder="请输入用户名" autocomplete="username" />
        </label>
        <label>
          密码
          <input v-model="password" type="password" placeholder="请输入密码" autocomplete="current-password" />
        </label>
        <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>
        <button type="submit" :disabled="submitting" class="btn-login">
          {{ submitting ? '登录中...' : '登录' }}
        </button>
      </form>
      <p class="hint">测试账号：testuser / password</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import request from '../utils/request.js'

const username = ref('')
const password = ref('')
const submitting = ref(false)
const errorMsg = ref('')

async function doLogin() {
  if (!username.value || !password.value) {
    errorMsg.value = '请输入用户名和密码'
    return
  }
  errorMsg.value = ''
  submitting.value = true
  try {
    const data = await request.post('/auth/login', {
      username: username.value,
      password: password.value,
    })
    localStorage.setItem('token', data.token)
    localStorage.setItem('userInfo', JSON.stringify({ userId: data.userId, username: data.username }))
    window.location.href = '/#/'
  } catch (e) {
    errorMsg.value = e.message || '登录失败'
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
}
.login-card {
  width: 360px;
  padding: 32px;
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  font-family: 'Microsoft YaHei', sans-serif;
}
h2 {
  margin: 0 0 24px;
  text-align: center;
  font-size: 20px;
}
label {
  display: block;
  margin-bottom: 16px;
  font-size: 14px;
  color: #666;
}
label input {
  display: block;
  width: 100%;
  margin-top: 6px;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  box-sizing: border-box;
  outline: none;
}
label input:focus {
  border-color: #2196f3;
}
.error-msg {
  padding: 8px 12px;
  margin-bottom: 12px;
  background: #fff3f3;
  color: #d32f2f;
  border-radius: 4px;
  font-size: 13px;
}
.btn-login {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 6px;
  background: #2196f3;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
}
.btn-login:hover:not(:disabled) {
  background: #1976d2;
}
.btn-login:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.hint {
  margin: 16px 0 0;
  text-align: center;
  color: #aaa;
  font-size: 12px;
}
</style>
