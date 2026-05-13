<script setup lang="ts">
import type { Comment } from '../../api/comment'

defineProps<{ comment: Comment }>()
</script>

<template>
  <div class="comment-card">
    <div class="flex items-start gap-3">
      <!-- Avatar — manga style initials -->
      <div class="w-9 h-9 flex items-center justify-center text-sm font-bold text-white flex-shrink-0"
           style="font-family: var(--font-display); background: var(--accent); font-size: 0.75rem;">
        {{ comment.authorName.charAt(0) }}
      </div>

      <div class="flex-1 min-w-0">
        <div class="flex items-center gap-2 mb-1">
          <span class="text-sm font-bold" style="font-family: var(--font-display); font-weight: 400; color: var(--text-primary);">
            {{ comment.authorName }}
          </span>
          <span class="text-xs" style="font-family: var(--font-mono); color: var(--ink-light); font-size: 0.6rem;">
            {{ new Date(comment.createdAt).toLocaleDateString('zh-CN') }}
          </span>
        </div>
        <p class="text-sm leading-relaxed" style="color: var(--text-primary); font-family: var(--font-sans); font-size: 0.85rem;">
          {{ comment.content }}
        </p>
      </div>
    </div>

    <!-- Replies — nested with left border accent -->
    <div v-if="comment.replies?.length" class="mt-3 pt-3 space-y-3" style="border-top: 1px dashed var(--border-light);">
      <div class="ml-8 pl-4" style="border-left: 2px solid var(--accent-blue);">
        <CommentItem v-for="reply in comment.replies" :key="reply.id" :comment="reply" />
      </div>
    </div>
  </div>
</template>
