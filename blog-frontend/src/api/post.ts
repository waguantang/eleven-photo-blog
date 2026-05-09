import api from './index'

export interface Post {
  id: number
  title: string
  slug: string
  content: string
  excerpt?: string
  coverImage?: string
  status: string
  categoryId?: number
  category?: { id: number; name: string; slug: string }
  tags?: { id: number; name: string; slug: string }[]
  createdAt: string
  updatedAt: string
  publishedAt?: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  page: number
  size: number
}

export const postApi = {
  list(params: { page?: number; size?: number; category?: number; tag?: number; keyword?: string }) {
    return api.get<{ code: number; data: PageResult<Post> }>('/posts', { params })
  },
  getBySlug(slug: string) {
    return api.get<{ code: number; data: Post }>(`/posts/${slug}`)
  },
  getPrevNext(id: number) {
    return api.get<{ code: number; data: { prev: Post | null; next: Post | null } }>(`/posts/${id}/prev-next`)
  },
  getArchiveMonths() {
    return api.get<{ code: number; data: string[] }>('/archive-months')
  },
  // Admin
  listAdmin(params: { page?: number; size?: number; status?: string; keyword?: string }) {
    return api.get<{ code: number; data: PageResult<Post> }>('/admin/posts', { params })
  },
  getById(id: number) {
    return api.get<{ code: number; data: Post }>(`/admin/posts/${id}`)
  },
  create(data: { title: string; content: string; excerpt?: string; coverImage?: string; status?: string; categoryId?: number; tagIds?: number[] }) {
    return api.post<{ code: number; data: Post }>('/admin/posts', data)
  },
  update(id: number, data: { title: string; content: string; excerpt?: string; coverImage?: string; status?: string; categoryId?: number; tagIds?: number[] }) {
    return api.put<{ code: number; data: Post }>(`/admin/posts/${id}`, data)
  },
  delete(id: number) {
    return api.delete<{ code: number }>(`/admin/posts/${id}`)
  },
}
