import {createRouter, createWebHistory} from "vue-router";

import StartView from '../views/Start.vue'
import FloorPlan from "@/main/vue/views/FloorPlan.vue";

import {AttainableRole} from "@/main/vue/entity/createUser";
import {useUserStore} from "@/main/vue/stores/users";

const router = createRouter({
    history: createWebHistory(),

    routes: [
        {
            path: "/",
            name: "home",
            component: StartView,
            children: [
                {
                    path: "",
                    component: FloorPlan,
                    name: "floorPlan",
                },
            ],
        },
        {
            path: "/login",
            name: "login",
            component: () => import("@/main/vue/views/Login.vue"),
        },
        {
            path: "/currentuserlist/edit/:email",
            name: "edit",
            component: () => import("@/main/vue/views/EditUser.vue"),
            props: true,
        },

        {
            path: "/rolesRequest",
            name: "rolesRequest",
            component: () => import("@/main/vue/views/RolesRequest.vue"),
            props: true,
        },

        {
            path: "/currentuserlist",
            name: "currentuserlist",
            component: () => import("@/main/vue/views/CurrentUserList.vue"),
            meta: {requiresAdmin: true},
        },
        {
            path: "/predefinedConfigs",
            name: "PredefinedConfigs",
            component: () => import("@/main/vue/views/PredefinedConfigEditor.vue"),
            meta: {
                authorize: AttainableRole.EDITOR,
            },
        },
        {
            path: "/grouprooms",
            name: "GroupRooms",
            component: () => import("@/main/vue/views/GroupRooms.vue"),
            meta: {
                authorize: AttainableRole.EDITOR,
            },
        },
        {
            path: "/editFloorPlan",
            component: () => import("@/main/vue/views/FloorPlanEdit.vue"),
            meta: {
                authorize: AttainableRole.EDITOR,
            },
        },
        {
            path: "/signup",
            component: () => import("@/main/vue/views/SignUp.vue"),
        },
        {
            path: "/profile",
            name: "profile",
            component: () => import("@/main/vue/views/ProfileView.vue"),
        },
        {
            path: "/passwordchange",
            component: () => import("@/main/vue/views/PasswordChange.vue"),
            props: (to) => ({token: to.query.token}),
            beforeEnter: (to, from, next) => {
                if (!to.query.token) {
                    next({path: "/"});
                    return;
                }

                next();
            }
        },
        {
            path: "/passwordreset",
            component: () => import("@/main/vue/views/PasswordReset.vue"),
        },
        {
            path: "/imprint",
            component: () => import("@/main/vue/views/Imprint.vue"),
        },
        {
            path: "/imprinteditor",
            component: () => import("@/main/vue/views/ImprintEditor.vue"),
            meta: {requiresAdmin: true},
        },
        {
            path: "/credentialview",
            component: () => import("@/main/vue/views/CredentialView.vue"),
        },
        {
            path: "/rolesRequest",
            component: () => import("@/main/vue/views/RolesRequest.vue"),
            meta: {requiresAdmin: true},
        },
        {
            path: "/credentials",
            component: () => import("@/main/vue/views/IssueCredentials.vue"),
            meta: {
                authorize: AttainableRole.ISSUER,
            },
        },
        {
            path: "/credentials/:id/issue",
            component: () => import("@/main/vue/views/IssueCredential.vue"),
            props: true,
            meta: {
                authorize: AttainableRole.ISSUER,
            },
        },
        {
            path: "/issuermanagement",
            component: () => import("@/main/vue/views/IssuerManagement.vue"),
            meta: {requiresAdmin: true},
        },
        {
            path: "/credentialmapping",
            component: () => import("@/main/vue/views/CredentialMappingList.vue"),
            meta: {requiresAdmin: true},
        },
        {
            path: "/corporatedesign",
            component: () => import("@/main/vue/views/CorporateDesign.vue"),
            meta: {requiresAdmin: true},
        },
        {
            path: "/credential_administration",
            component: () => import("@/main/vue/views/CredentialAdministration.vue"),
            meta: {
                authorize: AttainableRole.ADMINISTRATOR,
            },
        },
        {
            path: "/credential_administration/:type(internal|external)/:id(\\d+)",
            component: () => import("@/main/vue/views/CredentialEditing.vue"),
            props: true,
            meta: {
                authorize: AttainableRole.ADMINISTRATOR,
            },
        },
        {
            path: "/add_credential",
            component: () => import("@/main/vue/views/CredentialEditing.vue"),
            meta: {
                authorize: AttainableRole.ADMINISTRATOR,
            },
        },
        {
            path: "/import_credentials",
            component: () => import("@/main/vue/views/ImportCredentials.vue"),
            meta: {
                authorize: AttainableRole.ADMINISTRATOR,
            },
        },
        {path: "/:pathMatch(.*)*", component: StartView},
    ],
});

router.beforeEach((to, from, next) => {
    const {authenticated, user} = useUserStore();
    const {authorize} = to.meta;
    if (
        (to.meta.requiresAuth || authorize || to.meta.requiresAdmin) &&
        !authenticated
    ) {
        return next({path: "/login", query: {returnUrl: to.path}});
    } else if (
        authorize &&
        !user?.roles.some((role) => role.role === authorize && role.granted)
    ) {
        return next({path: "/"});
    }

    next();
});

export default router;
