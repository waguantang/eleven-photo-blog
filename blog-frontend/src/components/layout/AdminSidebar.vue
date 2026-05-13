<script setup lang="ts">
import { RouterLink, useRoute } from 'vue-router'

const route = useRoute()

const menu = [
  { to: '/admin', label: '仪表盘', icon: '📊' },
  { to: '/admin/posts', label: '文章管理', icon: '📝' },
  { to: '/admin/photos', label: '作品管理', icon: '📷' },
  { to: '/admin/tracks', label: '音乐管理', icon: '🎵' },
  { to: '/admin/comments', label: '评论管理', icon: '💬' },
  { to: '/admin/categories', label: '分类管理', icon: '📁' },
  { to: '/admin/tags', label: '标签管理', icon: '🏷' },
  { to: '/admin/config', label: '站点配置', icon: '⚙' },
]
</script>

<template>
  <aside class="w-56 flex-shrink-0 hidden md:block">
    <nav class="sticky top-20 rounded-xl p-3 space-y-1"
         style="background-color: var(--bg-card); border: 1px solid var(--border-color)">
      <RouterLink
        v-for="item in menu"
        :key="item.to"
        :to="item.to"
        class="flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium no-underline transition-colors"
        :style="route.path === item.to
          ? 'background-color: color-mix(in srgb, var(--accent-pink) 15%, transparent); color: var(--accent-pink)'
          : 'color: var(--text-secondary)'"
      >
        <span>{{ item.icon }}</span>
        <span>{{ item.label }}</span>
      </RouterLink>
    </nav>
  </aside>
  <!-- Mobile nav -->
  <nav class="md:hidden flex flex-wrap gap-2 mb-6">
    <RouterLink
      v-for="item in menu"
      :key="item.to"
      :to="item.to"
      class="px-3 py-1.5 rounded-full text-xs font-medium no-underline"
      :style="route.path === item.to
        ? 'background-color: var(--accent-pink); color: white'
        : 'background-color: var(--bg-card); color: var(--text-secondary); border: 1px solid var(--border-color)'"
    >{{ item.icon }} {{ item.label }}</RouterLink>
  </nav>
</template>
