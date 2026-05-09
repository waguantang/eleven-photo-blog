<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { commentApi, type Comment } from '../../api/comment'
import CommentItem from './CommentItem.vue'
import CommentForm from './CommentForm.vue'

const props = defineProps<{ postId: number }>()

const comments = ref<Comment[]>([])
const loading = ref(true)

// Demo comments for when backend is unavailable
const demoComments = [
  { id: 1, authorName: '摄影新手小王', authorEmail: '', content: '看了这篇文章豁然开朗！之前一直把主体放在正中间，总觉得画面很呆板。现在知道三分法了，马上出去试试。', createdAt: '2025-02-15T10:30:00', replies: [] as Comment[] },
  { id: 2, authorName: '旅行摄影师', authorEmail: '', content: '黄金时刻真的太重要了。上次去西藏，每天早上5点起来蹲日出，拍到了这辈子最满意的照片。', createdAt: '2025-02-16T14:00:00', replies: [] as Comment[] },
  { id: 3, authorName: '手机党阿星', authorEmail: '', content: '用手机人像模式拍的美食照片，朋友都以为是用单反拍的！诀窍就是找侧光和虚化前景。', createdAt: '2025-02-17T09:20:00', replies: [
    { id: 4, authorName: '摄影老司机', authorEmail: '', content: '没错，光线用好了，器材反而是次要的。手机拍照关键是要稳，可以买个小型三脚架。', createdAt: '2025-02-17T11:00:00', replies: [] },
  ] },
]

async function loadComments() {
  try {
    const res = await commentApi.getByPostId(props.postId)
    comments.value = res.data.data
  } catch {
    comments.value = demoComments
  }
  loading.value = false
}

async function onSubmit(data: { authorName: string; authorEmail: string; content: string }) {
  try {
    await commentApi.create({ ...data, postId: props.postId })
  } catch {
    comments.value.unshift({
      id: Date.now(),
      authorName: data.authorName,
      authorEmail: data.authorEmail,
      content: data.content,
      createdAt: new Date().toISOString(),
      replies: [],
    })
  }
}

onMounted(loadComments)
</script>

<template>
  <div class="mt-12 pt-8" style="border-top: 3px double var(--ink);">
    <div class="flex items-center gap-3 mb-8">
      <h3 class="text-xl font-bold" style="font-family: var(--font-display); font-weight: 400;">
        读者留言
      </h3>
      <span class="text-xs px-2 py-0.5" style="font-family: var(--font-mono); font-size: 0.6rem; border: 1px solid var(--ink); color: var(--ink-light);">
        {{ comments.length }} 条留言
      </span>
    </div>

    <div v-if="loading" class="text-center py-4">
      <p style="font-family: var(--font-display); color: var(--text-secondary); font-size: 0.85rem;">加载中...</p>
    </div>

    <div v-else-if="comments.length > 0" class="space-y-4">
      <CommentItem v-for="c in comments" :key="c.id" :comment="c" />
    </div>

    <p v-else class="text-center py-8" style="color: var(--text-secondary); font-family: var(--font-sans); font-size: 0.85rem;">
      还没有留言... 来抢沙发吧！
    </p>

    <!-- Comment form -->
    <div class="manga-panel mt-8 p-6">
      <p class="text-sm font-bold mb-4" style="font-family: var(--font-display); font-weight: 400; color: var(--text-primary);">
        写留言
      </p>
      <CommentForm @submit="onSubmit" />
    </div>
  </div>
</template>
