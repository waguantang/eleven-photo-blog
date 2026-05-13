import type { Directive } from 'vue'

const prefersReduced = typeof window !== 'undefined' && window.matchMedia('(prefers-reduced-motion: reduce)').matches

let observer: IntersectionObserver | null = null

function getObserver() {
  if (!observer) {
    observer = new IntersectionObserver(
      (entries) => {
        for (const entry of entries) {
          if (entry.isIntersecting) {
            const el = entry.target as HTMLElement
            el.classList.add('is-visible')
            observer!.unobserve(el)
          }
        }
      },
      { rootMargin: '0px 0px -40px 0px', threshold: 0.1 }
    )
  }
  return observer
}

export const vMotion: Directive<HTMLElement, string | undefined> = {
  mounted(el) {
    if (prefersReduced) {
      el.style.opacity = '1'
      el.style.transform = 'none'
      return
    }
    el.classList.add('motion-item')
    getObserver().observe(el)
  },
  unmounted(el) {
    getObserver().unobserve(el)
  },
}
