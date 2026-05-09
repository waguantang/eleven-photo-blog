import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { Howl } from 'howler'

interface Track {
  id: string
  title: string
  artist: string
  url: string
  cover?: string
  duration?: number
}

export const useMusicStore = defineStore('music', () => {
  const playlist = ref<Track[]>([])
  const currentIndex = ref(-1)
  const isPlaying = ref(false)
  const isExpanded = ref(false)
  const currentTime = ref(0)
  const duration = ref(0)
  const volume = ref(0.7)
  let howl: Howl | null = null
  let progressInterval: number | null = null

  const currentTrack = computed(() => {
    if (currentIndex.value >= 0 && currentIndex.value < playlist.value.length) {
      return playlist.value[currentIndex.value]
    }
    return null
  })

  const progress = computed(() => {
    if (duration.value === 0) return 0
    return (currentTime.value / duration.value) * 100
  })

  function loadPlaylist() {
    // 轻松的摄影背景音乐
    playlist.value = [
      { id: '1', title: '安静的午后', artist: 'Lofi Study', url: '' },
      { id: '2', title: '咖啡馆小调', artist: 'Jazz Cafe', url: '' },
      { id: '3', title: '阳光下的街道', artist: 'Acoustic Vibes', url: '' },
      { id: '4', title: '黄昏时分', artist: 'Chill Evening', url: '' },
    ]
  }

  function play(index: number) {
    if (index < 0 || index >= playlist.value.length) return
    currentIndex.value = index
    howl?.unload()
    const track = playlist.value[index]
    if (!track.url) {
      isPlaying.value = true
      duration.value = track.duration || 240
      startProgressSimulation()
      return
    }
    howl = new Howl({
      src: [track.url],
      html5: true,
      volume: volume.value,
      onplay: () => {
        isPlaying.value = true
        duration.value = howl!.duration()
        startProgressUpdate()
      },
      onend: () => { next() },
      onloaderror: () => { console.warn('Failed to load audio:', track.url) },
    })
    howl.play()
  }

  function startProgressSimulation() {
    stopProgressUpdate()
    currentTime.value = 0
    progressInterval = window.setInterval(() => {
      if (!isPlaying.value) return
      currentTime.value += 1
      if (currentTime.value >= duration.value) {
        currentTime.value = 0
      }
    }, 1000)
  }

  function startProgressUpdate() {
    stopProgressUpdate()
    progressInterval = window.setInterval(() => {
      if (howl && howl.playing()) {
        currentTime.value = howl.seek() as number
      }
    }, 500)
  }

  function stopProgressUpdate() {
    if (progressInterval) { clearInterval(progressInterval); progressInterval = null }
  }

  function togglePlay() {
    if (currentIndex.value < 0 && playlist.value.length > 0) {
      play(0)
      return
    }
    if (howl) {
      if (howl.playing()) { howl.pause(); isPlaying.value = false }
      else { howl.play(); isPlaying.value = true }
    } else {
      isPlaying.value = !isPlaying.value
    }
    if (isPlaying.value && !howl) startProgressSimulation()
    else if (!isPlaying.value) stopProgressUpdate()
  }

  function next() { if (playlist.value.length > 0) play((currentIndex.value + 1) % playlist.value.length) }
  function prev() { if (playlist.value.length > 0) play((currentIndex.value - 1 + playlist.value.length) % playlist.value.length) }
  function setVolume(v: number) { volume.value = v; howl?.volume(v) }
  function toggleExpand() { isExpanded.value = !isExpanded.value }
  function seek(time: number) {
    if (howl) { howl.seek(time); currentTime.value = time }
    else { currentTime.value = time }
  }

  loadPlaylist()

  return {
    playlist, currentIndex, isPlaying, isExpanded, currentTime, duration, volume, progress,
    currentTrack, play, togglePlay, next, prev, setVolume, toggleExpand, seek, loadPlaylist,
  }
})
