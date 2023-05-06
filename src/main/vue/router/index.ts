import { createRouter, createWebHistory } from 'vue-router'
import StartView from '../views/Start.vue'
import SignUp from "@/main/vue/views/SignUp.vue";
import PasswordChange from "../views/PasswortChange.vue";
import PasswordReset from "../views/PasswordReset.vue";
import Profilansicht from '@/main/vue/views/Profilansicht.vue'
import LoginView from '../views/Login.vue'
import CurrentUserList from "@/main/vue/views/CurrentUserList.vue";
import FloorPlan from "@/main/vue/views/FloorPlan.vue";
import Impressum from "../views/Impressum.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: StartView,
      children: [
        {path: "", component: FloorPlan}
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/admin/currentuserlist',
      name: 'currentuserlist',
      component: CurrentUserList,
      //meta: {requiresAdmin: true}
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


// router.beforeEach((to) => {
//   // Something which should be executed before each routing
// })

export default router
