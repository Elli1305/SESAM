import { createRouter, createWebHistory } from 'vue-router'
import StartView from '../views/Start.vue'
import Profilansicht from "@/main/vue/views/Profilansicht.vue";
import SignUp from "@/main/vue/views/SignUp.vue";
import LoginView from '../views/Login.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: StartView
    },
    {
      path: '/profile',
      name: 'profile',
      component: Profilansicht
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/signup',
      component: SignUp
    }
  ]
})

/*router.beforeEach((to) => {
  // Something which should be executed before each routing
})
*/
export default router
