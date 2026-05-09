<script setup lang="ts">
import { useMusicStore } from '../../stores/music'

const music = useMusicStore()

function formatTime(s: number): string {
  const m = Math.floor(s / 60)
  const sec = Math.floor(s % 60)
  return `${m}:${sec.toString().padStart(2, '0')}`
}
</script>

<template>
  <div
    class="fixed bottom-0 left-0 right-0 z-50 transition-all duration-300"
    style="background-color: color-mix(in srgb, #1A1714 94%, transparent); backdrop-filter: blur(20px); border-top: 2px solid var(--border);"
  >
    <!-- ═══ Collapsed: Cassette spine ═══ -->
    <div
      class="h-12 flex items-center justify-between px-4 max-w-5xl mx-auto cursor-pointer"
      @click="music.toggleExpand()"
    >
      <!-- Left: tape label -->
      <div class="flex items-center gap-3 min-w-0 flex-1">
        <div class="flex-shrink-0 relative w-7 h-7 flex items-center justify-center"
             style="border: 1.5px solid color-mix(in srgb, var(--accent) 40%, transparent); border-radius: 1px;">
          <div
            class="w-3.5 h-3.5 rounded-full transition-all"
            :class="{ 'animate-spin': music.isPlaying }"
            style="background: conic-gradient(var(--accent) 0deg, var(--accent-gold) 120deg, var(--accent) 240deg, var(--accent-gold) 360deg); animation-duration: 3s;"
          />
        </div>
        <div class="min-w-0">
          <p class="text-xs font-medium truncate" style="color: var(--text-primary); font-family: var(--font-display); font-size: 0.8rem;">
            {{ music.currentTrack?.title || 'NO TAPE' }}
          </p>
          <p class="text-xs truncate opacity-50" style="font-family: var(--font-mono); font-size: 0.6rem;">
            {{ music.currentTrack?.artist || 'EMPTY' }}
          </p>
        </div>
      </div>

      <!-- Center: mini equalizer -->
      <div class="hidden sm:flex items-end gap-px mx-3 h-4">
        <div
          v-for="i in 5" :key="i"
          class="w-0.5 transition-all"
          style="background-color: var(--accent); border-radius: 1px;"
          :style="{ height: music.isPlaying ? (3 + Math.random() * 13) + 'px' : '2px', opacity: music.isPlaying ? 1 : 0.3 }"
        />
      </div>

      <!-- Right: controls -->
      <div class="flex items-center gap-1.5 ml-3" @click.stop>
        <button @click="music.prev()" class="text-xs px-1 py-0.5 hover:text-red-400 transition-colors"
                style="color: var(--text-secondary); font-family: var(--font-display);">&#x23EE;</button>
        <button
          @click="music.togglePlay()"
          class="text-lg px-1.5 py-0.5 transition-all hover:scale-110"
          style="color: var(--accent); font-family: var(--font-display);"
        >{{ music.isPlaying ? '&#x23F8;' : '&#x25B6;' }}</button>
        <button @click="music.next()" class="text-xs px-1 py-0.5 hover:text-red-400 transition-colors"
                style="color: var(--text-secondary); font-family: var(--font-display);">&#x23ED;</button>
        <span class="text-xs ml-1 transition-transform" style="color: var(--text-secondary); font-size: 0.6rem;"
              :style="{ transform: music.isExpanded ? 'rotate(180deg)' : '' }">&#x25B2;</span>
      </div>
    </div>

    <!-- ═══ Expanded: Cassette face ═══ -->
    <div
      v-if="music.isExpanded"
      class="border-t px-4 py-5 max-w-2xl mx-auto"
      style="border-color: var(--border);"
    >
      <!-- Cassette body -->
      <div class="relative mx-auto mb-5" style="max-width: 340px;">
        <!-- Cassette shell -->
        <div class="relative rounded-md overflow-hidden"
             style="background: linear-gradient(145deg, #2D2A26 0%, #1F1C19 50%, #262320 100%); border: 1px solid #3D3830; padding: 18px 16px 14px; box-shadow: 0 8px 30px rgba(0,0,0,0.4), inset 0 1px 0 rgba(255,255,255,0.03);">

          <!-- Top label area -->
          <div class="text-center mb-3 pb-2" style="border-bottom: 1px dashed rgba(255,255,255,0.08);">
            <p class="text-xs font-bold tracking-wider uppercase" style="font-family: var(--font-display); color: var(--accent); font-size: 0.7rem; letter-spacing: 0.1em;">
              {{ music.currentTrack?.title || 'SIDE A' }}
            </p>
            <p class="text-xs opacity-40" style="font-family: var(--font-mono); font-size: 0.55rem;">
              {{ music.currentTrack?.artist || "Eleven's Mix" }}
            </p>
          </div>

          <!-- Reel windows row -->
          <div class="flex items-center justify-between px-4 mb-3">
            <!-- Left reel -->
            <div class="relative w-16 h-16 flex items-center justify-center"
                 style="background: radial-gradient(circle, rgba(0,0,0,0.5) 60%, rgba(255,255,255,0.03) 62%, rgba(0,0,0,0.3) 65%, rgba(255,255,255,0.02) 70%, #1A1714 72%); border-radius: 50%; border: 1px solid #3D3830;">
              <div
                class="w-10 h-10 flex items-center justify-center"
                :class="music.isPlaying ? 'animate-spin' : ''"
                style="animation-duration: 3s; animation-timing-function: linear;"
              >
                <div class="w-8 h-8 rounded-full"
                     style="background: repeating-conic-gradient(rgba(255,255,255,0.06) 0% 2%, transparent 2% 6%);" />
                <div class="absolute w-2 h-2 rounded-full" style="background: #1A1714; border: 1px solid #3D3830;" />
              </div>
            </div>

            <!-- Tape path between reels -->
            <div class="flex-1 flex items-center mx-2" style="height: 2px;">
              <div style="flex:1; height:1px; background: rgba(255,255,255,0.06);" />
              <div style="width:6px; height:6px; background: var(--accent); border-radius:50%; opacity:0.5;" />
              <div style="flex:1; height:1px; background: rgba(255,255,255,0.06);" />
            </div>

            <!-- Right reel -->
            <div class="relative w-16 h-16 flex items-center justify-center"
                 style="background: radial-gradient(circle, rgba(0,0,0,0.5) 60%, rgba(255,255,255,0.03) 62%, rgba(0,0,0,0.3) 65%, rgba(255,255,255,0.02) 70%, #1A1714 72%); border-radius: 50%; border: 1px solid #3D3830;">
              <div
                class="w-10 h-10 flex items-center justify-center"
                :class="music.isPlaying ? 'animate-spin' : ''"
                style="animation-duration: 3s; animation-timing-function: linear;"
              >
                <div class="w-8 h-8 rounded-full"
                     style="background: repeating-conic-gradient(rgba(255,255,255,0.06) 0% 2%, transparent 2% 6%);" />
                <div class="absolute w-2 h-2 rounded-full" style="background: #1A1714; border: 1px solid #3D3830;" />
              </div>
            </div>
          </div>

          <!-- Bottom: tape head area -->
          <div class="flex justify-center gap-1.5 mt-1">
            <div class="w-10 h-3 rounded-sm" style="background: rgba(255,255,255,0.03); border: 1px solid rgba(255,255,255,0.05);" />
            <div class="w-4 h-3 rounded-sm" style="background: rgba(255,255,255,0.02); border: 1px solid rgba(255,255,255,0.03);" />
            <div class="w-10 h-3 rounded-sm" style="background: rgba(255,255,255,0.03); border: 1px solid rgba(255,255,255,0.05);" />
          </div>

          <!-- Screw holes -->
          <div v-for="corner in ['top-1.5 left-1.5', 'top-1.5 right-1.5', 'bottom-1.5 left-1.5', 'bottom-1.5 right-1.5']"
               :key="corner"
               class="absolute w-1.5 h-1.5 rounded-full"
               :class="corner"
               style="background: rgba(0,0,0,0.3); border: 1px solid rgba(255,255,255,0.04);" />
        </div>
      </div>

      <!-- Progress bar -->
      <div class="mb-4 max-w-xs mx-auto">
        <div class="flex items-center gap-2 text-xs" style="font-family: var(--font-mono); font-size: 0.6rem; color: var(--text-secondary);">
          <span>{{ formatTime(music.currentTime) }}</span>
          <div
            class="flex-1 h-1.5 cursor-pointer relative group"
            style="background-color: color-mix(in srgb, var(--accent) 12%, transparent);"
            @click="(e: MouseEvent) => { const rect = (e.target as HTMLElement).getBoundingClientRect(); const pct = (e.clientX - rect.left) / rect.width; music.seek(pct * music.duration) }"
          >
            <div
              class="absolute inset-y-0 left-0 transition-all"
              style="background-color: var(--accent);"
              :style="{ width: music.progress + '%' }"
            />
            <div
              class="absolute top-1/2 -translate-y-1/2 w-2.5 h-2.5 rounded-full opacity-0 group-hover:opacity-100 transition-opacity"
              style="background-color: var(--accent);"
              :style="{ left: `calc(${music.progress}% - 5px)` }"
            />
          </div>
          <span>{{ formatTime(music.duration) }}</span>
        </div>
      </div>

      <!-- Volume knob -->
      <div class="flex items-center justify-center gap-2 mb-5">
        <span class="text-xs opacity-30" style="font-size: 0.55rem;">MIN</span>
        <input
          type="range" min="0" max="1" step="0.05" :value="music.volume"
          @input="(e: Event) => music.setVolume(parseFloat((e.target as HTMLInputElement).value))"
          class="w-24 h-1 appearance-none cursor-pointer"
          style="accent-color: var(--accent); background: color-mix(in srgb, var(--accent) 15%, transparent); border-radius: 2px;"
        />
        <span class="text-xs opacity-30" style="font-size: 0.55rem;">MAX</span>
      </div>

      <!-- J-Card playlist -->
      <div class="relative rounded-sm overflow-hidden"
           style="background-color: color-mix(in srgb, var(--bg-card) 50%, transparent); border: 1px solid var(--border);">
        <!-- J-card fold line -->
        <div class="h-5 flex items-center px-3" style="border-bottom: 1px dashed var(--border);">
          <span class="text-xs font-bold tracking-wider uppercase" style="font-family: var(--font-display); color: var(--text-secondary); font-size: 0.55rem;">
            J-CARD &mdash; SIDE A TRACKLIST
          </span>
        </div>
        <div
          v-for="(track, i) in music.playlist"
          :key="track.id"
          class="flex items-center justify-between px-3 py-2.5 cursor-pointer transition-colors text-sm group"
          :style="i === music.currentIndex
            ? { backgroundColor: 'color-mix(in srgb, var(--accent) 10%, transparent)', borderLeft: '3px solid var(--accent)' }
            : { borderLeft: '3px solid transparent' }"
          @click="music.play(i)"
        >
          <div class="flex items-center gap-2.5 min-w-0">
            <span class="text-xs flex-shrink-0"
                  style="font-family: var(--font-mono); font-size: 0.65rem;"
                  :style="{ color: i === music.currentIndex ? 'var(--accent)' : 'var(--text-secondary)' }">
              {{ i === music.currentIndex && music.isPlaying ? '&#x25B6;' : String(i + 1).padStart(2, '0') }}
            </span>
            <span class="truncate text-sm"
                  :style="{ fontFamily: i === music.currentIndex ? 'var(--font-display)' : 'inherit', color: i === music.currentIndex ? 'var(--accent)' : 'var(--text-primary)', fontSize: '0.8rem' }">
              {{ track.title }}
            </span>
          </div>
          <span class="text-xs flex-shrink-0 ml-2 opacity-40" style="font-size: 0.65rem;">
            {{ track.artist }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>
