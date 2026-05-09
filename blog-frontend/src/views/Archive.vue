<script setup lang="ts">
import Timeline from '../components/archive/Timeline.vue'
</script>

<template>
  <div class="max-w-4xl mx-auto px-4 py-8">
    <div class="text-center mb-10">
      <h1 class="text-3xl font-bold mb-2" style="font-family: var(--font-display); font-weight: 400;">
        文章归档
      </h1>
      <p style="color: var(--text-secondary); font-family: var(--font-mono); font-size: 0.65rem;">
        所有文章，按时间正序排列
      </p>
    </div>
    <Timeline />
  </div>
</template>
