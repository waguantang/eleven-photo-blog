<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { postApi, type Post } from '../api/post'
import { categoryApi, type Category } from '../api/category'
import PostCard from '../components/post/PostCard.vue'
import FeaturedPost from '../components/post/FeaturedPost.vue'
import Pagination from '../components/ui/Pagination.vue'

const posts = ref<Post[]>([])
const featuredPost = ref<Post | null>(null)
const categories = ref<Category[]>([])
const page = ref(1)
const total = ref(0)
const size = 9
const loading = ref(true)
const useDemo = ref(false)

// ── Demo articles: 摄影技巧文章 ──
const demoArticles = [
  {
    title: '三分法构图：从入门到精通',
    excerpt: '九宫格是摄影构图的基石。把主体放在四条线的交叉点上，照片立刻变得有"呼吸感"。本文带你从零开始掌握三分法。',
    date: '2024.12.01',
    category: '构图技巧',
    cover: '/img/Volume01.jpg',
    tags: ['构图', '三分法', '入门'],
    slug: 'rule-of-thirds',
  },
  {
    title: '光线的魔法：黄金时刻拍摄完全指南',
    excerpt: '日出后一小时和日落前一小时，是摄影师最爱的"黄金时刻"。光线柔和、色温温暖，学会利用这段时间，你的照片质感能提升一个档次。',
    date: '2024.12.08',
    category: '用光技巧',
    cover: '/img/Volume02.jpg',
    tags: ['光线', '黄金时刻', '逆光'],
    slug: 'golden-hour',
  },
  {
    title: '街拍秘籍：在平凡街头捕捉非凡瞬间',
    excerpt: '街拍不需要昂贵的器材，一部手机足矣。关键是观察力——学会发现光影交错处、等待有趣的人经过、用低角度避开杂乱背景。',
    date: '2024.12.15',
    category: '街拍纪实',
    cover: '/img/Volume03.jpg',
    tags: ['街拍', '手机摄影', '观察力'],
    slug: 'street-photography',
  },
  {
    title: '人像摄影：让普通人拍出模特感的7个技巧',
    excerpt: '不是只有专业模特才能拍出好看的人像。找对光线角度、让人物远离背景、利用前景虚化增加层次——这些简单技巧人人都能用。',
    date: '2025.01.05',
    category: '人像摄影',
    cover: '/img/Volume04.jpg',
    tags: ['人像', '光线', '姿态'],
    slug: 'portrait-tips',
  },
  {
    title: '手机摄影：你口袋里的专业相机',
    excerpt: '现代手机的摄影能力已经强大到令人惊叹。学会这些手机专属技巧——网格线、人像模式、手动曝光——你的朋友圈照片会与众不同。',
    date: '2025.01.20',
    category: '手机摄影',
    cover: '/img/Volume05.jpg',
    tags: ['手机', '技巧', '后期'],
    slug: 'mobile-photography',
  },
  {
    title: '后期调色：让照片讲述更有情绪的故事',
    excerpt: '摄影是前期与后期的结合。学会基础的颜色理论——冷暖对比、互补色、色调统一——你就能通过调色让观众感受到你想传达的情绪。',
    date: '2025.02.10',
    category: '后期处理',
    cover: '/img/Volume06.jpg',
    tags: ['调色', '后期', '色彩'],
    slug: 'color-grading',
  },
]

// ── Demo comments ──
const demoComments = [
  { author: '摄影新手小王', content: '三分法那篇太有用了！以前拍的照片总是居中对齐，现在终于知道怎么构图了。', date: '2025.01.15' },
  { author: '旅行爱好者', content: '黄金时刻的建议太实用了，这次去云南旅行就按照文章说的蹲日出，拍到了超美的梅里雪山！', date: '2025.01.20' },
  { author: '街拍达人老张', content: '街拍那篇写得很好，低角度确实是被很多人忽略的技巧。另外建议可以试试雨天街拍，倒影很出片。', date: '2025.02.03' },
  { author: '喜欢拍娃的妈妈', content: '人像技巧太及时了！用手机人像模式拍孩子，终于不用每张都是糊的了。前景虚化那招特别好用。', date: '2025.02.18' },
  { author: '设计系学生', content: '后期调色那篇讲的颜色理论很专业，跟我上设计课学的色彩构成有异曲同工之处。期待更多进阶内容！', date: '2025.03.01' },
]

