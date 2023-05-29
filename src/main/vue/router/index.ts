import {createRouter, createWebHistory} from 'vue-router'
import StartView from '../views/Start.vue'
import SignUp from "@/main/vue/views/SignUp.vue";
import PasswordChange from "../views/PasswortChange.vue";
import PasswordReset from "../views/PasswordReset.vue";
import Profilansicht from '@/main/vue/views/Profilansicht.vue'
import LoginView from '../views/Login.vue'
import CurrentUserList from "@/main/vue/views/CurrentUserList.vue";
import FloorPlan from "@/main/vue/views/FloorPlan.vue";
import Imprint from "../views/Imprint.vue";
import ImprintEditor from "@/main/vue/views/ImprintEditor.vue";

import FloorPlanEdit from "@/main/vue/views/FloorPlanEdit.vue";
import {useUserStore} from "@/main/vue/stores/users";
import EditUser from "@/main/vue/views/EditUser.vue";
import CredentialView from "@/main/vue/views/CredentialView.vue";

const router = createRouter({
    history: createWebHistory(),

    routes: [
        {
            path: '/',
            name: 'home',
            component: StartView,
            children: [
                {
                    path: "",
                    component: FloorPlan,
                    name: "floorPlan"
                }
            ]
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView
        },
        {
            path: '/admin/currentuserlist/edit/:email',
            name: 'edit',
            component: EditUser,
            props: true,
        },
        {
            path: '/admin/currentuserlist',
            name: 'currentuserlist',
            component: CurrentUserList,
            //meta: {requiresAdmin: true}
        },
        {
            path: '/editFloorPlan',
            component: FloorPlanEdit
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
            path: "/imprint",
            component: Imprint,
        },
        {
            path: "/imprinteditor",
            component: ImprintEditor,
            meta: {requiresAdmin: true},
        },
        {
            path: '/credentialview',
            component: CredentialView
        }

    ],
});


router.beforeEach((to, from, next) => {
    const {authenticated} = useUserStore();

    if (to.meta.requiresAuth && !authenticated) {
        return {
            path: "/login",
            query: {redirect: to.fullPath},
        };
    }

    if (to.path === "/ImprintEditor") {
        return next("/imprinteditor");
    }
    next();
});


export default router