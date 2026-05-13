<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { postApi, type Post } from '../../api/post'
import AdminSidebar from '../../components/layout/AdminSidebar.vue'

const router = useRouter()
const posts = ref<Post[]>([])
const total = ref(0)
const page = ref(1)
const status = ref('')
const keyword = ref('')
const loading = ref(true)

async function load() {
  loading.value = true
  try {
    const res = await postApi.listAdmin({ page: page.value, size: 10, status: status.value || undefined, keyword: keyword.value || undefined })
    posts.value = res.data.data.records
    total.value = res.data.data.total
  } catch { /* empty */ }
  loading.value = false
}

async function deletePost(id: number) {
  if (!confirm('确定要删除这篇文章吗？此操作不可恢复。')) return
  await postApi.delete(id)
  load()
}

onMounted(load)
</script>

<template>
  <div class="max-w-6xl mx-auto px-4 py-8">
    <div class="flex gap-8">
      <AdminSidebar />
      <div class="flex-1 min-w-0">
        <div class="flex items-center justify-between mb-6">
          <h1 class="text-2xl font-black" style="font-family: var(--font-display)">文章管理</h1>
          <button @click="router.push('/admin/posts/new')"
                  class="px-4 py-2 rounded-lg text-sm font-bold text-white transition-all hover:scale-105"
                  style="background: var(--accent)">
            + 写文章
          </button>
        </div>

        <div class="flex gap-3 mb-4">
          <select v-model="status" @change="page=1;load()"
                  class="px-3 py-1.5 rounded-lg text-sm border outline-none"
                  style="border-color: var(--border-color); background-color: var(--bg-card); color: var(--text-primary)">
            <option value="">全部状态</option>
            <option value="published">已发布</option>
            <option value="draft">草稿</option>
          </select>
          <input v-model="keyword" @keyup.enter="page=1;load()" placeholder="搜索标题..."
                 class="px-3 py-1.5 rounded-lg text-sm border outline-none flex-1 max-w-xs"
                 style="border-color: var(--border-color); background-color: var(--bg-card); color: var(--text-primary)" />
        </div>

        <div v-if="loading" class="text-center py-8" style="color: var(--text-secondary)">加载中...</div>
        <div v-else class="rounded-xl overflow-hidden" style="border: 1px solid var(--border-color)">
          <table class="w-full text-sm">
            <thead style="background-color: var(--bg-secondary)">
              <tr>
                <th class="text-left p-3" style="color: var(--text-secondary)">标题</th>
                <th class="text-left p-3 hidden sm:table-cell" style="color: var(--text-secondary)">状态</th>
                <th class="text-left p-3 hidden md:table-cell" style="color: var(--text-secondary)">发布时间</th>
                <th class="text-right p-3" style="color: var(--text-secondary)">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="post in posts" :key="post.id" class="border-t" style="border-color: var(--border-color)">
                <td class="p-3">
                  <span class="font-medium" style="color: var(--text-primary)">{{ post.title }}</span>
                </td>
                <td class="p-3 hidden sm:table-cell">
                  <span class="px-2 py-0.5 rounded-full text-xs"
                        :style="post.status === 'published' ? 'background-color: color-mix(in srgb, green 15%, transparent); color: green' : 'background-color: color-mix(in srgb, var(--accent-yellow) 15%, transparent); color: var(--accent-yellow)'">
                    {{ post.status === 'published' ? '已发布' : '草稿' }}
                  </span>
                </td>
                <td class="p-3 hidden md:table-cell" style="color: var(--text-secondary)">
                  {{ post.publishedAt ? new Date(post.publishedAt).toLocaleDateString('zh-CN') : '-' }}
                </td>
                <td class="p-3 text-right">
                  <button @click="router.push(`/admin/posts/${post.id}/edit`)" class="text-xs px-2 py-1 rounded mr-1"
                          style="color: var(--accent-pink); background-color: color-mix(in srgb, var(--accent-pink) 10%, transparent)">编辑</button>
                  <button @click="deletePost(post.id)" class="text-xs px-2 py-1 rounded"
                          style="color: var(--accent-red); background-color: color-mix(in srgb, var(--accent-red) 10%, transparent)">删除</button>
                </td>
              </tr>
            </tbody>
          </table>
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
