<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { postApi, type Post } from '../api/post'
import { tagApi, type Tag } from '../api/category'
import PostCard from '../components/post/PostCard.vue'
import Pagination from '../components/ui/Pagination.vue'

const route = useRoute()
const posts = ref<Post[]>([])
const tag = ref<Tag | null>(null)
const page = ref(1)
const total = ref(0)
const loading = ref(true)

async function load() {
  loading.value = true
  try {
    const tags = await tagApi.list()
    tag.value = tags.data.data.find(t => t.slug === route.params.slug) || null
    if (tag.value) {
      const res = await postApi.list({ page: page.value, size: 9, tag: tag.value.id })
      posts.value = res.data.data.records
      total.value = res.data.data.total
    }
  } catch { /* empty */ }
  loading.value = false
}

onMounted(load)
watch(() => route.params.slug, load)
</script>

<template>
  <div class="max-w-6xl mx-auto px-4 py-8">
    <div class="text-center mb-10">
      <h1 class="text-3xl font-bold mb-1" style="font-family: var(--font-display); font-weight: 400;">
        #{{ tag?.name || '标签' }}
      </h1>
      <p class="text-xs opacity-40" style="font-family: var(--font-mono); font-size: 0.6rem;">
        按标签筛选
      </p>
    </div>

    <div v-if="loading" class="text-center py-8">
      <p style="font-family: var(--font-display); color: var(--text-secondary); font-size: 0.9rem;">加载中...</p>
    </div>

    <div v-else-if="posts.length === 0" class="text-center py-16">
      <p style="font-family: var(--font-display); color: var(--text-secondary);">这个标签下还没有文章。</p>
    </div>

    <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
      <div
        v-for="(post, i) in posts" :key="post.id"
        :style="{
          transform: `rotate(${i % 3 === 0 ? -0.5 : i % 3 === 1 ? 0.7 : -0.4}deg)`,
          animation: `card-drop 0.4s ease-out ${i * 0.06}s both`,
        }"
      >
        <PostCard :post="post" />
      </div>
    </div>

    <Pagination :page="page" :total="total" :size="9" @change="(p: number) => { page = p; load() }" />
  </div>
</template>
