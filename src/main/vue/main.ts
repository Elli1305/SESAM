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

        common: {
            internalServerError: "Der Server konnte die Anfrage nicht verarbeiten",
            unkownError: "Ein unbekannter Fehler ist aufgetreten",
        },
        credentialview :{
            credentialview: "Credentialansicht",
            location: "Standort",
            search: "Suche",
            credentialtext: "Das Credential wird von folgenden Person ausgestellt:  ",
            credentialtext2: "Herausgeber kann in folgenden Räumen gefunden werden: ",
            credentialtext3: "Als vergleichbare Qualifikation dienen folgende Credentials:",
            credentialtext4: "Zum Erhalt des Credentials muss folgendes erfolgreich abgeschlossen sein:",
            category: "Kategorie",
            availablecredentials: "Verfügbbare Credential",
            qualification: "Vergleichbare Qualifikation",
            issuer: "Herausgeber"
        }
        ,
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
            issuer: "Herausgeber",
        },
        issueCredential: {
            title: "{0} Credential Ausstellung",
            description: [
                "Herzlich Willkommen zum Ausstellungsprozess des \"{0}\" Credentials! Hier können Sie ein offizielles Dokument erstellen, das die erfolgreiche Absolvierung eines bestimmten Kurses oder die Erlangung bestimmter Fähigkeiten oder Qualifikationen bestätigt.",
                "Um das Credential auszustellen, füllen Sie bitte die neben stehenden Felder mit den geforderten Attributen aus. Stellen Sie sicher, dass alle Daten korrekt und aktuell sind. Sobald Sie alle erforderlichen Attribute ausgefüllt haben, klicken Sie auf die Schaltfläche \"@:{'issueCredential.next'}\"."
            ],
            checkConditions: "Bitte verwenden Sie die folgende Checkliste, um sicherzustellen, dass alle notwendigen Schritte ausgeführt wurden, bevor Sie das Credential ausstellen.",
            validation: {
                inputRequired: "Dieses Feld ist erforderlich.",
            },
            steps: {
                form: "Formular",
                list: "Checklist",
                qrcode: "QR-Code",
            },
            next: "Nächster Schritt",
            previous: "Zurück",
            checklistHint:
                "Um das Credential ausstellen zu können muss jede Bedingung der Checkliste erfüllt sein.",
            confirm: {
                title: "Sind Sie sicher, dass Sie das Credential ausstellen möchten?",
                message:
                    'Bitte stellen Sie sicher, dass alle Bedingungen erfüllt sind und Sie alle erforderlichen Schritte ausgeführt haben, bevor Sie das Credential ausstellen. Sobald Sie das Credential ausgestellt haben, kann es nicht mehr rückgängig gemacht werden.',
                ok: "Credential ausstellen",
                cancel: "Abbrechen",
            },
            addCredential: {
                title: " Ihr neues Credential ({0}) wurde erfolgreich ausgestellt!",
                howTo: "So fügen Sie ein Credential zur BC Wallet hinzu:",
                steps: {
                    step1:
                        "Laden Sie die {0} aus dem App Store oder Google Play Store herunter.",
                    step2:
                        "Öffnen Sie die App und befolgen Sie die Anweisungen zur Einrichtung eines neuen Kontos.",
                    step3:
                        'Wenn Sie Ihr Konto erfolgreich eingerichtet haben, wählen Sie den Tab "Credentials" aus.',
                    step4:
                        "Tippen Sie auf das kleine Plus-Zeichen in der oberen rechten Ecke des Bildschirms.",
                    step5:
                        'Wählen Sie im daraufhin erscheinenden Menü die Option "Scan a QR code".',
                    step6:
                        "Richten Sie die Kamera Ihres Smartphones auf den QR-Code, den Sie scannen möchten (der QR-Code sollte sich rechts neben dieser Anleitung befinden).",
                    step7:
                        "Überprüfen Sie die Informationen, die auf dem Bildschirm angezeigt werden, um sicherzustellen, dass sie korrekt sind.",
                    step8:
                        'Wenn alle Informationen korrekt sind, tippen Sie auf "Accept", um das Credential Ihrer Wallet hinzuzufügen.',
                },
            },
            errors: {
                get: {
                    failed: "Das Credential konnte nicht geladen werden.",
                    unauthorized:
                        "Ihnen fehlen die benötigten Berechtigungen um das Credential anzuzeigen.",
                },
                issue: {
                    failed: "Das Credential konnte nicht ausgestellt werden.",
                    unauthorized:
                        "Ihnen fehlen die benötigten Berechtigungen um das Credential auszustellen.",
                    failedDependency:
                        "Es konnte keine Verbindung zum Credential Issuing Self-Service aufgebaut werden. Bitte stellen Sie sicher, dass sie mit dem Netzwerk der Universität verbunden sind.",
                },
                unknown: "Bitte versuchen Sie es später erneut.",
            },
        },
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
        common: {
            internalServerError: "The server could not process the request",
            unkownError: "An unknown error occured",
        },
        credentialview :{
            credentialview: "View Credentials",
            location: "Location",
            search: "Search",
            credentialtext: "The credential can be obtained from",
            credentialtext2: "Issuer can be found in: ",
            credentialtext3: "You need the following qualification for this credential: ",
            credentialtext4: "As a qualification for this credential, you need to have the following:",
            category: "Category",
            availablecredentials: "Available Credentials",
            qualification: "Comparable Qualifications",
            issuer: "Issuer"
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
    locale: 'en',
    allowComposition: true,
    fallbackLocale: 'en',
    globalInjection: true,
    messages
})
const app = createApp(App)

const pinia = createPinia();

app.use(pinia);
app.use(router);
app.use(Quasar, quasarUserOptions);
app.use(i18n);

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
