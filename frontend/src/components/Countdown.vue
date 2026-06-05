<template>
  <div class="countdown">
    <template v-if="isExpired">
      <span class="expired">活动已结束</span>
    </template>
    <template v-else>
      <span class="time-block">{{ padZero(days) }}</span><span class="sep">天</span>
      <span class="time-block">{{ padZero(hours) }}</span><span class="sep">:</span>
      <span class="time-block">{{ padZero(minutes) }}</span><span class="sep">:</span>
      <span class="time-block">{{ padZero(seconds) }}</span>
    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  endTime: {
    type: [Number, String],
    required: true,
  },
})

const emit = defineEmits(['expired'])

const now = ref(Date.now())
let timer = null

const endTimestamp = computed(() => {
  const t = typeof props.endTime === 'string' ? new Date(props.endTime).getTime() : props.endTime
  return Number(t)
})

const diff = computed(() => Math.max(0, endTimestamp.value - now.value))

const days = computed(() => Math.floor(diff.value / 86400000))
const hours = computed(() => Math.floor((diff.value % 86400000) / 3600000))
const minutes = computed(() => Math.floor((diff.value % 3600000) / 60000))
const seconds = computed(() => Math.floor((diff.value % 60000) / 1000))

const isExpired = computed(() => diff.value <= 0)

function padZero(n) {
  return String(n).padStart(2, '0')
}

function tick() {
  now.value = Date.now()
  if (diff.value <= 0) {
    clearInterval(timer)
    timer = null
    emit('expired')
  }
}

onMounted(() => {
  tick()
  if (!isExpired.value) {
    timer = setInterval(tick, 1000)
  }
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
})
</script>

<style scoped>
.countdown {
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 2px;
}
.time-block {
  background: #333;
  color: #fff;
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-weight: 700;
  min-width: 28px;
  text-align: center;
}
.sep {
  color: #333;
  font-weight: 600;
}
.expired {
  color: #999;
  font-size: 14px;
}
</style>
