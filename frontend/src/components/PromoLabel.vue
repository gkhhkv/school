<template>
  <span v-if="type" class="promo-label" :style="labelStyle">{{ labelText }}</span>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  type: {
    type: String,
    default: '',
  },
})

const labelMap = {
  seckill: { text: '限时秒杀', bg: '#ff4444', color: '#fff' },
  fullreduction: { text: '满减优惠', bg: '#ff9800', color: '#fff' },
  new: { text: '新品首发', bg: '#2196f3', color: '#fff' },
  hot: { text: '热销爆款', bg: '#4caf50', color: '#fff' },
}

const labelText = computed(() => labelMap[props.type]?.text || '')

const labelStyle = computed(() => {
  const cfg = labelMap[props.type]
  if (!cfg) return {}
  return {
    backgroundColor: cfg.bg,
    color: cfg.color,
  }
})
</script>

<style scoped>
.promo-label {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
  line-height: 1.5;
}
</style>
