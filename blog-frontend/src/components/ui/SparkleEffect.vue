<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

interface Particle {
  id: number
  x: number
  y: number
  char: string
  delay: number
  duration: number
}

const particles = ref<Particle[]>([])
let timer: number | null = null

const chars = ['✦', '♪', '♫', '★', '✿', '📷', '✨', '🌿']

function spawnParticle() {
  const id = Date.now()
  const p: Particle = {
    id,
    x: Math.random() * 100,
    y: Math.random() * 80,
    char: chars[Math.floor(Math.random() * chars.length)],
    delay: 0,
    duration: 6 + Math.random() * 8,
  }
  particles.value = [...particles.value.slice(-6), p]
  setTimeout(() => {
    particles.value = particles.value.filter(p => p.id !== id)
  }, p.duration * 1000)
}

onMounted(() => {
  spawnParticle()
  timer = window.setInterval(spawnParticle, 5000)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<template>
  <div class="fixed inset-0 pointer-events-none z-0 overflow-hidden">
    <div
      v-for="p in particles"
      :key="p.id"
      class="absolute text-sm pointer-events-none select-none"
      :style="{
        left: p.x + '%',
        top: p.y + '%',
        animation: `float ${p.duration}s ease-in-out infinite, sparkle ${p.duration * 0.3}s ease-in-out infinite`,
        animationDelay: p.delay + 's',
        opacity: 0.3,
      }"
    >{{ p.char }}</div>
  </div>
</template>