const derivedPosts = computed(() => {
  if (useDemo.value) return demoArticles as unknown as Post[]
  return posts.value
})

const derivedFeatured = computed(() => {
  if (useDemo.value) return demoArticles[0] as unknown as Post
  return featuredPost.value
})

const derivedCategories = computed(() => {
  if (useDemo.value) return [
    { id: 1, name: '构图技巧', slug: 'composition' },
    { id: 2, name: '用光技巧', slug: 'lighting' },
    { id: 3, name: '街拍纪实', slug: 'street' },
    { id: 4, name: '人像摄影', slug: 'portrait' },
    { id: 5, name: '手机摄影', slug: 'mobile' },
    { id: 6, name: '后期处理', slug: 'post-processing' },
  ] as Category[]
  return categories.value
})

const derivedTotal = computed(() => {
  if (useDemo.value) return demoArticles.length
  return total.value
})

onMounted(async () => {
  try {
    const [postRes, catRes] = await Promise.all([
      postApi.list({ page: 1, size }),
      categoryApi.list()
    ])
    const list = postRes.data.data.records
    if (list.length > 0) {
      featuredPost.value = list[0]
      posts.value = list.slice(1)
    }
    total.value = postRes.data.data.total
    categories.value = catRes.data.data
  } catch {
    useDemo.value = true
    posts.value = demoArticles.slice(1) as unknown as Post[]
  }
  loading.value = false
})

async function changePage(p: number) {
  page.value = p
  if (useDemo.value) return
  loading.value = true
  window.scrollTo({ top: 0, behavior: 'smooth' })
  try {
    const res = await postApi.list({ page: p, size })
    posts.value = res.data.data.records
    total.value = res.data.data.total
  } catch { /* empty */ }
  loading.value = false
}

// ── 摄影主题卡片 ──
const topics = [
  { name: '构图', icon: '⊞', color: 'var(--bocchi-pink)', desc: '三分法、引导线、框架——学会这些基础，照片就有了骨架。' },
  { name: '光线', icon: '☀', color: 'var(--nijika-yellow)', desc: '光线是摄影的灵魂。掌握顺光、逆光、侧光，你就掌握了画面的情绪。' },
  { name: '色彩', icon: '🎨', color: 'var(--ryo-blue)', desc: '色彩搭配决定照片的氛围。冷暖对比、同色系——调色就是调情绪。' },
  { name: '故事', icon: '⌘', color: 'var(--kita-red)', desc: '好照片会讲故事。在按下快门前，先想清楚你想传达什么。' },
]
</script>

