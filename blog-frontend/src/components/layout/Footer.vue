<script setup lang="ts">
</script>

<template>
  <footer class="mt-auto mb-20 py-8 text-center">
    <!-- Speed lines top -->
    <div class="speed-lines mb-8" style="height: 24px;" />

    <div class="max-w-6xl mx-auto px-4">
      <p class="mb-2 text-lg" style="font-family: var(--font-display); font-size: 1.1rem;">
        <span style="color: var(--kita-red);">Eleven的摄影笔记</span>
      </p>
      <p class="text-xs mb-4 opacity-50" style="font-family: var(--font-sans);">
        用光影写故事，用构图说心情
      </p>
      <p class="text-xs opacity-25" style="font-family: var(--font-mono); font-size: 0.55rem;">
        &copy; {{ new Date().getFullYear() }} Eleven's Photo Blog &mdash; LIGHT &amp; FRAME
      </p>
    </div>
  </footer>
</template>
