import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useCartStore = defineStore('cart', () => {
  const items = ref([])

  function addToCart(product) {
    const existing = items.value.find((item) => item.product.productId === product.productId)
    if (existing) {
      existing.quantity++
    } else {
      items.value.push({ product: { ...product }, quantity: 1 })
    }
  }

  function removeFromCart(productId) {
    items.value = items.value.filter((item) => item.product.productId !== productId)
  }

  function updateQuantity(productId, quantity) {
    if (quantity < 1) return
    const item = items.value.find((i) => i.product.productId === productId)
    if (item) item.quantity = quantity
  }

  function clearCart() {
    items.value = []
  }

  const totalCount = computed(() => items.value.reduce((sum, i) => sum + i.quantity, 0))
  const totalPrice = computed(() =>
    items.value.reduce((sum, i) => sum + Number(i.product.price) * i.quantity, 0),
  )

  return { items, addToCart, removeFromCart, updateQuantity, clearCart, totalCount, totalPrice }
})
