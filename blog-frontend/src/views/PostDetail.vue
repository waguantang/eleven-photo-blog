<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { postApi, type Post } from '../api/post'
import PostContent from '../components/post/PostContent.vue'
import CommentSection from '../components/comment/CommentSection.vue'

const route = useRoute()
const post = ref<Post | null>(null)
const prev = ref<Post | null>(null)
const next = ref<Post | null>(null)
const loading = ref(true)

const date = computed(() => {
  if (!post.value) return ''
  return new Date(post.value.publishedAt || post.value.createdAt).toLocaleDateString('zh-CN', {
    year: 'numeric', month: 'long', day: 'numeric'
  })
})

onMounted(async () => {
  const slug = route.params.slug as string
  try {
    const res = await postApi.getBySlug(slug)
    post.value = res.data.data
    const pn = await postApi.getPrevNext(post.value.id)
    prev.value = pn.data.data.prev
    next.value = pn.data.data.next
  } catch { /* 404 */ }
  loading.value = false
})
</script>

<template>
  <div class="max-w-3xl mx-auto px-4 py-8">
    <div v-if="loading" class="text-center py-16">
      <p style="font-family: var(--font-display); color: var(--text-secondary); font-size: 0.9rem;">
        加载中...
      </p>
    </div>

    <div v-else-if="!post" class="text-center py-16">
      <div class="text-7xl mb-4 opacity-20">&#x1F3B8;</div>
      <p class="text-xl mb-2" style="font-family: var(--font-display); color: var(--text-secondary);">
        文章未找到
      </p>
      <p style="color: var(--text-secondary); font-size: 0.85rem;">
        这篇文章迷失在光影之中了。
      </p>
    </div>

    <article v-else>
      <!-- Cover image with manga panel border -->
      <div v-if="post.coverImage" class="manga-panel overflow-hidden mb-8" style="border-bottom-width: 3px;">
        <img
          :src="post.coverImage"
          :alt="post.title"
          class="w-full h-64 md:h-80 object-cover"
        />
      </div>

      <!-- Header -->
      <h1 class="text-3xl md:text-4xl font-bold mb-4 leading-tight"
          style="font-family: var(--font-display); font-weight: 400;">
        {{ post.title }}
      </h1>

      <div class="flex flex-wrap items-center gap-3 mb-8 text-sm">
        <span style="font-family: var(--font-mono); font-size: 0.7rem; color: var(--accent);">
          {{ date }}
        </span>
        <RouterLink
          v-if="post.category"
          :to="`/category/${post.category.slug}`"
          class="no-underline text-xs"
          style="font-family: var(--font-display); color: var(--accent-blue); font-size: 0.7rem; border-bottom: 1px solid var(--accent-blue);"
        >{{ post.category.name }}</RouterLink>
        <span
          v-for="tag in post.tags" :key="tag.id"
          class="text-xs"
          style="font-family: var(--font-mono); color: var(--text-secondary); font-size: 0.65rem;"
        >#{{ tag.name }}</span>
      </div>

      <!-- Content -->
      <PostContent :content="post.content" />

      <!-- Prev / Next nav -->
      <div class="mt-12 pt-8 grid grid-cols-2 gap-4" style="border-top: 3px double var(--ink);">
        <RouterLink
          v-if="prev"
          :to="`/post/${prev.slug}`"
          class="no-underline p-4 transition-all group manga-panel"
        >
          <span class="text-xs" style="font-family: var(--font-mono); color: var(--text-secondary); font-size: 0.6rem;">&larr; 上一篇</span>
          <p class="text-sm font-bold mt-1 line-clamp-1 group-hover:text-red-700 transition-colors"
             style="font-family: var(--font-display); font-weight: 400;">{{ prev.title }}</p>
        </RouterLink>
        <div v-else />

        <RouterLink
          v-if="next"
          :to="`/post/${next.slug}`"
          class="no-underline p-4 text-right transition-all group manga-panel"
        >
          <span class="text-xs" style="font-family: var(--font-mono); color: var(--text-secondary); font-size: 0.6rem;">下一篇 &rarr;</span>
          <p class="text-sm font-bold mt-1 line-clamp-1 group-hover:text-red-700 transition-colors"
             style="font-family: var(--font-display); font-weight: 400;">{{ next.title }}</p>
        </RouterLink>
      </div>

      <!-- Comments -->
      <CommentSection :post-id="post.id" />
    </article>
  </div>
</template>
