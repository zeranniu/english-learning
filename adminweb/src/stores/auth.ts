import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import request from '@/utils/request'

interface AdminInfo {
  id: number
  username: string
  nickname: string
  avatar: string
  roles: { id: number; roleName: string; roleCode: string }[]
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('admin_token') || '')
  const adminInfo = ref<AdminInfo | null>(
    JSON.parse(localStorage.getItem('admin_info') || 'null')
  )

  const isLoggedIn = computed(() => !!token.value)
  const roles = computed(() => adminInfo.value?.roles?.map(r => r.roleCode) || [])

  function setToken(t: string) {
    token.value = t
    localStorage.setItem('admin_token', t)
  }

  function setAdminInfo(info: AdminInfo) {
    adminInfo.value = info
    localStorage.setItem('admin_info', JSON.stringify(info))
  }

  function clearAuth() {
    token.value = ''
    adminInfo.value = null
    localStorage.removeItem('admin_token')
    localStorage.removeItem('admin_info')
  }

  async function login(username: string, password: string) {
    const data: any = await request.post('/admin/auth/login', { username, password })
    setToken(data.token)
    setAdminInfo({
      id: data.adminId,
      username: data.username,
      nickname: data.nickname,
      avatar: data.avatar,
      roles: (data.roles || []).map((r: string) => ({ id: 0, roleName: r, roleCode: r })),
    })
    return data
  }

  async function fetchInfo() {
    const data: any = await request.get('/admin/auth/info')
    setAdminInfo(data)
    return data
  }

  async function logout() {
    await request.post('/admin/auth/logout').catch(() => {})
    clearAuth()
  }

  return { token, adminInfo, isLoggedIn, roles, setToken, setAdminInfo, clearAuth, login, fetchInfo, logout }
})
