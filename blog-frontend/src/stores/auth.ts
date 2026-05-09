import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '../api/auth'
import router from '../router'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('token'))
  const username = ref<string | null>(localStorage.getItem('username'))

  const isLoggedIn = computed(() => !!token.value)

  async function login(name: string, password: string) {
    const res = await authApi.login(name, password)
    if (res.data.code === 200) {
      token.value = res.data.data.token
      username.value = res.data.data.username
      localStorage.setItem('token', token.value!)
      localStorage.setItem('username', username.value!)
      return true
    }
    return false
  }

  function logout() {
    authApi.logout().catch(() => {})
    token.value = null
    username.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    router.push('/admin/login')
  }

  return { token, username, isLoggedIn, login, logout }
})
