import { ref, onMounted, onUnmounted, type Ref } from 'vue'

const prefersReduced = typeof window !== 'undefined' && window.matchMedia('(prefers-reduced-motion: reduce)').matches

let observer: IntersectionObserver | null = null
const elementMap = new WeakMap<Element, () => void>()

function getObserver() {
  if (!observer) {
    observer = new IntersectionObserver(
      (entries) => {
        for (const entry of entries) {
          if (entry.isIntersecting) {
            const cb = elementMap.get(entry.target)
            if (cb) cb()
            observer!.unobserve(entry.target)
            elementMap.delete(entry.target)
          }
        }
      },
      { rootMargin: '0px 0px -40px 0px', threshold: 0.1 }
    )
  }
  return observer
}

/**
 * Scroll-triggered reveal. Returns isVisible ref that flips to true when the element enters viewport.
 * Respects prefers-reduced-motion: immediately sets isVisible = true.
 */
export function useScrollReveal(target: Ref<Element | null>, _options?: { once?: boolean }) {
  const isVisible = ref(prefersReduced)

  onMounted(() => {
    if (!target.value || prefersReduced) {
      isVisible.value = true
      return
    }
    const el = target.value
    elementMap.set(el, () => {
      isVisible.value = true
    })
    getObserver().observe(el)
  })

  onUnmounted(() => {
    if (target.value && elementMap.has(target.value)) {
      elementMap.delete(target.value)
      getObserver().unobserve(target.value)
    }
  })

  return { isVisible }
}

/**
 * Parallax transform based on scroll position.
 * Speed: 0.1 (slow) to 0.5 (fast). Returns a style object for :style binding.
 */
export function useParallax(speed: number = 0.3) {
  const offset = ref(0)
  let ticking = false

  function onScroll() {
    if (!ticking) {
      requestAnimationFrame(() => {
        offset.value = window.scrollY * speed
        ticking = false
      })
      ticking = true
    }
  }

  onMounted(() => {
    if (prefersReduced) return
    window.addEventListener('scroll', onScroll, { passive: true })
  })

  onUnmounted(() => {
    window.removeEventListener('scroll', onScroll)
  })

  return { offset }
}

/**
 * Returns a style object for a scroll-triggered reveal with optional stagger index.
 */
export function motionStyle(index?: number) {
  const delay = index != null ? `${index * 0.06}s` : '0s'
  return {
    opacity: '0',
    transform: 'translateY(24px)',
    transition: `opacity 0.6s cubic-bezier(0.16, 1, 0.3, 1) ${delay}, transform 0.6s cubic-bezier(0.16, 1, 0.3, 1) ${delay}`,
    willChange: 'transform, opacity',
  } as Record<string, string>
}

export { prefersReduced }
