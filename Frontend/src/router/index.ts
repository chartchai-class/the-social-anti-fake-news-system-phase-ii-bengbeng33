import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import NewsDetailView from '../views/NewsDetailView.vue'
import VoteView from '../views/VoteView.vue'
import CommentsView from '../views/CommentsView.vue'
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
    path: '/news/:id/vote',
    name: 'vote',
    component: VoteView,
    props: true
  },
  {
    path: '/news/:id/comments',
    name: 'comments',
    component: CommentsView,
    props: true
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router