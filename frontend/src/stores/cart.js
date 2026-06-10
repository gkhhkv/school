import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import request from '../utils/request.js'

function toStoreItem(apiItem) {
  return {
    product: {
      productId: apiItem.productId,
      name: apiItem.productName,
      price: apiItem.price,
      image: apiItem.image,
      stock: apiItem.stock,
    },
    quantity: apiItem.quantity,
  }
}

function toSyncItem(storeItem) {
  return {
    productId: storeItem.product.productId,
    quantity: storeItem.quantity,
  }
}

const LS_KEY = 'cart_items'

function loadLocalCart() {
  try {
    const raw = localStorage.getItem(LS_KEY)
    return raw ? JSON.parse(raw) : []
  } catch {
    return []
  }
}

function saveLocalCart(items) {
  localStorage.setItem(LS_KEY, JSON.stringify(items))
}

export const useCartStore = defineStore('cart', () => {
  const items = ref([])
  const loaded = ref(false)

  async function loadCart() {
    const token = localStorage.getItem('token')
    if (token) {
      try {
        const apiItems = await request.get('/cart')
        items.value = apiItems.map(toStoreItem)
      } catch {
        items.value = loadLocalCart()
      }
    } else {
      items.value = loadLocalCart()
    }
    loaded.value = true
  }

  async function syncToServer() {
    const token = localStorage.getItem('token')
    if (token) {
      try {
        const syncItems = items.value.map(toSyncItem)
        await request.put('/cart/sync', { items: syncItems })
      } catch {
        // 静默失败，本地数据仍在
      }
    } else {
      saveLocalCart(items.value)
    }
  }

  async function addToCart(product) {
    const existing = items.value.find((item) => item.product.productId === product.productId)
    if (existing) {
      existing.quantity++
    } else {
      items.value.push({ product: { ...product }, quantity: 1 })
    }
    await syncToServer()
  }

  async function removeFromCart(productId) {
    items.value = items.value.filter((item) => item.product.productId !== productId)
    await syncToServer()
  }

  async function updateQuantity(productId, quantity) {
    if (quantity < 1) return
    const item = items.value.find((i) => i.product.productId === productId)
    if (item) {
      item.quantity = quantity
      await syncToServer()
    }
  }

  async function clearCart() {
    items.value = []
    const token = localStorage.getItem('token')
    if (token) {
      try {
        await request.delete('/cart')
      } catch { /* ignore */ }
    } else {
      saveLocalCart(items.value)
    }
  }

  const totalCount = computed(() => items.value.reduce((sum, i) => sum + i.quantity, 0))
  const totalPrice = computed(() =>
    items.value.reduce((sum, i) => sum + Number(i.product.price) * i.quantity, 0),
  )

  return { items, loaded, loadCart, addToCart, removeFromCart, updateQuantity, clearCart, totalCount, totalPrice }
})
