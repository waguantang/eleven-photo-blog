import api from './index'

export interface Comment {
  id: number
  postId: number
  parentId?: number
  authorName: string
  authorEmail?: string
  content: string
  status: string
  createdAt: string
  replies?: Comment[]
}

export const commentApi = {
  getByPostId(postId: number) {
    return api.get<{ code: number; data: Comment[] }>('/comments', { params: { postId } })
  },
  create(data: { postId: number; parentId?: number; authorName: string; authorEmail?: string; content: string }) {
    return api.post<{ code: number; data: { id: number; message: string } }>('/comments', data)
  },
  // Admin
  listAdmin(params: { page?: number; size?: number; status?: string; postId?: number }) {
    return api.get<{ code: number; data: { records: Comment[]; total: number; page: number; size: number } }>('/admin/comments', { params })
  },
  updateStatus(id: number, status: string) {
    return api.put<{ code: number; data: Comment }>(`/admin/comments/${id}`, { status })
  },
  delete(id: number) {
    return api.delete<{ code: number }>(`/admin/comments/${id}`)
  },
  getPendingCount() {
    return api.get<{ code: number; data: { count: number } }>('/admin/comments/pending-count')
  },
}
