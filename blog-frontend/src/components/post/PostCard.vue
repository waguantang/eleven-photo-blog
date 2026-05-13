<script setup lang="ts">
import { computed } from 'vue'
import type { Post } from '../../api/post'

const props = defineProps<{ post: Post }>()

const dateISO = computed(() => {
  const d = (props.post as any).date || props.post.publishedAt || props.post.createdAt
  return new Date(d).toISOString().split('T')[0]
})
</script>

<template>
  <RouterLink :to="`/post/${post.slug}`" class="block no-underline" style="color: inherit">
    <article class="group relative transition-all duration-200 cursor-pointer">
      <!-- Cover image -->
      <div class="relative overflow-hidden" style="aspect-ratio: 4/3; border-bottom: 1px solid var(--border);">
        <img
          v-if="post.coverImage"
          :src="post.coverImage" :alt="post.title"
          class="w-full h-full object-cover transition-all duration-500 group-hover:scale-105"
        />
      </div>

      <!-- Body -->
      <div class="p-4">
        <!-- Date + Category row -->
        <div class="flex items-center gap-2 mb-2">
          <span class="text-xs" style="font-family: var(--font-mono); color: var(--accent); font-size: 0.6rem;">
            {{ dateISO }}
          </span>
          <span
            v-if="post.category"
            class="filter-tag"
            style="font-size: 0.55rem; padding: 1px 6px;"
          >{{ post.category.name }}</span>
        </div>

        <!-- Title -->
        <h3 class="text-lg font-bold mb-2 leading-snug line-clamp-2 group-hover:text-blue-600 transition-colors"
            style="font-family: var(--font-display); font-size: 1.05rem; font-weight: 400;">
          {{ post.title }}
        </h3>

        <!-- Excerpt -->
        <p v-if="post.excerpt" class="text-sm line-clamp-2 mb-3 leading-relaxed"
           style="color: var(--text-secondary); font-family: var(--font-sans); font-size: 0.78rem;">
          {{ post.excerpt }}
        </p>

        <!-- Tags -->
        <div v-if="post.tags?.length" class="flex gap-1.5 flex-wrap">
          <span
            v-for="tag in post.tags.slice(0, 3)" :key="tag.id"
            class="text-xs"
            style="color: var(--ink-light); font-family: var(--font-mono); font-size: 0.6rem;"
          >#{{ tag.name }}</span>
        </div>
      </div>
    </article>
  </RouterLink>
</template>
