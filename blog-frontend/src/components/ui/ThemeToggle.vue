<script setup lang="ts">
import { useThemeStore } from '../../stores/theme'

const theme = useThemeStore()
</script>

<template>
  <button
    @click="theme.toggle()"
    class="p-1.5 rounded-sm text-base transition-all duration-500 hover:scale-110 active:scale-95"
    style="color: var(--text-secondary)"
    :title="theme.isDark ? '切换到浅色模式' : '切换到深色模式'"
  >
    <span v-if="theme.isDark">☽</span>
    <span v-else>☀</span>
  </button>
</template>
