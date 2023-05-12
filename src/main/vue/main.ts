import {createApp, watch} from 'vue'
import {createPinia} from 'pinia'
import {Quasar} from 'quasar'
import quasarUserOptions from './quasar-user-options'
import {createI18n} from 'vue-i18n'

import App from './App.vue'
import router from './router'

import 'quasar/src/css/index.sass'
import '@quasar/extras/material-icons/material-icons.css'
import '@/main/vue/styles/notify.scss'
import axios from "axios";

const messages = {
    de: {
        adminCurrentUser: {
            headline: "Aktuelle Nutzer",
            roles: {ADMINISTRATOR: "Administrator", EDITOR: "Bearbeiter", ISSUER: "Herausgeber"},
            prename: "Vorname",
            showAdmin: "Administratoren",
            showEditor: "Bearbeiter",
            showIssuer: "Herausgeber",
            search: "Suche"
        },
        adminEdit: {
            title: "Nutzer bearbeiten",
            changeRoles:"Rollen vergeben/entfernen: ",
            delete: "Löschen",
            back: "Zurück",
            attention: "ACHTUNG",
            question: "Nutzer wirklich löschen?",
            deleteOwnAccount: "Eigener Account kann nicht gelöscht werden",
            otherAdmin: "Bitte wenden Sie sich an einen anderen Administratoren",


        },

        common: {
            internalServerError: "Der Server konnte die Anfrage nicht verarbeiten",
            unkownError: "Ein unbekannter Fehler ist aufgetreten",
        },
        home: {
            header: "SESAM",
            signup: "Registrierung",
            signin: "Login",
            floorplan: "Floorplan",
            credentials: "Credentials",
            profileManagement: "Profilverwaltung",
            currentUsers: "Aktuelle Nutzer",
            currentRegistrations: "Aktuelle Registrierungen",
            issuerManagement: "Herausgeberverwaltung",
            corporateDesign: "Corporate Design",
            editCorporateDesign: "Corporate Design anpassen",
            editImprint: "Imprint bearbeiten",
            credentialManagement: "Credentialverwaltung",
            editorPages: "Bearbeiten",
            issuerPages: "Credential austellen",
            imprint: "Impressum",
            logout: "Logout"
        },
        login: {
            wrongEmailPassword:"Falsches Passwort oder Benutzername",
            loginFailed: "Login Fehlgeschlagen",
            forgotPassword: "Passwort vergessen:",
            resetPassword: "Passwort zurücksetzen",
            notRegistered: "Nicht registriert:",
            toRegister: "Zur Registrierung",
            password: "Passwort"
        },
        passwordReset: {
            resetPassword: "Passwort zurücksetzen",
            email: "E-Mail",
            emailSendFailed: "E-Mail versenden fehlgeschlagen",
            emailNonExistent: "Die E-Mail ist nicht existent",
            emailDoesNotConform: "E-Mail erfüllt nicht die Kriterien",
            positiveEmail: "E-Mail versenden war erfolgreich"
        },
        passwordChange: {
            changePassword: "Passwort ändern",
            password: "Passwort",
            passwordHint: "Das Passwort muss mind. 8 Zeichen lang sein, ein Sonderzeichen, eine Ziffer und einen Großbuchstaben beinhalten",
            repeatPassword: "Passwort wiederholen",
            failedToReset: "Password zurücksetzen fehlgeschlagen",
            passwordDoesNotConform: "Passwort erfüllt nicht die Kriterien",
            passwordsDontMatch: "Die Passwörter stimmen nicht überein",
            successfulReset: "Passwort zurücksetzen war erfolgreich"
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
        adminCurrentUser: {
            headline: "Current Users",
            roles: {ADMINISTRATOR: "Admin", EDITOR: "Editor", ISSUER: "Issuer"},
            prename: "Prename",
            showAdmin: "Show Admin",
            showEditor: "Show Editor",
            showIssuer: "Show Issuer",
            search: "Search"
        },
        adminEdit: {
            title: "Edit user",
            changeRoles:"Give/take Roles: ",
            delete: "Delete",
            back: "Back",
            attention: "Attention",
            question: "Do you really want to delete this user?"
        },
        common: {
            internalServerError: "The server could not process the request",
            unkownError: "An unknown error occured",
        },
        home: {
            header: "SESAM",
            signup: "Sign-up",
            signin: "Sign-in",
            floorplan: "Floorplan",
            credentials: "Credentials",
            profileManagement: "Profile Management",
            currentUsers: "Current Users",
            currentRegistrations: "Current Registrations",
            issuerManagement: "Issuer Management",
            corporateDesign: "Corporate Design",
            editCorporateDesign: "Edit Corporate Design",
            editImprint: "Edit Imprint",
            credentialManagement: "Credential Management",
            editorPages: "Edit",
            issuerPages: "Issue Credential",
            imprint: "Imprint",
            logout: "Logout"
        },
        login: {
            wrongEmailPassword:"Wrong Password oder Username",
            loginFailed: "Login Failed",
            forgotPassword: "Forgot Password:",
            resetPassword: "Reset Password",
            notRegistered: "Not registered",
            toRegister: "Go to Register",
            password: "Password"
        },
        passwordReset: {
            resetPassword: "Reset Password",
            email: "Email",
            emailSendFailed: "Failed to send email",
            emailNonExistent: "Email is non-existent",
            emailDoesNotConform: "Email does not match the required criteria",
            positiveEmail: "Email sent",
        },
        passwordChange: {
            changePassword: "Change Password",
            password: "Password",
            passwordHint:
                "The password must include min. 8 characters, a special character, a number, and a capital letter",
            repeatPassword: "Repeat password",
            failedToReset: "Failed to reset password",
            passwordDoesNotConform: "Password does not match the required criteria",
            passwordsDontMatch: "Passwords do not match",
            successfulReset: "Password reset was successful",
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
    locale: 'de',
    allowComposition: true,
    fallbackLocale: 'en',
    globalInjection: true,
    messages
})
const app = createApp(App)
const pinia = createPinia();
app.use(pinia)
app.use(router)
app.use(Quasar, quasarUserOptions)
app.use(i18n)

app.mount('#app')

axios.defaults.headers.post['Content-Type'] = 'application/json'

watch(
    pinia.state,
    (state) => {
        sessionStorage.setItem("users", JSON.stringify(state.users));
        sessionStorage.setItem("floorPlan", JSON.stringify(state.floorPlan));
    },
    { deep: true }
);
