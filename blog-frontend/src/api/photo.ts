import api from './index'

export interface Photo {
  id: number
  title: string
  description?: string
  src: string
  category?: string
  sortOrder: number
  createdAt: string
}

export const photoApi = {
  list() { return api.get<{ code: number; data: Photo[] }>('/photos') },
  listAdmin() { return api.get<{ code: number; data: Photo[] }>('/admin/photos') },
  create(data: { title: string; description?: string; src: string; category?: string; sortOrder?: number }) {
    return api.post<{ code: number; data: Photo }>('/admin/photos', data)
  },
  update(id: number, data: { title: string; description?: string; src: string; category?: string; sortOrder?: number }) {
    return api.put<{ code: number; data: Photo }>(`/admin/photos/${id}`, data)
  },
  delete(id: number) { return api.delete(`/admin/photos/${id}`) },
}
