import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes: RouteRecordRaw[] = [
  { path: '/login', component: () => import('@/views/login/LoginView.vue'), meta: { title: '登录' } },
  {
    path: '/',
    component: () => import('@/layouts/AdminLayout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', component: () => import('@/views/dashboard/DashboardView.vue'), meta: { title: '数据看板' } },
      { path: 'user/list', component: () => import('@/views/user/UserListView.vue'), meta: { title: '学生列表' } },
      { path: 'user/edit/:id?', component: () => import('@/views/user/UserEditView.vue'), meta: { title: '学生编辑' } },
      { path: 'system/role', component: () => import('@/views/system/RoleListView.vue'), meta: { title: '角色管理' } },
      { path: 'system/menu', component: () => import('@/views/system/MenuListView.vue'), meta: { title: '菜单管理' } },
      { path: 'system/log', component: () => import('@/views/system/LogListView.vue'), meta: { title: '操作日志' } },
      { path: 'content/vocab', component: () => import('@/views/content/VocabListView.vue'), meta: { title: '单词库' } },
      { path: 'content/vocab/edit/:id?', component: () => import('@/views/content/VocabEditView.vue'), meta: { title: '单词编辑' } },
      { path: 'content/listening', component: () => import('@/views/content/ListeningListView.vue'), meta: { title: '听力题库' } },
      { path: 'content/listening/edit/:id?', component: () => import('@/views/content/ListeningEditView.vue'), meta: { title: '听力编辑' } },
      { path: 'content/reading', component: () => import('@/views/content/ReadingListView.vue'), meta: { title: '阅读题库' } },
      { path: 'content/reading/edit/:id?', component: () => import('@/views/content/ReadingEditView.vue'), meta: { title: '阅读编辑' } },
      { path: 'content/grammar', component: () => import('@/views/content/GrammarListView.vue'), meta: { title: '语法题库' } },
      { path: 'content/grammar/edit/:id?', component: () => import('@/views/content/GrammarEditView.vue'), meta: { title: '语法编辑' } },
      { path: 'stats/mistake', component: () => import('@/views/stats/MistakeStatsView.vue'), meta: { title: '错题统计' } },
      { path: 'stats/data', component: () => import('@/views/stats/DataStatsView.vue'), meta: { title: '学习统计' } },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, _from, next) => {
  document.title = `${to.meta.title || ''} - 管理端`
  if (to.path === '/login') return next()
  const authStore = useAuthStore()
  if (!authStore.isLoggedIn) return next('/login')
  next()
})

export default router
