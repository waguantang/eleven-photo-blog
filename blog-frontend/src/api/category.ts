import api from './index'

export interface Category {
  id: number
  name: string
  slug: string
  description?: string
  createdAt: string
}

export interface Tag {
  id: number
  name: string
  slug: string
  createdAt: string
}

export const categoryApi = {
  list() { return api.get<{ code: number; data: Category[] }>('/categories') },
  create(data: { name: string; description?: string }) { return api.post<{ code: number; data: Category }>('/admin/categories', data) },
  update(id: number, data: { name: string; description?: string }) { return api.put<{ code: number; data: Category }>(`/admin/categories/${id}`, data) },
  delete(id: number) { return api.delete(`/admin/categories/${id}`) },
}

export const tagApi = {
  list() { return api.get<{ code: number; data: Tag[] }>('/tags') },
  create(data: { name: string }) { return api.post<{ code: number; data: Tag }>('/admin/tags', data) },
  update(id: number, data: { name: string }) { return api.put<{ code: number; data: Tag }>(`/admin/tags/${id}`, data) },
  delete(id: number) { return api.delete(`/admin/tags/${id}`) },
}
