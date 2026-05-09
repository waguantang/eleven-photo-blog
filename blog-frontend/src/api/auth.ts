import api from './index'

export const authApi = {
  login(username: string, password: string) {
    return api.post<{ code: number; data: { token: string; username: string } }>('/auth/login', { username, password })
  },
  logout() {
    return api.post<{ code: number }>('/auth/logout')
  },
  session() {
    return api.get<{ code: number; data: { loggedIn: boolean; username: string; userId: number } }>('/auth/session')
  },
}
