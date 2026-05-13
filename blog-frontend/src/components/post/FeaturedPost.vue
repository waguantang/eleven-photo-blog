<script setup lang="ts">
import { computed } from 'vue'
import type { Post } from '../../api/post'

const props = defineProps<{ post: Post }>()

const date = computed(() => {
  const d = (props.post as any).date || props.post.publishedAt || props.post.createdAt
  return new Date(d).toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })
})
</script>

<template>
  <RouterLink :to="`/post/${post.slug}`" class="block no-underline" style="color: inherit">
    <div class="relative group cursor-pointer max-w-4xl mx-auto">
      <!-- Manga panel frame -->
      <div class="relative card-panel overflow-hidden">
        <!-- Image area with cinematic aspect ratio -->
        <div class="relative overflow-hidden" style="aspect-ratio: 16/7; border-bottom: 1px solid var(--border);">
          <img
            v-if="post.coverImage"
            :src="post.coverImage" :alt="post.title"
            class="w-full h-full object-cover transition-all duration-700 group-hover:scale-[1.02]"
          />
          <!-- Gradient overlay -->
          <div class="absolute inset-0" style="background: linear-gradient(to top, rgba(0,0,0,0.5) 0%, transparent 50%);" />

          <!-- Title overlay on image -->
          <div class="absolute bottom-0 left-0 right-0 p-6 md:p-8">
            <div class="flex items-center gap-2 mb-2">
              <span class="text-xs tracking-wider px-2 py-0.5 text-white"
                    style="font-family: var(--font-mono); font-size: 0.6rem; border: 1px solid rgba(255,255,255,0.6); background: rgba(0,0,0,0.3);">
                {{ date }}
              </span>
              <span v-if="post.category" class="text-xs px-2 py-0.5 text-white"
                    style="font-family: var(--font-display); font-size: 0.6rem; background: var(--accent);">
                {{ post.category.name }}
              </span>
            </div>
            <h2 class="text-2xl md:text-3xl font-bold leading-tight text-white"
                style="font-family: var(--font-display); font-weight: 400;">
              {{ post.title }}
            </h2>
            <p v-if="post.excerpt" class="mt-2 text-sm text-white/80 line-clamp-1"
               style="font-family: var(--font-sans); font-size: 0.85rem;">
              {{ post.excerpt }}
            </p>
          </div>
        </div>
      </div>

      <!-- "NEW" badge -->
      <div class="absolute -top-2 -right-2 pointer-events-none"
           style="font-family: var(--font-display); font-size: 0.7rem; background: var(--accent); color: #fff; padding: 6px 10px; border-radius: 999px;">
        NEW!
      </div>
    </div>
  </RouterLink>
</template>
