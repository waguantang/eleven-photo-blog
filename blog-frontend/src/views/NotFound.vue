<script setup lang="ts">
</script>

<template>
  <div class="max-w-lg mx-auto px-4 py-24 text-center">
    <div class="text-7xl mb-6 opacity-20">&#x1F4F7;</div>
    <h1 class="text-2xl font-bold mb-4" style="font-family: var(--font-display); font-weight: 400; color: var(--accent);">
      页面走丢了...
    </h1>
    <p class="mb-2" style="color: var(--text-secondary); font-size: 0.95rem;">
      就像一张对焦失败的照片，这个页面变得模糊不清。
    </p>
    <p class="mb-8" style="color: var(--text-secondary); font-size: 0.85rem;">
      你访问的页面不存在 —— 也许它被放进了某个忘记标记的文件夹里。
    </p>
    <RouterLink
      to="/"
      class="inline-block px-6 py-2.5 text-sm font-bold no-underline transition-all hover:scale-105"
      style="font-family: var(--font-display); background-color: var(--accent); color: #fff;"
    >
      回到首页 &rarr;
    </RouterLink>
  </div>
</template>
