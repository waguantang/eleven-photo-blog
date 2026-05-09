<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import ThemeToggle from '../ui/ThemeToggle.vue'

const route = useRoute()
const scrolled = ref(false)
const mobileOpen = ref(false)

function onScroll() { scrolled.value = window.scrollY > 30 }
onMounted(() => { window.addEventListener('scroll', onScroll, { passive: true }) })
onUnmounted(() => { window.removeEventListener('scroll', onScroll) })

const links = [
  { to: '/', label: '首页' },
  { to: '/archive', label: '归档' },
  { to: '/portfolio', label: '作品集' },
	  { to: '/about', label: '关于' },
]
</script>

<template>
  <nav
    class="sticky top-0 z-50 transition-all duration-300"
    :style="{
      backgroundColor: scrolled ? 'color-mix(in srgb, var(--bg-primary) 94%, transparent)' : 'var(--bg-primary)',
      backdropFilter: scrolled ? 'blur(12px)' : 'none',
      borderBottom: scrolled ? '2px solid var(--ink)' : '2px solid transparent',
    }"
  >
    <div class="max-w-6xl mx-auto px-4 h-16 flex items-center justify-between">
      <!-- Logo -->
      <RouterLink to="/" class="flex items-center gap-2 no-underline group" style="color: inherit">
        <span class="text-xl font-bold tracking-wide" style="font-family: var(--font-display); font-weight: 400;">
          Eleven<span style="color: var(--kita-red);">摄影笔记</span>
        </span>
        <span class="text-xs px-1.5 py-0.5 border-2 rounded-none opacity-60 group-hover:opacity-100 transition-opacity"
              style="border-color: var(--ink); font-family: var(--font-mono); font-size: 0.55rem;">
          BLOG
        </span>
      </RouterLink>

      <!-- Desktop nav -->
      <div class="hidden md:flex items-center gap-1">
        <RouterLink
          v-for="link in links"
          :key="link.to"
          :to="link.to"
          class="relative px-3 py-1.5 text-sm no-underline transition-all"
          style="font-family: var(--font-display); color: var(--text-secondary); font-weight: 400;"
        >
          <span
            v-if="route.path === link.to"
            class="absolute bottom-0 left-2 right-2 h-0.5"
            style="background: var(--kita-red);"
          />
          <span :style="route.path === link.to ? 'color: var(--text-primary);' : ''">{{ link.label }}</span>
        </RouterLink>
        <div class="ml-3 pl-3" style="border-left: 2px solid var(--ink);">
          <ThemeToggle />
        </div>
      </div>

      <!-- Mobile -->
      <div class="flex md:hidden items-center gap-1">
        <ThemeToggle />
        <button
          class="p-2"
          style="font-family: var(--font-display); color: var(--text-primary); font-size: 1.2rem; border: none; background: none; cursor: pointer;"
          @click="mobileOpen = !mobileOpen"
        >
          {{ mobileOpen ? '✕' : '☰' }}
        </button>
      </div>
    </div>

    <!-- Mobile menu -->
    <div
      v-if="mobileOpen"
      class="md:hidden px-4 pb-4 space-y-1"
      style="background: var(--bg-primary); border-bottom: 2px solid var(--ink)"
    >
      <RouterLink
        v-for="link in links"
        :key="link.to"
        :to="link.to"
        class="block px-3 py-2.5 text-sm no-underline transition-colors"
        style="font-family: var(--font-display);"
        :style="route.path === link.to
          ? 'color: var(--kita-red);'
          : 'color: var(--text-secondary);'"
        @click="mobileOpen = false"
      >{{ link.label }}</RouterLink>
    </div>
  </nav>
</template>
