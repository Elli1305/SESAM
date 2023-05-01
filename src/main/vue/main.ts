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
        home: {header: "SESAM(Ger)"},
        adminCurrentUser: {headline: "Aktuelle Nutzer", roles:{ADMINISTRATOR: "Administrator", EDITOR:"Bearbeiter", ISSUER:"Herausgeber", prename:"Vorname", showAdmin:"zeige Admin"}}

    },
    en: {
        home: {header: "SESAM(En)"},
        adminCurrentUser: {headline: "Current Users", roles:{ADMINISTRATOR: "Admin", EDITOR:"Editor", ISSUER:"Issuer", prename:"Prename", showAdmin: "Show Admin"}}
    }
}


const i18n = createI18n({
    legacy: false,
    locale: 'de',
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
