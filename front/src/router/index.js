import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/searchApart',
      name: 'searchApart',
      component: () => import('../views/SearchApartView.vue'),
    },
    {
      path: '/reviews',
      children: [
        {
          path: 'list',
          name: 'list',
          component: () => import('../views/ReviewList.vue'),
        },
        {
          path: 'regist',
          name: 'regist',
          component: () => import('../views/ReviewRegistForm.vue'),
        }
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
    },
    {
      path: '/signup',
      name: 'signup',
      component: () => import('../views/SignupView.vue'),
    },
  ],
})

export default router
