<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { photoApi, type Photo } from '../api/photo'

const parallax = ref(0)
function onParallax() { parallax.value = window.scrollY * 0.3 }
onMounted(() => window.addEventListener('scroll', onParallax, { passive: true }))
onUnmounted(() => window.removeEventListener('scroll', onParallax))

const photos = ref<Photo[]>([])
const fallback: Photo[] = [
  { id: 0, title: '晨光熹微', description: '清晨第一缕阳光穿过薄雾，洒在静谧的街道上。柔和的光线营造出朦胧而温暖的氛围，整个画面仿佛被镀上了一层金色的薄纱。', src: '/YouName/1006667.jpg', category: '风光', sortOrder: 1, createdAt: '2025-01-01' },
  { id: 0, title: '城市脉动', description: '繁华都市的一角，行人与车流交织成独特的城市节奏。利用慢速快门捕捉到动静结合的画面——静止的建筑背景下，流动的人群形成了有趣的对比。', src: '/YouName/1006672.jpg', category: '街拍', sortOrder: 2, createdAt: '2025-02-01' },
  { id: 0, title: '静谧时光', description: '一个安静的午后，光影透过窗户洒在木质地板上。画面中弥漫着慵懒而温暖的气息，让人想起那些值得被定格的平凡瞬间。', src: '/YouName/1006728.jpg', category: '静物', sortOrder: 3, createdAt: '2025-02-01' },
  { id: 0, title: '回眸一瞬', description: '人像摄影的魅力在于捕捉那些转瞬即逝的表情。这张照片利用了侧逆光，让发丝边缘泛起温柔的光晕，为人物增添了一份灵动。', src: '/YouName/1007102.jpg', category: '人像', sortOrder: 4, createdAt: '2025-03-01' },
  { id: 0, title: '山水之间', description: '远山如黛，近水含烟。这是一张典型的风光摄影作品，运用了前景、中景、远景的三层构图法，让画面有了丰富的层次感和空间感。', src: '/YouName/1007172.jpg', category: '风光', sortOrder: 5, createdAt: '2025-03-01' },
  { id: 0, title: '夜色阑珊', description: '夜幕降临，城市的灯光开始闪烁。这张夜景用了低ISO和长曝光，让灯光的轨迹在画面中流动。三脚架是夜景拍摄的必备利器。', src: '/YouName/1007178.jpg', category: '夜景', sortOrder: 6, createdAt: '2025-04-01' },
  { id: 0, title: '花语', description: '微距镜头下的花朵，花瓣的纹理和露珠都清晰可见。浅景深让背景化为柔美的色块，主体更加突出——这是静物摄影中常用的手法。', src: '/YouName/1244598.jpg', category: '静物', sortOrder: 7, createdAt: '2025-04-01' },
  { id: 0, title: '街角故事', description: '街头摄影的有趣之处在于偶然性。一个不起眼的街角，在特定的光线和路人的配合下，就成了一个有故事感的画面。低角度拍摄让画面更有电影感。', src: '/YouName/1254303.png', category: '街拍', sortOrder: 8, createdAt: '2025-04-01' },
  { id: 0, title: '窗外的世界', description: '利用窗户作为天然画框，将室外的景色框住。框架式构图让观众的视线自然而然地聚焦在窗外的景色上，同时也增添了画面的纵深。', src: '/YouName/731739.png', category: '构图练习', sortOrder: 9, createdAt: '2025-05-01' },
  { id: 0, title: '落日余晖', description: '黄金时刻的最后一抹阳光。逆光下的剪影效果，天空呈现出由橙到紫的渐变——这是大自然最美的调色盘。拍日落的关键：宁可欠曝也不要过曝。', src: '/YouName/740275.png', category: '风光', sortOrder: 10, createdAt: '2025-05-01' },
  { id: 0, title: '黑白映像', description: '剥离了色彩之后，光影和构图就成了画面唯一的主角。这张黑白作品强调了画面的线条感和对比度，适合表现建筑或人物的情绪。', src: '/YouName/748076.png', category: '黑白', sortOrder: 11, createdAt: '2025-05-01' },
  { id: 0, title: '人间烟火', description: '菜市场、老街道、手艺人——这些充满生活气息的场景是人文摄影的宝库。拍好人文的关键是融入环境，让被摄者忽略你的存在。', src: '/YouName/764987.jpg', category: '人文', sortOrder: 12, createdAt: '2025-06-01' },
  { id: 0, title: '夏日的风', description: '用高速快门凝固了被风吹起的发丝和裙摆。自然光下的人像，没有刻意摆拍，而是捕捉了人物最自然的状态——这才是好的人像。', src: '/YouName/765121.png', category: '人像', sortOrder: 13, createdAt: '2025-06-01' },
  { id: 0, title: '几何之美', description: '建筑的线条、楼梯的螺旋、走廊的纵深——抽象几何是极简摄影的精髓。找到有趣的角度，让画面变成线与面的艺术构成。', src: '/YouName/815592.png', category: '建筑', sortOrder: 14, createdAt: '2025-07-01' },
  { id: 0, title: '雨后', description: '雨后地面的积水形成的倒影，让平凡的场景多了一份诗意。低角度贴近水面拍摄，让倒影和实物形成对称的构图——这是雨天特有的魔法。', src: '/YouName/903819.png', category: '街拍', sortOrder: 15, createdAt: '2025-07-01' },
]

