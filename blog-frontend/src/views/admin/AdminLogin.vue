<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../../stores/auth'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()

const username = ref('admin')
const password = ref('')
const error = ref('')
const loading = ref(false)

async function login() {
  error.value = ''
  if (!password.value) { error.value = '请输入密码'; return }
  loading.value = true
  const ok = await auth.login(username.value, password.value)
  loading.value = false
  if (ok) {
    router.push((route.query.redirect as string) || '/admin')
  } else {
    error.value = '密码错误，请重试'
  }
}
</script>

<template>
  <div class="min-h-[80vh] flex items-center justify-center px-4">
    <div class="w-full max-w-sm">
      <div class="text-center mb-8">
        <div class="text-6xl mb-4">📷</div>
        <h1 class="text-2xl font-black" style="font-family: var(--font-display); color: var(--accent-pink)">
          摄影笔记
        </h1>
        <p class="text-sm mt-2" style="color: var(--text-secondary)">后台管理 — 请输入密码进入</p>
      </div>
      <form @submit.prevent="login" class="space-y-4"
            style="background-color: var(--bg-card); padding: 2rem; border-radius: 1rem; border: 1px solid var(--border-color)">
        <input v-model="username" type="text" placeholder="用户名"
               class="w-full px-3 py-2.5 rounded-lg text-sm border outline-none"
               style="border-color: var(--border-color); background-color: var(--bg-secondary); color: var(--text-primary)" />
        <input v-model="password" type="password" placeholder="密码"
               class="w-full px-3 py-2.5 rounded-lg text-sm border outline-none"
               style="border-color: var(--border-color); background-color: var(--bg-secondary); color: var(--text-primary)" />
        <p v-if="error" class="text-xs" style="color: var(--accent-red)">{{ error }}</p>
        <button type="submit" :disabled="loading" class="w-full py-2.5 rounded-lg text-sm font-bold text-white transition-all hover:scale-105 disabled:opacity-50"
                style="background: var(--accent)">
          {{ loading ? '请稍候...' : '进入后台' }}
        </button>
      </form>
    </div>
  </div>
</template>
