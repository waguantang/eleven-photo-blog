<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { siteConfigApi } from '../api/siteConfig'

interface Section { title: string; items: string[] }

const title = ref('关于本站')
const subtitle = ref('ABOUT THIS BLOG')
const intro = ref('你好，我是 Eleven。这里是我记录摄影学习历程的个人博客。')
const sections = ref<Section[]>([
  { title: '在这里你会看到', items: ['📷 摄影构图与用光技巧分享', '📱 手机摄影实用教程', '🎨 调色与后期处理心得', '🌆 街拍、人像、风光实拍经验'] },
  { title: '关于我', items: ['我不是专业摄影师，只是一个热爱按下快门记录生活的人。', '如果你也喜欢摄影，欢迎一起交流探讨。'] },
])
const quote = ref('「摄影是把瞬间变成永恒的艺术」')
const quoteAuthor = ref('每一个热爱记录的人')

onMounted(async () => {
  try {
    const res = await siteConfigApi.list()
    const cfg = res.data.data
    if (cfg.about_title) title.value = cfg.about_title
    if (cfg.about_subtitle) subtitle.value = cfg.about_subtitle
    if (cfg.about_intro) intro.value = cfg.about_intro
    if (cfg.about_sections) {
      try { sections.value = JSON.parse(cfg.about_sections) } catch {}
    }
    if (cfg.about_quote) quote.value = cfg.about_quote
    if (cfg.about_quote_author) quoteAuthor.value = cfg.about_quote_author
  } catch { /* keep defaults */ }
})
</script>

<template>
  <div class="min-h-screen">
    <div class="relative hero-pattern py-16">
      <div class="max-w-2xl mx-auto px-4 text-center relative z-10">
        <div class="text-7xl mb-6 opacity-15">📷</div>
        <h1 class="text-4xl font-bold mb-4 heading-underline">
          {{ title }}
        </h1>
        <p class="text-sm opacity-50" style="font-family: var(--font-mono); font-size: 0.65rem;">
          {{ subtitle }}
        </p>
      </div>
    </div>

    <div class="max-w-2xl mx-auto px-4 py-12">
      <div class="card-panel p-8 mb-8">
        <h2 class="text-xl font-bold mb-4">
          欢迎来到<span style="color: var(--accent);">Eleven的摄影笔记</span>
        </h2>
        <div class="space-y-4 leading-relaxed" style="color: var(--text-secondary); font-family: var(--font-sans); font-size: 0.9rem;">
          <p>{{ intro }}</p>

          <template v-for="(section, si) in sections" :key="si">
            <p v-if="section.title">{{ section.title }}：</p>
            <ul class="space-y-2" style="list-style: none; padding-left: 0;">
              <li v-for="(item, ii) in section.items" :key="ii">{{ item }}</li>
            </ul>
          </template>
        </div>
      </div>

      <!-- 格言 -->
      <div class="cta-card text-center mb-8">
        <p class="text-lg" style="font-family: var(--font-display); color: var(--text-primary); font-weight: 400;">
          {{ quote }}
        </p>
        <p class="text-sm mt-2 opacity-50" style="font-family: var(--font-sans);">
          — {{ quoteAuthor }}
        </p>
      </div>

      <!-- Photography icons grid -->
      <div class="grid grid-cols-4 gap-2 mb-8">
        <div class="flex items-center justify-center text-4xl" style="aspect-ratio: 1; border: 1px solid var(--border); background: var(--bg-card); border-radius: 8px;">
          📷
        </div>
        <div class="flex items-center justify-center text-4xl" style="aspect-ratio: 1; border: 1px solid var(--border); background: var(--bg-card); border-radius: 8px;">
          🌅
        </div>
        <div class="flex items-center justify-center text-4xl" style="aspect-ratio: 1; border: 1px solid var(--border); background: var(--bg-card); border-radius: 8px;">
          📱
        </div>
        <div class="flex items-center justify-center text-4xl" style="aspect-ratio: 1; border: 1px solid var(--border); background: var(--bg-card); border-radius: 8px;">
          🎨
        </div>
      </div>

      <div class="pt-8 text-center" style="border-top: 1px solid var(--border);">
        <p class="text-xs opacity-25" style="font-family: var(--font-mono); font-size: 0.55rem;">
          PHOTOGRAPHY BLOG &bull; LIGHT &amp; FRAME &bull; SINCE 2024
        </p>
      </div>
    </div>
  </div>
</template>
