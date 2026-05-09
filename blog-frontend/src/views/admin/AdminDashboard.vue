<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAuthStore } from '../../stores/auth'
import { postApi } from '../../api/post'
import { commentApi } from '../../api/comment'
import AdminSidebar from '../../components/layout/AdminSidebar.vue'

const auth = useAuthStore()
const postTotal = ref(0)
const commentPending = ref(0)

onMounted(async () => {
  try {
    const [posts, pending] = await Promise.all([
      postApi.listAdmin({ page: 1, size: 1 }),
      commentApi.getPendingCount()
    ])
    postTotal.value = posts.data.data.total
    commentPending.value = pending.data.data.count
  } catch { /* empty */ }
})
</script>

<template>
  <div class="max-w-6xl mx-auto px-4 py-8">
    <div class="flex gap-8">
      <AdminSidebar />
      <div class="flex-1 min-w-0">
        <h1 class="text-2xl font-black mb-6" style="font-family: var(--font-display)">仪表盘</h1>
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          <div class="p-6 rounded-xl" style="background: linear-gradient(135deg, color-mix(in srgb, var(--accent-pink) 20%, transparent), color-mix(in srgb, var(--accent-pink) 10%, transparent)); border: 1px solid color-mix(in srgb, var(--accent-pink) 30%, transparent)">
            <p class="text-3xl font-black" style="color: var(--accent-pink)">{{ postTotal }}</p>
            <p class="text-sm mt-1" style="color: var(--text-secondary)">文章总数</p>
          </div>
          <div class="p-6 rounded-xl" style="background: linear-gradient(135deg, color-mix(in srgb, var(--accent-yellow) 20%, transparent), color-mix(in srgb, var(--accent-yellow) 10%, transparent)); border: 1px solid color-mix(in srgb, var(--accent-yellow) 30%, transparent)">
            <p class="text-3xl font-black" style="color: var(--accent-yellow)">{{ commentPending }}</p>
            <p class="text-sm mt-1" style="color: var(--text-secondary)">待审核评论</p>
          </div>
          <div class="p-6 rounded-xl" style="background: linear-gradient(135deg, color-mix(in srgb, var(--accent-blue) 20%, transparent), color-mix(in srgb, var(--accent-blue) 10%, transparent)); border: 1px solid color-mix(in srgb, var(--accent-blue) 30%, transparent)">
            <p class="text-base font-bold mt-2" style="font-family: var(--font-display); color: var(--accent-blue)">
              {{ auth.username }}
            </p>
            <p class="text-sm mt-1" style="color: var(--text-secondary)">已登录</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
