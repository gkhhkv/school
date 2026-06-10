<template>
  <div class="register-page">
    <div class="register-card">
      <h2>用户注册</h2>
      <form @submit.prevent="doRegister">
        <label>
          用户名
          <input v-model="username" type="text" placeholder="2-50位字符" autocomplete="username" />
        </label>
        <label>
          密码
          <input v-model="password" type="password" placeholder="4位及以上" autocomplete="new-password" />
        </label>
        <label>
          手机号
          <input v-model="phone" type="text" placeholder="选填" autocomplete="tel" />
        </label>
        <label>
          收货地址
          <input v-model="address" type="text" placeholder="选填" autocomplete="street-address" />
        </label>
        <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>
        <button type="submit" :disabled="submitting" class="btn-register">
          {{ submitting ? '注册中...' : '注册' }}
        </button>
      </form>
      <p class="hint">
        已有账号？
        <router-link to="/login">去登录</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request.js'
import { useAuthStore } from '../stores/auth.js'

const router = useRouter()
const auth = useAuthStore()

const username = ref('')
const password = ref('')
const phone = ref('')
const address = ref('')
const submitting = ref(false)
const errorMsg = ref('')

async function doRegister() {
  if (!username.value || !password.value) {
    errorMsg.value = '请填写用户名和密码'
    return
  }
  if (username.value.length < 2 || username.value.length > 50) {
    errorMsg.value = '用户名长度需在2-50位之间'
    return
  }
  if (password.value.length < 4) {
    errorMsg.value = '密码长度不能少于4位'
    return
  }
  errorMsg.value = ''
  submitting.value = true
  try {
    const data = await request.post('/auth/register', {
      username: username.value,
      password: password.value,
      phone: phone.value || undefined,
      address: address.value || undefined,
    })
    auth.login(data)
    router.push('/')
  } catch (e) {
    errorMsg.value = e.message || '注册失败'
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
}
.register-card {
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
.btn-register {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 6px;
  background: #4caf50;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
}
.btn-register:hover:not(:disabled) {
  background: #388e3c;
}
.btn-register:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.hint {
  margin: 16px 0 0;
  text-align: center;
  color: #aaa;
  font-size: 12px;
}
.hint a {
  color: #2196f3;
  text-decoration: none;
}
.hint a:hover {
  text-decoration: underline;
}
</style>
