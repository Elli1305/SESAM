
import { createRouter, createWebHistory } from "vue-router";
import StartView from "../views/Start.vue";
import SignUp from "@/main/vue/views/SignUp.vue";
import LoginView from "../views/Login.vue";
import PasswordChange from "../views/PasswortChange.vue";
import PasswordReset from "../views/PasswordReset.vue";
import Profilansicht from '@/main/vue/views/Profilansicht.vue'

import Impressum from "../views/Impressum.vue";
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
      path: '/signup',
      component: SignUp
    },
    {
      path: '/profile',
      name: 'profile',
      component: Profilansicht
    },
    {
      path: '/passwordchange',
      component: PasswordChange,
      props: (to) => ({
        token: to.query.token,
      }),
    },
    {
      path: '/passwordreset',
      component: PasswordReset,

    },
    {
      path: "/Impressum",
      component: Impressum,
    },
  ],
});


/*router.beforeEach((to) => {
  // Something which should be executed before each routing
})
*/
export default router
