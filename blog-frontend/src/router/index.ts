import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/Home.vue'),
    },
    {
      path: '/post/:slug',
      name: 'post',
      component: () => import('../views/PostDetail.vue'),
    },
    {
      path: '/archive',
      name: 'archive',
      component: () => import('../views/Archive.vue'),
    },
    {
      path: '/portfolio',
      name: 'portfolio',
      component: () => import('../views/Portfolio.vue'),
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/About.vue'),
    },
    {
      path: '/category/:slug',
      name: 'category',
      component: () => import('../views/CategoryPosts.vue'),
    },
    {
      path: '/tag/:slug',
      name: 'tag',
      component: () => import('../views/TagPosts.vue'),
    },
    {
      path: '/admin/login',
      name: 'admin-login',
      component: () => import('../views/admin/AdminLogin.vue'),
    },
    {
      path: '/admin',
      name: 'admin',
      component: () => import('../views/admin/AdminDashboard.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/admin/posts',
      name: 'admin-posts',
      component: () => import('../views/admin/AdminPosts.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/admin/posts/new',
      name: 'admin-post-new',
      component: () => import('../views/admin/PostEditor.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/admin/posts/:id/edit',
      name: 'admin-post-edit',
      component: () => import('../views/admin/PostEditor.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/admin/comments',
      name: 'admin-comments',
      component: () => import('../views/admin/AdminComments.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/admin/categories',
      name: 'admin-categories',
      component: () => import('../views/admin/AdminCategories.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/admin/tags',
      name: 'admin-tags',
      component: () => import('../views/admin/AdminTags.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('../views/NotFound.vue'),
    },
  ],
})

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    const authStore = useAuthStore()
    if (!authStore.isLoggedIn) {
      next({ name: 'admin-login', query: { redirect: to.fullPath } })
      return
    }
  }
  next()
})

export default router
