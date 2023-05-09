import { createRouter, createWebHistory } from "vue-router";
import StartView from "../views/Start.vue";
import SignUp from "@/main/vue/views/SignUp.vue";
import PasswordChange from "../views/PasswortChange.vue";
import PasswordReset from "../views/PasswordReset.vue";
import Profilansicht from "@/main/vue/views/Profilansicht.vue";
import LoginView from "../views/Login.vue";
import CurrentUserList from "@/main/vue/views/CurrentUserList.vue";
import Floorplan from "@/main/vue/views/Floorplan.vue";
import Impressum from "../views/Impressum.vue";
import IssueCredential from "../views/IssueCredential.vue";
import { useUserStore } from "../stores/users";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      name: "home",
      component: StartView,
      children: [{ path: "", component: Floorplan }],
    },
    {
      path: "/login",
      name: "login",
      component: LoginView,
    },
    {
      path: "/admin/currentuserlist",
      name: "currentuserlist",
      component: CurrentUserList,
      //meta: {requiresAdmin: true}
    },
    {
      path: "/signup",
      component: SignUp,
    },
    {
      path: "/profile",
      name: "profile",
      component: Profilansicht,
    },
    {
      path: "/passwordchange",
      component: PasswordChange,
      props: (to) => ({
        token: to.query.token,
      }),
    },
    {
      path: "/passwordreset",
      component: PasswordReset,
    },
    {
      path: "/Impressum",
      component: Impressum,
    },
    {
      path: "/issue_credential/:id",
      component: IssueCredential,
      props: true,
      meta: { requiresAuth: true },
    },
  ],
});

router.beforeEach((to) => {
  const { authenticated } = useUserStore();

  if (to.meta.requiresAuth && !authenticated) {
    return {
      path: "/login",
      query: { redirect: to.fullPath },
    };
  }
});

export default router;
