<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { commentApi, type Comment } from '../../api/comment'
import AdminSidebar from '../../components/layout/AdminSidebar.vue'

const comments = ref<Comment[]>([])
const total = ref(0)
const page = ref(1)
const status = ref('')
const loading = ref(true)

async function load() {
  loading.value = true
  try {
    const res = await commentApi.listAdmin({ page: page.value, size: 10, status: status.value || undefined })
    comments.value = res.data.data.records
    total.value = res.data.data.total
  } catch { /* empty */ }
  loading.value = false
}

async function approve(id: number) { await commentApi.updateStatus(id, 'approved'); load() }
async function reject(id: number) { await commentApi.updateStatus(id, 'rejected'); load() }
async function deleteComment(id: number) { if (confirm('确定删除？')) { await commentApi.delete(id); load() } }

onMounted(load)
</script>

<template>
  <div class="max-w-6xl mx-auto px-4 py-8">
    <div class="flex gap-8">
      <AdminSidebar />
      <div class="flex-1 min-w-0">
        <h1 class="text-2xl font-black mb-6" style="font-family: var(--font-display)">评论管理</h1>

        <div class="mb-4">
          <select v-model="status" @change="page=1;load()"
                  class="px-3 py-1.5 rounded-lg text-sm border outline-none"
                  style="border-color: var(--border-color); background-color: var(--bg-card); color: var(--text-primary)">
            <option value="">全部状态</option>
            <option value="pending">待审核</option>
            <option value="approved">已通过</option>
            <option value="rejected">已拒绝</option>
          </select>
        </div>

        <div v-if="loading" class="text-center py-8" style="color: var(--text-secondary)">加载中...</div>
        <div v-else class="space-y-3">
          <div v-for="c in comments" :key="c.id" class="p-4 rounded-xl"
               style="background-color: var(--bg-card); border: 1px solid var(--border-color)">
            <div class="flex items-center justify-between mb-2">
              <div>
                <span class="text-sm font-bold" style="color: var(--text-primary)">{{ c.authorName }}</span>
                <span class="text-xs ml-2" style="color: var(--text-secondary)">
                  {{ new Date(c.createdAt).toLocaleDateString('zh-CN') }}
                </span>
                <span class="ml-2 px-2 py-0.5 rounded text-xs"
                      :style="c.status === 'approved' ? 'color: green; background-color: color-mix(in srgb, green 10%, transparent)' : c.status === 'rejected' ? 'color: var(--accent-red); background-color: color-mix(in srgb, var(--accent-red) 10%, transparent)' : 'color: var(--accent-yellow); background-color: color-mix(in srgb, var(--accent-yellow) 10%, transparent)'">
                  {{ c.status === 'approved' ? '已通过' : c.status === 'rejected' ? '已拒绝' : '待审核' }}
                </span>
              </div>
              <div class="flex gap-1">
                <button v-if="c.status !== 'approved'" @click="approve(c.id)" class="text-xs px-2 py-1 rounded"
                        style="color: green; background-color: color-mix(in srgb, green 10%, transparent)">通过</button>
                <button v-if="c.status !== 'rejected'" @click="reject(c.id)" class="text-xs px-2 py-1 rounded"
                        style="color: var(--accent-yellow); background-color: color-mix(in srgb, var(--accent-yellow) 10%, transparent)">拒绝</button>
                <button @click="deleteComment(c.id)" class="text-xs px-2 py-1 rounded"
                        style="color: var(--accent-red); background-color: color-mix(in srgb, var(--accent-red) 10%, transparent)">删除</button>
              </div>
            </div>
            <p class="text-sm" style="color: var(--text-primary)">{{ c.content }}</p>
          </div>
        </div>

        <div v-if="total > 10" class="flex justify-center mt-6 gap-2">
          <button v-for="p in Math.ceil(total / 10)" :key="p" @click="page = p; load()"
                  class="min-w-[32px] h-8 px-2 rounded text-xs font-medium"
                  :style="p === page ? 'background-color: var(--accent-pink); color: white' : 'background-color: var(--bg-secondary); color: var(--text-secondary)'">
            {{ p }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
