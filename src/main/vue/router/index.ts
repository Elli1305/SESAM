import {createRouter, createWebHistory} from "vue-router";
import StartView from "../views/Start.vue";
import SignUp from "@/main/vue/views/SignUp.vue";
import PasswordChange from "../views/PasswortChange.vue";
import PasswordReset from "../views/PasswordReset.vue";
import Profilansicht from "@/main/vue/views/Profilansicht.vue";
import LoginView from "../views/Login.vue";
import CurrentUserList from "@/main/vue/views/CurrentUserList.vue";
import FloorPlan from "@/main/vue/views/FloorPlan.vue";
import IssueCredential from "../views/IssueCredential.vue";
import IssueCredentials from "../views/IssueCredentials.vue";
import Credentialview from "@/main/vue/views/CredentialView.vue"

import Imprint from "../views/Imprint.vue";
import ImprintEditor from "@/main/vue/views/ImprintEditor.vue";
import EditUser from "@/main/vue/views/EditUser.vue";
import CorporateDesign from "@/main/vue/views/CorporateDesign.vue";
import {useUserStore} from "@/main/vue/stores/users";
import RequestRolles from "@/main/vue/views/RolesRequest.vue";
import CredentialAdministration from "@/main/vue/views/CredentialAdministration.vue";
import CredentialEditing from "@/main/vue/views/CredentialEditing.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: "/",
            name: "home",
      component: StartView,
      children: [{ path: "", component: FloorPlan ,
                    name: "floorPlan"}],
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
        path: '/admin/rolesRequest',
        name: 'rolesRequest',
        component: RequestRolles,
        //meta: {requiresAdmin: true}
      },
    {
      path: '/admin/currentuserlist/edit/:email',
      name: 'edit',
      component: EditUser,
      props: true,
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
      path: "/imprint",
      component: Imprint,
    },
    {
      path: "/imprinteditor",
      component: ImprintEditor,
      meta: {requiresAdmin: true},
    },
    {
      path: "/credentialview",
      component: Credentialview,
    },
    {
      path: "/credentials",
      component: IssueCredentials,
      props: true,
      meta: { requiresAuth: true },
    },
    {
      path: "/credentials/:id/issue",
      component: IssueCredential,
      props: true,
      meta: { requiresAuth: true },
    },
    {
      path: "/corporatedesign",
      component: CorporateDesign,
      meta: {requiresAdmin: true},
    },
    {
      path: "/credential_administration",
      component: CredentialAdministration,
      meta: {
        requiresRoles: [
          'ADMINISTRATOR',
          'ISSUER'
        ]
      }
    },
    {
      path: "/credential_administration/:id(\\d+)",
      component: CredentialEditing,
      props: true,
      meta: {
        requiresRoles: [
          'ADMINISTRATOR',
        ]
      }
    },
    {
      path: "/add_credential",
      component: CredentialEditing,
      meta: {
        requiresRoles: [
          'ADMINISTRATOR',
        ]
      }
    }
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
