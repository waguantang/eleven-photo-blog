import api from './index'

export interface Track {
  id: number
  title: string
  artist?: string
  url?: string
  cover?: string
  duration?: number
  sortOrder: number
  createdAt: string
}

export const trackApi = {
  list() { return api.get<{ code: number; data: Track[] }>('/tracks') },
  listAdmin() { return api.get<{ code: number; data: Track[] }>('/admin/tracks') },
  create(data: { title: string; artist?: string; url?: string; cover?: string; duration?: number; sortOrder?: number }) {
    return api.post<{ code: number; data: Track }>('/admin/tracks', data)
  },
  update(id: number, data: { title: string; artist?: string; url?: string; cover?: string; duration?: number; sortOrder?: number }) {
    return api.put<{ code: number; data: Track }>(`/admin/tracks/${id}`, data)
  },
  delete(id: number) { return api.delete(`/admin/tracks/${id}`) },
}
