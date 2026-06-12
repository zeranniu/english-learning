import { tabbarStore } from '@/tabbar/store'
import { useTokenStore } from '@/store/token'

// 不需要登录就能访问的页面
const WHITE_LIST = ['/pages/login/index']

export const permission = {
  install(router) {
    router.beforeEach((to, from, next) => {
      const path = to.path
      tabbarStore.setAutoCurIdx(path)

      // 白名单页面直接放行
      if (WHITE_LIST.includes(path)) {
        return next()
      }

      // 检查是否有有效 token
      const tokenStore = useTokenStore()
      
      // 直接检查 tokenInfo 是否有值（不依赖 updateNowTime）
      const hasToken = tokenStore.tokenInfo && 
        ((tokenStore.tokenInfo as any).token || (tokenStore.tokenInfo as any).accessToken)
      
      if (hasToken) {
        next()
      } else {
        // 无有效 token，跳转登录页
        next('/pages/login/index')
      }
    })
  },
}
