import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', () => {
  const userInfo = ref(loadFromStorage())

  function loadFromStorage() {
    const raw = localStorage.getItem('userInfo')
    if (raw) {
      try { return JSON.parse(raw) } catch { return null }
    }
    return null
  }

  function login(data) {
    localStorage.setItem('token', data.token)
    const info = { userId: data.userId, username: data.username }
    localStorage.setItem('userInfo', JSON.stringify(info))
    userInfo.value = info
  }

  function logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    userInfo.value = null
  }

  return { userInfo, login, logout }
})