<template>
  <div>
    <!-- ═══════════════════════════════════════
         HERO — 摄影主题英雄区
         ═══════════════════════════════════════ -->
    <section class="relative w-full overflow-hidden" style="height: 85vh; min-height: 520px;">
      <div class="absolute inset-0">
        <img
          src="/img/background1.jpg"
          alt="Photography"
          class="w-full h-full object-cover"
        />
      </div>

      <div class="absolute inset-0 hero-overlay" />

      <div class="absolute inset-0 opacity-[0.06] pointer-events-none"
           style="background-image: radial-gradient(circle, #fff 1.5px, transparent 1.5px); background-size: 10px 10px;" />

      <div class="absolute bottom-0 left-0 right-0 h-24 pointer-events-none"
           style="background: repeating-linear-gradient(100deg, transparent, transparent 3px, rgba(255,255,255,0.15) 3px, rgba(255,255,255,0.15) 4px, transparent 4px, transparent 8px);" />

      <div class="absolute inset-0 flex flex-col items-center justify-center text-center px-4">
        <div class="mb-4" v-motion>
          <span class="text-white/70 text-xs tracking-[0.3em] uppercase"
                style="font-family: var(--font-mono); font-size: 0.6rem; border: 1px solid rgba(255,255,255,0.3); padding: 4px 16px; letter-spacing: 0.25em;">
            探索光影 · 记录生活
          </span>
        </div>

        <h1 v-motion class="text-6xl md:text-8xl font-bold mb-4 tracking-wide text-white"
            style="font-family: var(--font-display); font-weight: 400; text-shadow: 3px 3px 0 rgba(0,0,0,0.5);">
          Eleven的摄影笔记
        </h1>

        <p v-motion class="text-white/80 text-lg md:text-xl mb-8 max-w-lg leading-relaxed"
           style="font-family: var(--font-sans); font-weight: 300;">
          用光影写故事，用构图说心情 —— 这里记录我的摄影学习与心得
        </p>

        <div v-motion class="flex gap-4 flex-wrap justify-center">
          <a href="#topics"
             class="px-8 py-3 text-white font-bold no-underline transition-all hover:scale-105"
             style="font-family: var(--font-display); font-weight: 400; border: 2px solid rgba(255,255,255,0.8); background: rgba(255,255,255,0.1); font-size: 0.95rem; backdrop-filter: blur(4px);">
            开始探索
          </a>
          <a href="#articles"
             class="px-8 py-3 font-bold no-underline transition-all hover:scale-105"
             style="font-family: var(--font-display); font-weight: 400; border: 2px solid var(--accent); background: var(--accent); color: #fff; font-size: 0.95rem;">
            阅读文章
          </a>
        </div>

        <div class="absolute bottom-8 animate-[float_2s_ease-in-out_infinite]" style="color: rgba(255,255,255,0.5);">
          <span style="font-family: var(--font-display); font-size: 0.65rem; letter-spacing: 0.2em;">往下看</span>
          <div class="mt-1 text-center text-lg">↓</div>
        </div>
      </div>
    </section>

    <!-- ═══════════════════════════════════════
         SPEED LINES DIVIDER
         ═══════════════════════════════════════ -->
    <div class="section-divider" style="height: 32px;" />

    <!-- ═══════════════════════════════════════
         摄影主题卡片
         ═══════════════════════════════════════ -->
    <section id="topics" class="max-w-6xl mx-auto px-4 py-16">
      <div class="text-center mb-12">
        <h2 class="text-3xl md:text-4xl font-bold mb-3 heading-underline"
            style="font-family: var(--font-display); font-weight: 400;">
          摄影四大要素
        </h2>
        <p class="text-sm" style="color: var(--text-secondary); font-family: var(--font-sans);">
          每一张好照片，都离不开这四个维度
        </p>
      </div>

      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <div
          v-for="t in topics" :key="t.name"
          v-motion
          class="card-panel p-6 overflow-hidden text-center"
        >
          <div class="text-4xl mb-3" style="color: v-bind('t.color');">{{ t.icon }}</div>
          <h3 class="text-lg font-bold mb-2" style="font-family: var(--font-display); font-weight: 400;">{{ t.name }}</h3>
          <p class="text-xs leading-relaxed"
             style="color: var(--text-secondary); font-family: var(--font-sans); font-size: 0.8rem;">
            {{ t.desc }}
          </p>
          <div class="mt-3 h-1 w-12 mx-auto" :style="{ background: t.color }" />
        </div>
      </div>
    </section>

    <div class="section-divider" style="height: 32px;" />

    <!-- ═══════════════════════════════════════
         精选文章 / FEATURED
         ═══════════════════════════════════════ -->
    <section id="articles" class="max-w-6xl mx-auto px-4 py-16">
      <div class="text-center mb-4">
        <div class="stamp mb-8" style="font-size: 0.65rem;">★ 精选推荐 ★</div>
      </div>

      <div v-if="derivedFeatured" class="mb-16">
        <FeaturedPost :post="derivedFeatured" />
      </div>

      <!-- ═══ 分类标签 ═══ -->
      <div v-if="derivedCategories.length" class="flex flex-wrap justify-center gap-3 mb-12">
        <RouterLink
          v-for="cat in derivedCategories" :key="cat.id"
          :to="`/category/${cat.slug}`"
          class="no-underline transition-all hover:scale-105"
        >
          <span class="filter-tag">{{ cat.name }}</span>
        </RouterLink>
      </div>

      <!-- ═══════════════════════════════════
           文章网格
           ═══════════════════════════════════ -->
      <div v-if="loading" class="text-center py-12">
        <p style="font-family: var(--font-display); color: var(--text-secondary); font-size: 0.9rem;">
          加载中...
        </p>
      </div>

      <div v-else-if="derivedPosts.length === 0" class="text-center py-16">
        <div class="text-6xl mb-4 opacity-20">📷</div>
        <p class="text-lg font-bold mb-1" style="font-family: var(--font-display); color: var(--text-secondary);">
          还没有文章
        </p>
      </div>

      <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-[3px] post-grid">
        <div
          v-for="(post, i) in derivedPosts.slice(0, useDemo ? 6 : derivedPosts.length)" :key="post.id || i"
          v-motion
          :style="{
            background: 'var(--bg-card)',
          }"
        >
          <PostCard :post="post" />
        </div>
      </div>

      <Pagination :page="page" :total="derivedTotal" :size="size" @change="changePage" />

      <!-- ═══════════════════════════════════
           读者留言区域
           ═══════════════════════════════════ -->
      <div class="mt-20">
        <div class="text-center mb-10">
          <h2 class="text-3xl md:text-4xl font-bold mb-3 heading-underline"
              style="font-family: var(--font-display); font-weight: 400;">
            读者留言
          </h2>
          <p class="text-sm" style="color: var(--text-secondary); font-family: var(--font-sans);">
            来自摄影同好的声音
          </p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4 max-w-4xl mx-auto">
          <div
            v-for="(cmt, i) in demoComments" :key="i"
            v-motion
            class="comment-card"
          >
            <div class="flex items-center gap-2 mb-2">
              <div class="w-7 h-7 flex items-center justify-center text-xs font-bold text-white"
                   style="font-family: var(--font-display); font-size: 0.6rem;"
                   :style="{ background: i % 2 === 0 ? 'var(--bocchi-pink)' : i % 3 === 0 ? 'var(--ryo-blue)' : 'var(--nijika-yellow)' }">
                {{ cmt.author.charAt(0) }}
              </div>
              <span class="text-sm font-bold" style="font-family: var(--font-display); font-weight: 400;">
                {{ cmt.author }}
              </span>
              <span class="text-xs opacity-40" style="font-family: var(--font-mono); font-size: 0.6rem;">
                {{ cmt.date }}
              </span>
            </div>
            <p class="text-sm leading-relaxed" style="color: var(--text-primary); font-family: var(--font-sans); font-size: 0.85rem;">
              {{ cmt.content }}
            </p>
          </div>
        </div>

        <!-- CTA -->
        <div class="max-w-md mx-auto mt-8 cta-card text-center" v-motion>
          <p class="font-bold mb-3" style="font-family: var(--font-display); font-weight: 400; font-size: 1.1rem;">
            分享你的摄影心得！
          </p>
          <p class="text-sm mb-4" style="color: var(--text-secondary); font-family: var(--font-sans); font-size: 0.8rem;">
            任何关于摄影的问题、心得、作品，都欢迎在这里交流。
          </p>
          <RouterLink to="/" class="inline-block px-6 py-2 text-sm font-bold text-white no-underline transition-all hover:scale-105"
               style="font-family: var(--font-display); font-weight: 400; background: var(--accent); border: none;">
            写留言
          </RouterLink>
        </div>
      </div>
    </section>

    <!-- ═══════════════════════════════════════
         BOTTOM — 博客信息
         ═══════════════════════════════════════ -->
    <div class="max-w-6xl mx-auto px-4 pb-8">
      <div class="pt-8 text-center" style="border-top: 1px solid var(--border);">
        <p class="text-xs opacity-30" style="font-family: var(--font-mono); font-size: 0.55rem;">
          PHOTOGRAPHY BLOG &bull; ISSUE #01 &bull; 光影之间，记录美好
        </p>
      </div>
    </div>
  </div>
</template>
