<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { postApi, type Post } from '../../api/post'

const groupedPosts = ref<Map<string, Post[]>>(new Map())
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await postApi.list({ size: 100 })
    const posts = res.data.data.records
    const map = new Map<string, Post[]>()
    for (const p of posts) {
      const month = (p.publishedAt || p.createdAt).substring(0, 7)
      if (!map.has(month)) map.set(month, [])
      map.get(month)!.push(p)
    }
    groupedPosts.value = map
  } catch { /* empty */ }
  loading.value = false
})
</script>

<template>
  <div v-if="loading" class="text-center py-8">
    <p style="font-family: var(--font-display); color: var(--text-secondary); font-size: 0.9rem;">Loading archive...</p>
  </div>

  <div v-else-if="groupedPosts.size === 0" class="text-center py-16">
    <div class="text-6xl mb-4 opacity-20">&#x1F4ED;</div>
    <p class="text-lg" style="font-family: var(--font-display); color: var(--text-secondary);">
      The archive is empty — nothing has been printed yet.
    </p>
  </div>

  <div v-else class="relative">
    <!-- Center line -->
    <div class="absolute left-1/2 top-0 bottom-0 w-px -translate-x-1/2 hidden md:block"
         style="background: repeating-linear-gradient(to bottom, var(--border) 0px, var(--border) 4px, transparent 4px, transparent 8px);" />

    <div v-for="([month, monthPosts]) in groupedPosts" :key="month" class="mb-10">
      <!-- Month stamp -->
      <div class="text-center mb-6 relative z-10">
        <span class="inline-block px-3 py-1 text-sm"
              style="font-family: var(--font-mono); color: var(--accent); font-size: 0.7rem; border: 2px solid var(--accent);">
          {{ month }}
        </span>
      </div>

      <div v-for="(post, pi) in monthPosts" :key="post.id" class="mb-6 md:mb-4">
        <div class="flex items-center" :class="pi % 2 === 0 ? 'md:flex-row' : 'md:flex-row-reverse'">
          <!-- Content -->
          <div class="flex-1" :class="pi % 2 === 0 ? 'md:text-right md:pr-8' : 'md:text-left md:pl-8'">
            <RouterLink :to="`/post/${post.slug}`" class="no-underline group block">
              <span class="text-xs" style="font-family: var(--font-mono); color: var(--text-secondary); font-size: 0.6rem;">
                {{ new Date(post.publishedAt || post.createdAt).toLocaleDateString('zh-CN') }}
              </span>
              <h3 class="text-base mt-1 transition-colors line-clamp-1 group-hover:text-red-700"
                  style="font-family: var(--font-display); color: var(--text-primary); font-size: 0.95rem; font-weight: 400;">
                {{ post.title }}
              </h3>
              <p v-if="post.excerpt" class="text-sm mt-1 line-clamp-1" style="color: var(--text-secondary); font-size: 0.8rem;">
                {{ post.excerpt }}
              </p>
            </RouterLink>
          </div>

          <!-- Dot on timeline -->
          <div class="hidden md:flex w-3 h-3 flex-shrink-0 mx-2 z-10 relative"
               style="background-color: var(--accent);"
          />
          <div class="flex-1 hidden md:block" />
        </div>
      </div>
    </div>
  </div>
</template>
