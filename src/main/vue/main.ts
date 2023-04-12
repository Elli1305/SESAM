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


const messages = {
    de: {
        home: {header: "SESAM(Ger)"}
    },
    en: {
        home: {header: "SESAM(En)"}
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
