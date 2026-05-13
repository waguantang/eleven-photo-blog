<script setup lang="ts">
import MarkdownIt from 'markdown-it'
import hljs from 'highlight.js'
import 'highlight.js/styles/github-dark.css'

const props = defineProps<{ content: string }>()

const md = new MarkdownIt({
  html: false,
  linkify: true,
  breaks: true,
  highlight(str: string, lang: string) {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return hljs.highlight(str, { language: lang }).value
      } catch { /* fallback */ }
    }
    return ''
  },
})

const html = md.render(props.content)
</script>

<template>
  <div class="article-prose" v-html="html" />
</template>
