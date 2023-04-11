import { createRouter, createWebHistory } from 'vue-router'
import StartView from '../views/Start.vue'
import LoginView from '../views/Login.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: StartView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    }
  ]
})

/*router.beforeEach((to) => {
  // Something which should be executed before each routing
})
*/
export default router
