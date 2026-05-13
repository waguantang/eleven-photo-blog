<script setup lang="ts">
import { ref } from 'vue'

const emit = defineEmits<{ submit: [data: { authorName: string; authorEmail: string; content: string }] }>()

const authorName = ref('')
const authorEmail = ref('')
const content = ref('')
const submitted = ref(false)
const error = ref('')

function handleSubmit() {
  if (!authorName.value.trim() || !content.value.trim()) {
    error.value = '请填写昵称和留言内容'
    return
  }
  error.value = ''
  emit('submit', {
    authorName: authorName.value.trim(),
    authorEmail: authorEmail.value.trim(),
    content: content.value.trim(),
  })
  submitted.value = true
}
</script>

<template>
  <div v-if="submitted" class="text-center py-4 v-motion">
    <p class="font-bold" style="font-family: var(--font-display); color: var(--accent-red); font-weight: 400;">
      提交成功！
    </p>
    <p class="text-xs mt-1" style="color: var(--text-secondary); font-family: var(--font-sans);">
      留言已发送，审核通过后将会显示出来。
    </p>
  </div>

  <form v-else class="space-y-3" @submit.prevent="handleSubmit">
    <div class="flex gap-3">
      <input
        v-model="authorName" type="text" placeholder="昵称 *"
        class="flex-1 px-3 py-2 text-sm outline-none transition-all"
        style="border: 1px solid var(--border); background: var(--bg-card); color: var(--text-primary); font-family: var(--font-sans);"
        :style="{ borderColor: error ? 'var(--accent-red)' : 'var(--border)' }"
      />
      <input
        v-model="authorEmail" type="email" placeholder="邮箱（可选）"
        class="flex-1 px-3 py-2 text-sm outline-none transition-all"
        style="border: 1px solid var(--border); background: var(--bg-card); color: var(--text-primary); font-family: var(--font-sans);"
      />
    </div>
    <textarea
      v-model="content" rows="3" placeholder="写下你的想法..."
      class="w-full px-3 py-2 text-sm outline-none resize-none transition-all"
      style="border: 1px solid var(--border); background: var(--bg-card); color: var(--text-primary); font-family: var(--font-sans);"
      :style="{ borderColor: error ? 'var(--accent-red)' : 'var(--border)' }"
    />
    <p v-if="error" class="text-xs" style="color: var(--accent-red); font-family: var(--font-sans);">{{ error }}</p>
    <button
      type="submit"
      class="px-6 py-2 text-sm font-bold text-white transition-all hover:scale-105 active:scale-95"
      style="font-family: var(--font-display); background: var(--accent); border: none; cursor: pointer; font-weight: 400;"
    >
      发送留言
    </button>
  </form>
</template>
