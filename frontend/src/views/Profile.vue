<template>
  <div class="profile-page">
    <div class="profile-card">
      <h2>个人资料</h2>
      <div v-if="loading" class="loading">加载中...</div>
      <template v-else-if="profile">
        <div class="info-row">
          <span class="label">用户名</span>
          <span class="value">{{ profile.username }}</span>
        </div>
        <div class="info-row">
          <span class="label">手机号</span>
          <input v-if="editing" v-model="editPhone" type="text" class="edit-input" />
          <span v-else class="value">{{ profile.phone || '未填写' }}</span>
        </div>
        <div class="info-row">
          <span class="label">收货地址</span>
          <input v-if="editing" v-model="editAddress" type="text" class="edit-input address-input" />
          <span v-else class="value">{{ profile.address || '未填写' }}</span>
        </div>
        <div class="info-row">
          <span class="label">注册时间</span>
          <span class="value">{{ formatDate(profile.createdAt) }}</span>
        </div>
        <div v-if="successMsg" class="success-msg">{{ successMsg }}</div>
        <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>
        <div class="actions">
          <template v-if="editing">
            <button class="btn-save" :disabled="saving" @click="doSave">
              {{ saving ? '保存中...' : '保存' }}
            </button>
            <button class="btn-cancel" @click="cancelEdit">取消</button>
          </template>
          <button v-else class="btn-edit" @click="startEdit">编辑</button>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request.js'

const profile = ref(null)
const loading = ref(true)
const editing = ref(false)
const editPhone = ref('')
const editAddress = ref('')
const saving = ref(false)
const successMsg = ref('')
const errorMsg = ref('')

async function loadProfile() {
  try {
    profile.value = await request.get('/auth/profile')
  } catch {
    profile.value = null
  } finally {
    loading.value = false
  }
}

function startEdit() {
  editPhone.value = profile.value.phone || ''
  editAddress.value = profile.value.address || ''
  editing.value = true
  successMsg.value = ''
  errorMsg.value = ''
}

function cancelEdit() {
  editing.value = false
  errorMsg.value = ''
}

async function doSave() {
  errorMsg.value = ''
  successMsg.value = ''
  saving.value = true
  try {
    await request.put('/auth/profile', {
      phone: editPhone.value || null,
      address: editAddress.value || null,
    })
    profile.value.phone = editPhone.value
    profile.value.address = editAddress.value
    editing.value = false
    successMsg.value = '保存成功'
  } catch (e) {
    errorMsg.value = e.message || '保存失败'
  } finally {
    saving.value = false
  }
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

onMounted(loadProfile)
</script>

<style scoped>
.profile-page {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 40px 24px;
  min-height: 60vh;
}
.profile-card {
  width: 420px;
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
.loading {
  text-align: center;
  color: #999;
  padding: 40px;
}
.info-row {
  display: flex;
  align-items: center;
  padding: 14px 0;
  border-bottom: 1px solid #f0f0f0;
  font-size: 14px;
}
.label {
  width: 80px;
  color: #999;
  flex-shrink: 0;
}
.value {
  color: #333;
}
.edit-input {
  flex: 1;
  padding: 6px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  outline: none;
}
.edit-input:focus {
  border-color: #2196f3;
}
.address-input {
  width: 100%;
}
.actions {
  margin-top: 24px;
  display: flex;
  gap: 12px;
  justify-content: center;
}
.btn-edit,
.btn-save,
.btn-cancel {
  padding: 8px 32px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  font-weight: 600;
}
.btn-edit {
  background: #2196f3;
  color: #fff;
}
.btn-edit:hover {
  background: #1976d2;
}
.btn-save {
  background: #4caf50;
  color: #fff;
}
.btn-save:hover:not(:disabled) {
  background: #388e3c;
}
.btn-save:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.btn-cancel {
  background: #f5f5f5;
  color: #666;
}
.btn-cancel:hover {
  background: #e0e0e0;
}
.success-msg {
  margin-top: 12px;
  padding: 8px 12px;
  background: #e8f5e9;
  color: #2e7d32;
  border-radius: 4px;
  font-size: 13px;
  text-align: center;
}
.error-msg {
  margin-top: 12px;
  padding: 8px 12px;
  background: #fff3f3;
  color: #d32f2f;
  border-radius: 4px;
  font-size: 13px;
  text-align: center;
}
</style>