onMounted(async () => {
  try {
    const res = await photoApi.list()
    photos.value = res.data.data
  } catch {
    photos.value = fallback
  }
})

function formatDate(dateStr: string) {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  return `${y}.${m}`
}

const activeIndex = ref<number | null>(null)
const loaded = ref<Set<number>>(new Set())

function openLightbox(index: number) {
  activeIndex.value = index
}

function closeLightbox() {
  activeIndex.value = null
}

function goPrev() {
  if (activeIndex.value !== null && activeIndex.value > 0) {
    activeIndex.value--
  }
}

function goNext() {
  if (activeIndex.value !== null && activeIndex.value < photos.value.length - 1) {
    activeIndex.value++
  }
}

function onKeydown(e: KeyboardEvent) {
  if (activeIndex.value === null) return
  if (e.key === 'Escape') closeLightbox()
  if (e.key === 'ArrowLeft') goPrev()
  if (e.key === 'ArrowRight') goNext()
}

function onImageLoad(index: number) {
  loaded.value = new Set(loaded.value).add(index)
}

// Register keyboard listener
if (typeof window !== 'undefined') {
  window.addEventListener('keydown', onKeydown)
}
</script>

<template>
  <div class="min-h-screen">
    <!-- ═══ Header ═══ -->
    <div class="relative hero-pattern py-16">
      <div class="max-w-2xl mx-auto px-4 text-center relative z-10">
        <div class="text-7xl mb-6 opacity-15" :style="{ transform: `translateY(${parallax}px)` }">📷</div>
        <h1 class="text-4xl font-bold mb-4 heading-underline"
            style="font-family: var(--font-display); font-weight: 400;">
          作品集
        </h1>
        <p class="text-sm opacity-50" style="font-family: var(--font-sans); color: var(--text-secondary);">
          每一张照片，都是按下快门时的心跳
        </p>
      </div>
    </div>

    <!-- ═══ Filter bar ═══ -->
    <div class="max-w-6xl mx-auto px-4 py-8">
      <div class="flex flex-wrap justify-center gap-2 mb-10">
        <span
          v-for="cat in [...new Set(photos.map(p => p.category))]" :key="cat"
          class="filter-tag cursor-pointer text-xs"
          style="font-size: 0.7rem;"
        >{{ cat }}</span>
      </div>

      <!-- ═══ Gallery Grid ═══ -->
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
        <div
          v-for="(photo, i) in photos" :key="i"
          v-motion
          class="card-panel overflow-hidden group cursor-pointer"
          @click="openLightbox(i)"
        >
          <!-- Image -->
          <div class="relative overflow-hidden" style="aspect-ratio: 4/3;">
            <img
              :src="photo.src"
              :alt="photo.title"
              loading="lazy"
              class="w-full h-full object-cover transition-all duration-500 group-hover:scale-105"
              @load="onImageLoad(i)"
            />
            <!-- Hover overlay -->
            <div class="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex items-center justify-center">
              <span class="text-white text-sm font-bold px-4 py-2"
                    style="font-family: var(--font-display); border: 1px solid rgba(255,255,255,0.6); font-weight: 400;">
                查看大图
              </span>
            </div>
            <!-- Category badge -->
            <span class="absolute top-2 left-2 text-xs px-2 py-0.5 text-white/90"
                  style="font-family: var(--font-mono); font-size: 0.6rem; background: rgba(0,0,0,0.5);">
              {{ photo.category }}
            </span>
          </div>

          <!-- Info -->
          <div class="p-4">
            <div class="flex items-center justify-between mb-1.5">
              <h3 class="text-sm font-bold" style="font-family: var(--font-display); font-weight: 400;">
                {{ photo.title }}
              </h3>
              <span class="text-xs opacity-40" style="font-family: var(--font-mono); font-size: 0.6rem;">
                {{ formatDate(photo.createdAt) }}
              </span>
            </div>
            <p class="text-xs leading-relaxed line-clamp-2"
               style="color: var(--text-secondary); font-family: var(--font-sans); font-size: 0.75rem;">
              {{ photo.description }}
            </p>
          </div>
        </div>
      </div>
    </div>

    <!-- ═══ Lightbox Modal ═══ -->
    <Teleport to="body">
      <div
        v-if="activeIndex !== null"
        class="fixed inset-0 z-[100] flex items-center justify-center"
        style="background: rgba(0,0,0,0.92);"
        @click.self="closeLightbox"
      >
        <!-- Close button -->
        <button
          class="absolute top-4 right-4 z-10 text-white/70 hover:text-white text-2xl transition-colors"
          style="background: none; border: none; cursor: pointer;"
          @click="closeLightbox"
        >✕</button>

        <!-- Counter -->
        <span class="absolute top-4 left-4 text-white/50 text-xs z-10" style="font-family: var(--font-mono);">
          {{ activeIndex + 1 }} / {{ photos.length }}
        </span>

        <!-- Prev -->
        <button
          v-if="activeIndex > 0"
          class="absolute left-4 top-1/2 -translate-y-1/2 z-10 text-white/60 hover:text-white text-3xl transition-colors"
          style="background: none; border: none; cursor: pointer;"
          @click.stop="goPrev"
        >‹</button>

        <!-- Image -->
        <div class="max-w-5xl max-h-[85vh] w-full px-8 md:px-16">
          <img
            :src="photos[activeIndex].src"
            :alt="photos[activeIndex].title"
            class="w-full max-h-[75vh] object-contain"
          />
          <!-- Caption -->
          <div class="mt-4 text-center">
            <h3 class="text-white text-lg font-bold mb-1" style="font-family: var(--font-display); font-weight: 400;">
              {{ photos[activeIndex].title }}
            </h3>
            <p class="text-white/50 text-xs mb-1" style="font-family: var(--font-mono); font-size: 0.65rem;">
              {{ formatDate(photos[activeIndex].createdAt) }} &bull; {{ photos[activeIndex].category }}
            </p>
            <p class="text-white/70 text-sm max-w-lg mx-auto leading-relaxed"
               style="font-family: var(--font-sans); font-size: 0.8rem;">
              {{ photos[activeIndex].description }}
            </p>
          </div>
        </div>

        <!-- Next -->
        <button
          v-if="activeIndex < photos.length - 1"
          class="absolute right-4 top-1/2 -translate-y-1/2 z-10 text-white/60 hover:text-white text-3xl transition-colors"
          style="background: none; border: none; cursor: pointer;"
          @click.stop="goNext"
        >›</button>
      </div>
    </Teleport>
  </div>
</template>
