import { createRouter, createWebHistory } from 'vue-router'
import StartView from '../views/Start.vue'
import SignUp from "@/main/vue/views/SignUp.vue";
import LoginView from '../views/Login.vue'
import NavigationTree from "@/main/vue/views/NavigationTree.vue";
import Floorplan from "@/main/vue/views/Floorplan.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: StartView,
      children: [
        {path: "", component: Floorplan}
      ]
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
