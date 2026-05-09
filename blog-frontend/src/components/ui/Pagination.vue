<script setup lang="ts">
defineProps<{ page: number; total: number; size: number }>()
defineEmits<{ (e: 'change', page: number): void }>()

function pages(current: number, total: number, size: number): number[] {
  const count = Math.ceil(total / size)
  if (count <= 5) return Array.from({ length: count }, (_, i) => i + 1)
  const result: number[] = []
  for (let i = 1; i <= count; i++) {
    if (i === 1 || i === count || (i >= current - 1 && i <= current + 1)) {
      result.push(i)
    } else if (result[result.length - 1] !== -1) {
      result.push(-1)
    }
  }
  return result
}
</script>

<template>
  <div v-if="total > size" class="flex justify-center items-center gap-1 mt-10">
    <button
      v-for="p in pages(page, total, size)"
      :key="p"
      :disabled="p === -1"
      class="min-w-[34px] h-9 px-2 text-sm transition-all"
      :style="p === page
        ? {
            backgroundColor: 'var(--accent)',
            color: '#fff',
            fontFamily: 'var(--font-display)',
          }
        : p === -1
          ? { color: 'var(--text-secondary)', cursor: 'default', fontFamily: 'var(--font-mono)' }
          : {
              color: 'var(--text-secondary)',
              backgroundColor: 'var(--bg-secondary)',
              fontFamily: 'var(--font-mono)',
            }"
      @click="p !== -1 && $emit('change', p)"
    >{{ p === -1 ? '·' : p }}</button>
  </div>
</template>
