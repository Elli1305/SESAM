import { createRouter, createWebHistory } from 'vue-router'
import StartView from '../views/Start.vue'
import SignUp from "@/main/vue/views/SignUp.vue";
import LoginView from '../views/Login.vue'
import CurrentUserList from "@/main/vue/views/CurrentUserList.vue";

const router = createRouter({
  history: createWebHistory(),
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
    },
    {
      path: '/admin/currentuserlist',
      name: 'currentuserlist',
      component: CurrentUserList
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
