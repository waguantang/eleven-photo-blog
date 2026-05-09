<script setup lang="ts">
</script>

<template>
  <div class="min-h-screen">
    <div class="relative action-lines-bg py-16">
      <div class="max-w-2xl mx-auto px-4 text-center relative z-10">
        <div class="text-7xl mb-6 opacity-15">📷</div>
        <h1 class="text-4xl font-bold mb-4 ink-stroke"
            style="font-family: var(--font-display); font-weight: 400;">
          关于本站
        </h1>
        <p class="text-sm opacity-50" style="font-family: var(--font-mono); font-size: 0.65rem;">
          ABOUT THIS BLOG
        </p>
      </div>
    </div>

    <div class="max-w-2xl mx-auto px-4 py-12">
      <div class="manga-panel p-8 mb-8">
        <h2 class="text-xl font-bold mb-4" style="font-family: var(--font-display); font-weight: 400;">
          欢迎来到<span style="color: var(--kita-red);">Eleven的摄影笔记</span>
        </h2>
        <div class="space-y-4 leading-relaxed" style="color: var(--text-secondary); font-family: var(--font-sans); font-size: 0.9rem;">
          <p>你好，我是 Eleven。这里是我记录摄影学习历程的个人博客。</p>

          <p>在这里你会看到：</p>
          <ul class="space-y-2" style="list-style: none; padding-left: 0;">
            <li>📷 摄影构图与用光技巧分享</li>
            <li>📱 手机摄影实用教程</li>
            <li>🎨 调色与后期处理心得</li>
            <li>🌆 街拍、人像、风光实拍经验</li>
          </ul>

          <p>我不是专业摄影师，只是一个热爱按下快门记录生活的人。</p>
          <p>如果你也喜欢摄影，欢迎一起交流探讨。</p>
        </div>
      </div>

      <!-- 格言 -->
      <div class="scream-bubble text-center mb-8">
        <p class="text-lg" style="font-family: var(--font-display); color: var(--text-primary); font-weight: 400;">
          「摄影是把瞬间变成永恒的艺术」
        </p>
        <p class="text-sm mt-2 opacity-50" style="font-family: var(--font-sans);">
          — 每一个热爱记录的人
        </p>
      </div>

      <!-- Photography icons grid -->
      <div class="grid grid-cols-4 gap-2 mb-8">
        <div class="flex items-center justify-center text-4xl" style="aspect-ratio: 1; border: 2px solid var(--ink); background: var(--bg-card);">
          📷
        </div>
        <div class="flex items-center justify-center text-4xl" style="aspect-ratio: 1; border: 2px solid var(--ink); background: var(--bg-card);">
          🌅
        </div>
        <div class="flex items-center justify-center text-4xl" style="aspect-ratio: 1; border: 2px solid var(--ink); background: var(--bg-card);">
          📱
        </div>
        <div class="flex items-center justify-center text-4xl" style="aspect-ratio: 1; border: 2px solid var(--ink); background: var(--bg-card);">
          🎨
        </div>
      </div>

      <div class="pt-8 text-center" style="border-top: 3px double var(--ink);">
        <p class="text-xs opacity-25" style="font-family: var(--font-mono); font-size: 0.55rem;">
          PHOTOGRAPHY BLOG &bull; LIGHT &amp; FRAME &bull; SINCE 2024
        </p>
      </div>
    </div>
  </div>
</template>
