import { createRouter, createWebHistory } from 'vue-router'
import type { User } from '@/types'
import HomeView from '../views/HomeView.vue'
import NewsDetailView from '../views/NewsDetailView.vue'
import FactView from '../views/FactView.vue'
import FakeView from '../views/FakeView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterView
  },
  {
    path: '/fact',
    name: 'fact',
    component: FactView
  },
  {
    path: '/fake',
    name: 'fake',
    component: FakeView
  },
  {
    path: '/news/:id',
    name: 'news-detail',
    component: NewsDetailView,
    props: true
  },
  {
    path: '/admin',
    name: 'admin-dashboard',
    component: () => import('../views/AdminDashboardView.vue'),
    meta: { requiresAdmin: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Global auth guard: only allow access to login/register when not authenticated
router.beforeEach((to) => {
  const publicPaths = ['/login', '/register']
  const isPublic = publicPaths.includes(to.path)
  const rawUser = localStorage.getItem('user')
  let parsedUser: User | null = null

  if (rawUser) {
    try {
      parsedUser = JSON.parse(rawUser) as User
    } catch (error) {
      console.warn('Unable to parse stored user', error)
      localStorage.removeItem('user')
      localStorage.removeItem('accessToken')
      localStorage.removeItem('refreshToken')
    }
  }

  // If not logged in and trying to access a protected route, redirect to login
  if (!parsedUser && !isPublic) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }

  // If logged in and going to login/register, redirect to home
  if (parsedUser && isPublic) {
    return { path: '/' }
  }

  if (to.meta.requiresAdmin) {
    if (!parsedUser || !parsedUser.roles?.includes('ADMIN')) {
      return { path: '/', replace: true }
    }
  }

  return true
})

export default router
