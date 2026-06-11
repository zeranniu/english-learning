import type { IAuthLoginRes } from '@/api/types/login'
import { http } from '@/http/http'

export interface ILoginForm {
  username: string
  password: string
}

/**
 * 用户登录
 */
export function login(loginForm: ILoginForm) {
  return http.post<IAuthLoginRes>('/auth/login', loginForm)
}

/**
 * 退出登录
 */
export function logout() {
  return http.get<void>('/auth/logout')
}

/**
 * 刷新token (单token模式暂不支持)
 */
export function refreshToken(_refreshToken: string) {
  return Promise.reject(new Error('单token模式不支持刷新'))
}

/**
 * 获取微信登录凭证
 */
export function getWxCode() {
  return new Promise<UniApp.LoginRes>((resolve, reject) => {
    uni.login({
      provider: 'weixin',
      success: res => resolve(res),
      fail: err => reject(new Error(err)),
    })
  })
}

/**
 * 微信登录
 */
export function wxLogin(data: { code: string }) {
  return http.post<IAuthLoginRes>('/auth/wxLogin', data)
}
