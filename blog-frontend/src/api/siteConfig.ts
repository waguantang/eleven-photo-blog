import api from './index'

export interface SiteConfig {
  id: number
  configKey: string
  configValue?: string
  updatedAt: string
}

export const siteConfigApi = {
  list() { return api.get<{ code: number; data: Record<string, string> }>('/config') },
  listAdmin() { return api.get<{ code: number; data: SiteConfig[] }>('/admin/config') },
  create(data: { configKey: string; configValue?: string }) {
    return api.post<{ code: number; data: SiteConfig }>('/admin/config', data)
  },
  update(id: number, data: { configKey: string; configValue?: string }) {
    return api.put<{ code: number; data: SiteConfig }>(`/admin/config/${id}`, data)
  },
  delete(id: number) { return api.delete(`/admin/config/${id}`) },
}
