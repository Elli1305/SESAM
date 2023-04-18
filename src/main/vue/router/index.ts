import { createRouter, createWebHistory } from 'vue-router'
import StartView from '../views/Start.vue'
import Profilansicht from "@/main/vue/views/Profilansicht.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: StartView },
    { path: '/profile', name: 'Profile', component: Profilansicht }
  ]
})

router.beforeEach((to) => {
  // Something which should be executed before each routing
})

export default router
