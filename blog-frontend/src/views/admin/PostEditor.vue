<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { postApi } from '../../api/post'
import { categoryApi, tagApi, type Category, type Tag } from '../../api/category'
import AdminSidebar from '../../components/layout/AdminSidebar.vue'
import { MdEditor, MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => !!route.params.id)

const title = ref('')
const content = ref('')
const excerpt = ref('')
const coverImage = ref('')
const status = ref('draft')
const categoryId = ref<number | undefined>()
const selectedTagIds = ref<number[]>([])
const categories = ref<Category[]>([])
const tags = ref<Tag[]>([])
const saving = ref(false)
const error = ref('')

onMounted(async () => {
  const [cats, tgs] = await Promise.all([categoryApi.list(), tagApi.list()])
  categories.value = cats.data.data
  tags.value = tgs.data.data

  if (isEdit.value) {
    try {
      const res = await postApi.getById(Number(route.params.id))
      const p = res.data.data
      title.value = p.title
      content.value = p.content
      excerpt.value = p.excerpt || ''
      coverImage.value = p.coverImage || ''
      status.value = p.status
      categoryId.value = p.categoryId
      selectedTagIds.value = p.tags?.map(t => t.id) || []
    } catch { error.value = '文章加载失败' }
  }
})

async function save() {
  if (!title.value.trim() || !content.value.trim()) {
    error.value = '标题和内容不能为空'; return
  }
  error.value = ''
  saving.value = true
  try {
    const data = {
      title: title.value,
      content: content.value,
      excerpt: excerpt.value,
      coverImage: coverImage.value,
      status: status.value,
      categoryId: categoryId.value,
      tagIds: selectedTagIds.value,
    }
    if (isEdit.value) {
      await postApi.update(Number(route.params.id), data)
    } else {
      await postApi.create(data)
    }
    router.push('/admin/posts')
  } catch { error.value = '保存失败' }
  saving.value = false
}

function toggleTag(id: number) {
  const idx = selectedTagIds.value.indexOf(id)
  if (idx >= 0) selectedTagIds.value.splice(idx, 1)
  else selectedTagIds.value.push(id)
}
</script>

<template>
  <div class="max-w-6xl mx-auto px-4 py-8">
    <div class="flex gap-8">
      <AdminSidebar />
      <div class="flex-1 min-w-0">
        <h1 class="text-2xl font-black mb-6" style="font-family: var(--font-display)">
          {{ isEdit ? '编辑文章' : '写新文章' }}
        </h1>

        <div class="space-y-4">
          <input v-model="title" placeholder="文章标题"
                 class="w-full px-4 py-3 rounded-xl text-lg font-bold border outline-none"
                 style="font-family: var(--font-display); border-color: var(--border-color); background-color: var(--bg-card); color: var(--text-primary)" />

          <div class="flex flex-wrap gap-4">
            <select v-model="categoryId"
                    class="px-3 py-2 rounded-lg text-sm border outline-none"
                    style="border-color: var(--border-color); background-color: var(--bg-card); color: var(--text-primary)">
              <option :value="undefined">选择分类</option>
              <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
            </select>

            <select v-model="status"
                    class="px-3 py-2 rounded-lg text-sm border outline-none"
                    style="border-color: var(--border-color); background-color: var(--bg-card); color: var(--text-primary)">
              <option value="draft">草稿</option>
              <option value="published">发布</option>
            </select>
          </div>

          <!-- Tags -->
          <div class="flex flex-wrap gap-2">
            <button v-for="tag in tags" :key="tag.id" @click="toggleTag(tag.id)"
                    class="px-3 py-1 rounded-full text-xs font-medium transition-colors border"
                    :style="selectedTagIds.includes(tag.id)
                      ? 'background-color: var(--accent-pink); color: white; border-color: var(--accent-pink)'
                      : 'background-color: var(--bg-card); color: var(--text-secondary); border-color: var(--border-color)'">
              {{ tag.name }}
            </button>
          </div>

          <input v-model="excerpt" placeholder="文章摘要（选填）"
                 class="w-full px-3 py-2 rounded-lg text-sm border outline-none"
                 style="border-color: var(--border-color); background-color: var(--bg-card); color: var(--text-primary)" />
          <input v-model="coverImage" placeholder="封面图片 URL（选填）"
                 class="w-full px-3 py-2 rounded-lg text-sm border outline-none"
                 style="border-color: var(--border-color); background-color: var(--bg-card); color: var(--text-primary)" />

          <!-- Markdown editor -->
          <MdEditor v-model="content" language="en-US" :toolbars="['bold', 'italic', 'strikethrough', 'title', '|', 'quote', 'unorderedList', 'orderedList', 'code', 'link', 'image', 'table', '|', 'preview', 'fullscreen']"
                    style="height: 500px; border-radius: 0.75rem; overflow: hidden" />

          <p v-if="error" class="text-sm" style="color: var(--accent-red)">{{ error }}</p>

          <div class="flex gap-3">
            <button @click="save" :disabled="saving"
                    class="px-6 py-2.5 rounded-lg text-sm font-bold text-white transition-all hover:scale-105 disabled:opacity-50"
                    style="background: var(--accent)">
              {{ saving ? '保存中...' : (status === 'published' ? '发布！🎸' : '保存草稿') }}
            </button>
            <button @click="router.back()"
                    class="px-6 py-2.5 rounded-lg text-sm font-medium transition-colors"
                    style="color: var(--text-secondary); background-color: var(--bg-secondary)">
              取消
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
