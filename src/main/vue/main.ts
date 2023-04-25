import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { Quasar } from 'quasar'
import quasarUserOptions from './quasar-user-options'
import {createI18n} from 'vue-i18n'

import App from './App.vue'
import router from './router'

// import store from "./stores";

import 'quasar/src/css/index.sass'
import '@quasar/extras/material-icons/material-icons.css'
import '@/main/vue/styles/notify.scss'
import axios from "axios";


const messages = {
    de: {
        home: {
            header: "SESAM(Ger)",
            signup: "Registrierung",
            signin: "Login",
            floorplan: "Floorplan",
            credentials: "Credentials",
            information: "Information",
            imprint: "Impressum",
            logout: "Logout"
        },
        profile: {
            title: "Profilansicht",
            firstname: "Vorname",
            lastname: "Name",
            email: "E-Mail",
            admin: "Admin",
            editor: "Bearbeiter",
            issuer: "Herausgeber"
        }
    },
    en: {
        home: {
            header: "SESAM(En)",
            signup: "Sign-up",
            signin: "Sign-in",
            floorplan: "Floorplan",
            credentials: "Credentials",
            information: "Information",
            imprint: "Imprint",
            logout: "Logout"
        },
        profile: {
            title: "Profile View",
            firstname: "First name",
            lastname: "Name",
            email: "E-mail",
            admin: "Admin",
            editor: "Editor",
            issuer: "Issuer"
        }
    }
}


const i18n = createI18n({
    legacy: false,
    locale: 'en',
    allowComposition: true,
    fallbackLocale: 'en',
    messages
})
const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(Quasar, quasarUserOptions)
app.use(i18n)
// app.use(store)

app.mount('#app')

axios.defaults.headers.post['Content-Type'] = 'application/json'
