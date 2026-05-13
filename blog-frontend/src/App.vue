<script setup lang="ts">
import { RouterView } from 'vue-router'
import NavBar from './components/layout/NavBar.vue'
import Footer from './components/layout/Footer.vue'
import MusicPlayer from './components/music/MusicPlayer.vue'
</script>

<template>
  <div class="min-h-screen flex flex-col" style="background-color: var(--bg-primary); color: var(--text-primary)">
    <NavBar />
    <main class="flex-1 relative">
      <RouterView v-slot="{ Component }">
        <transition name="page" mode="out-in">
          <component :is="Component" />
        </transition>
      </RouterView>
    </main>
    <Footer />
    <MusicPlayer />
  </div>
</template>

<style>
.page-enter-active,
.page-leave-active {
  transition: opacity 0.35s cubic-bezier(0.16, 1, 0.3, 1),
              transform 0.35s cubic-bezier(0.16, 1, 0.3, 1);
}
.page-enter-from {
  opacity: 0;
  transform: translateY(12px) scale(0.98);
}
.page-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}
</style>
