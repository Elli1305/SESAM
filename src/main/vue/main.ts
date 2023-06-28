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
            lastname: "Name",
            email: "Email",
            role: "Rollen",
            edit: "Bearbeiten",
            showAdmin: "Administratoren",
            showEditor: "Bearbeiter",
            showIssuer: "Herausgeber",
            search: "Suche"
        },
        adminRolesRequest:{
          headline: "Angefragte Rollen",
            prename: "Vorname",
            lastname: "Nachname",
            email: "Email",
            role: "Rollen",
            save: "Speichern",
            showAdmin: "Administratoren",
            showEditor: "Bearbeiter",
            showIssuer: "Herausgeber",
            search: "Suche"
        },
        adminEdit: {
            title: "Nutzer bearbeiten",
            changeRoles: "Rollen vergeben/entfernen: ",
            delete: "Löschen",
            back: "Abbrechen",
            save: "Speichern",
            attention: "ACHTUNG",
            question: "Nutzer wirklich löschen?",
            deleteOwnAccount: "Eigener Account kann nicht gelöscht werden",
            otherAdmin: "Bitte wenden Sie sich an einen anderen Administratoren",
        },
        doorconfig:{
            configurationGroup: "Konfigurationsgruppen",
            direction: "Richtung:",
            direction2: "Richtung",
            in: "Rein",
            out: "Raus",
            both: "Beide",
            hint: "Konfigurationsgruppen sind untereinander mit UND verknüpft",
            description: "Beschreibung der Konfiguration",
            and: "Credentials in dieser Auswahl sind ODER-Verknüpft",
            category: "KATEGORIEN",
            add: "Konfigurationsgruppe hinzufügen",
            config: "Konfiguration",
            attribute: "Attribut hinzufügen",
            time: "Aktueller Zeitpunkt"
        },
        floorplan: {
            locations: 'Standorte',
            doorName: 'Türname',
            editDoor: 'Tür bearbeiten',
            editRoom: 'Raum bearbeiten',
            editGroup: 'Gruppe bearbeiten',
            save: 'speichern',
            cancel: 'abbrechen',
            done: 'fertig',
            confirmDeletion: 'Bestätigen Sie das Löschen',
            confirmDeletionText:'Sind Sie sicher, dass Sie die Tür löschen möchten?',
            doors: 'Türen',
            rooms: 'Räume',
            roomName: 'Raumname',
            roomAmount: 'Anzahl an Räumen',
            groupName: 'Gruppenname',
            edit: 'Bearbeiten',
            newFloor: 'Etage erstellen',
            editFloor: 'Etage bearbeiten',
            floorlevel: 'Etagennummer',
            floorplanUpload: 'Etagenplan hochladen',
            newBuilding: 'Gebäude erstellen',
            editBuilding: 'Gebäude bearbeiten',
            editLocation: 'Standort bearbeiten',
            addLocation: 'Standort hinzufügen',
            addBuilding: 'Gebäude hinzufügen',
            addFloor: 'Etage hinzufügen',
            addDoor: 'Tür zuweisen',
            pickRoom: 'Raum auswählen',
            configGroup: 'Konfigurationsgruppe',
            config: 'Konfiguration',
            direction: 'Richtung',
            infoConfigGroups: 'Konfigurationsgruppen sind untereinander mit UND verknüpft',
            in: 'rein',
            both: 'beide',
            out: 'raus',
            configDescription: 'Beschreibung der Konfiguration',
            infoCredential: 'Credentials sind untereinander mit ODER verknüpft',
            infoCredentialGroups: 'Durch Credentialgruppen werden alle Credentials der Gruppe auf einmal ausgewählt',
            addAttribute: 'Attribut hinzufügen',
            addConfigGroup: 'Konfigurationsgruppe hinzufügen',
            currentTime: 'Aktueller Zeitpunkt',
            credentialAttributes: {
                id: 'ID',
                firstName: 'Vorname',
                lastName: 'Nachname',
                birthdate: 'Geburtstagsdatum',
                expirationDate: 'Ablaufdatum'
            }

        },
        groupRooms: {
            title: "Gruppierungen von Räumen",
            question: "Gruppierung wirklich löschen?",
            chooseBuilding: "Gebäude wählen",
            chooseLocation: "Location wählen",
            chooseRooms: "Räume auswählen",
            editGroup:"Gruppierung bearbeiten",
            new: "Neue Gruppe",
            search: "Gruppensuche",
            rooms: "Räume",
            groups: "Raumgruppen",
            addRooms: "Räume zu ausgewählten Gruppen zufügen",
            noGroupSelected: "Keine Gruppe ausgewählt",
            noRoomSelected: "Keine Räume ausgewählt",
            checkNameMessage: "Name darf nicht leer sein",
            checkNameCaption: "Mindestens ein Buchstabe, eine Ziffer oder ein Zeichen."

        },

        common: {
            internalServerError: "Der Server konnte die Anfrage nicht verarbeiten",
            unkownError: "Ein unbekannter Fehler ist aufgetreten",
            noData: "Keine Daten vorhanden.",
            noResults: "Kein Einträge gefunden.",
            of: "von",
            cancel: 'Abbrechen',
            save: 'Speichern',
            category: "Kategorie",
            categories: "Kategorien",
        },
        credentialview :{
            credentialview: "Credentialansicht",
            location: "Standort",
            search: "Suche",
            category: "Kategorie",
            availablecredentials: "Verfügbbare Credential",
            qualification: "Vergleichbare Credential",
            issuer: "Herausgeber",
            room: "Raum: "
        },
        credentialmapping: {
            credentialmapping: 'Credentialmapping',
            newcategory: 'Neue Kategorie',
            category: 'Kategorie',
            search: 'Suche',
            name: 'Name der Kategorie',
            save: 'Speichern',
            cancel: 'Abbrechen',
            internal: 'Interne Credentials',
            external: 'Externe Credentials',
            categorycreate: 'Neue Kategorie erstellen',
            categorychange: 'Kategorie ändern',
            categorydelete: 'Löschen der Kategorie',
            delete: 'Möchten Sie die Kategorie wirklich löschen?'
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
            editFloorplan: "Floorplan bearbeiten",
            groupRooms: "Räume gruppieren",
            issuerPages: "Credential ausstellen",
            imprint: "Impressum",
            logout: "Logout",
            roomSearch: "Raumsuche"
        },
        login: {
            wrongEmailPassword: "Falsches Passwort oder Benutzername",
            loginFailed: "Login Fehlgeschlagen",
            forgotPassword: "Passwort vergessen:",
            resetPassword: "Passwort zurücksetzen",
            notRegistered: "Nicht registriert:",
            toRegister: "Zur Registrierung",
            password: "Passwort"
        },
        signUp:{
          prename: "Vorname",
          name: "Nachname",
          password: "Passwort",
          passwordRepeat: "Passwort wiederholen",
          signup: "Registrierung",
          alreadySignedUp: "Bereits registriert:"
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
            next: "Weiter",
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
                title: "Ihr neues Credential wurde erfolgreich ausgestellt!",
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
        corporateDesign: {
            title: "Corporate Design",
            colors: "Farben",
            logo: "Logo",
            logoHint: "Nur SVG Dateien",
            favicon: "Favicon",
            faviconHint: "Nur ICO Dateien",
            background: "Hintergrund",
            textColor: "Schriftfarbe",
            primaryInfo: "Banner, Buttons",
            secondaryInfo: "Rollenschilder",
            accentInfo: "Schriftfarbe in Banner, Footer, Buttons",
            darkInfo: "Footer",
            lightBlueInfo: "...",
            positiveInfo: "Positive Nachricht, Speichern Button",
            negativeInfo: "Negative Nachricht, Löschen/Zurücksetzen Button",
            infoInfo: "Profilbild, Informationen",
            warningInfo: "Warnungen",
            confirm: {
                reset: {
                    title: "Sind Sie sicher, dass sie zurücksetzen wollen?",
                    message: "Danach können Sie Ihre aktuellen Einstellungen nicht wiederherstellen.",
                    ok: "Zurücksetzen",
                    cancel: "Abbrechen"
                },
                save: {
                    title: "Sind Sie sicher, dass sie speichern wollen?",
                    message: "Danach können Sie Ihre aktuellen Einstellungen nicht wiederherstellen.",
                    ok: "Speichern",
                    cancel: "Abbrechen"
                }
            },
            resetFailure: "Zurücksetzen fehlgeschlagen",
            saveFailure: "Speichern fehlgeschlagen"
        },
        imprint:{
            imprintTitle: "Impressum",
            imprintEditorTitle: "Impressum bearbeiten",
            imprintEditorMessageSave: "Sind Sie sicher, dass sie speichern wollen?",
            imprintEditorMessageDelete: "Sind Sie sicher, dass Sie das Impressum löschen wollen?",
            save:"Speichern",
            cancel: "Abbrechen",
            delete:"Löschen",
            imprintEditorMessageSaveConfirmation:"Inhalt erfolgreich gespeichert",
            imprintEditorMessageDeleteConfirmation:"Inhalt erfolgreich gelöscht"

        },

        issuermanagement: {
            title: "Herausgeber verwalten",
            firstname: "Vorname",
            lastname: "Name",
            email: "E-Mail",
            credential:"Credential",
            search: "Suche",
            issuerFilter:"Herausgebern filtern",
            edit: "Bearbeiten",
            credentialsList:"Liste aller Credentials",
            roomsList:"Liste aller Räume",
            roomId: "Raum ID",
            credentials:"Credential",
            dialogTitle:"Credentials verwalten",
            save:"Speichern",
            cancel: "Abbrechen",
            saveConfirmationTitle: "Speichern?",
            saveConfirmationMessage: "Sind Sie sicher, dass sie speichern wollen?",
        },
    },
    en: {
        adminCurrentUser: {
            headline: "Current Users",
            roles: {ADMINISTRATOR: "Admin", EDITOR: "Editor", ISSUER: "Issuer"},
            prename: "Firstname",
            lastname: "Name",
            email: "Email",
            role: "Roles",
            edit: "Edit",
            showAdmin: "Show Admin",
            showEditor: "Show Editor",
            showIssuer: "Show Issuer",
            search: "Search"
        },
        adminRolesRequest:{
            headline: "Requested Roles",
            roles: {ADMINISTRATOR: "Admin", EDITOR: "Editor", ISSUER: "Issuer"},
            prename: "Prename",
            lastname: "Name",
            email: "Email",
            role: "Roles",
            showAdmin: "Show Admin",
            showEditor: "Show Editor",
            showIssuer: "Show Issuer",
            search: "Search",
            save: "Save"
        },

        groupRooms: {
            title: "Groupings of rooms",
            question: "Do you really want to delete this group?",
            chooseBuilding: "Choose a building",
            chooseLocation: "Choose a location",
            chooseRooms: "Choose the rooms",
            editGroup:"Edit group",
            new: "New group",
            search: "Search for groups",
            rooms: "Rooms",
            groups: "Groups of Rooms",
            addRooms: "Add rooms to selected group",
            noGroupSelected: "No group selected",
            noRoomSelected: "No rooms selected",
            checkNameMessage: "Name must not be empty",
            checkNameCaption: "At least one letter, number or symbol."
        },

        adminEdit: {
            title: "Edit user",
            changeRoles: "Give/take Roles: ",
            delete: "Delete",
            back: "Back",
            save: "Save",
            attention: "Attention",
            question: "Do you really want to delete this user?"
        },
        common: {
            internalServerError: "The server could not process the request",
            unkownError: "An unknown error occured",
            noData: "No data available.",
            noResults: "No entries found.",
            of: "of",
            cancel: 'Cancel',
            save: 'Save',
            category: "Category",
            categories: "Categories",
        },
        credentialview :{
            credentialview: "View Credentials",
            location: "Location",
            search: "Search",
            category: "Category",
            availablecredentials: "Available Credentials",
            qualification: "Comparable Credential",
            issuer: "Issuer",
            room: "Room: "
        },
        credentialmapping: {
            credentialmapping: 'Credential mapping',
            newcategory: 'New Category',
            search: 'Search',
            name: 'Name of the Category',
            category: 'Category',
            save: 'Save',
            cancel: 'Cancel',
            internal: 'Internal Credentials',
            external: 'External Credentials',
            categorycreate: 'Create new category',
            categorychange: 'Change category',
            categorydelete: 'Delete category',
            delete: 'Are you sure you want to delete the category?'
        },
        doorconfig:{
            configurationGroup: "Configuration Groups",
            direction: "Direction:",
            direction2: "Direction",
            in: "In",
            out: "Out",
            both: "Both",
            hint: "Configuration groups are connected by AND",
            description: "Description of the Configuration",
            and: "Credentials in this selection are connected by OR",
            category: "CATEGORIES",
            add: "Add configuration group",
            config: "Configuration",
            attribute: "Add attribute",
            time: "Current time point"
        },
        floorplan: {
            locations: 'Locations',
            doorName: 'Door name',
            editDoor:'Edit Door',
            editRoom:'Edit Room',
            editGroup: 'Edit group',
            editFloor: 'Edit Floor',
            floorlevel: 'Floor level',
            floorplanUpload: 'Upload floor plan',
            save: 'save',
            cancel: 'cancel',
            done: 'done',
            confirmDeletion: 'Confirm deletion',
            confirmDeletionText:'Are you sure you want to delete the door?',
            doors:'Doors',
            rooms: 'Rooms',
            roomName: 'Name of room',
            roomAmount: 'Amount of rooms',
            groupName: 'Name of room',
            edit: 'Edit',
            editBuilding: 'Edit building',
            editLocation: 'Edit location',
            addLocation: 'Add location',
            addBuilding: 'Add building',
            addFloor: 'Add floor',
            addDoor: 'Assign door',
            pickRoom: 'Choose room',
            configGroup: 'Configuration Group',
            config: 'Configuration',
            direction: 'Direction',
            infoConfigGroups: 'Configuration groups are combined with AND',
            in: 'in',
            both: 'both',
            out: 'out',
            configDescription: 'Description of the configuration',
            infoCredential: 'Credentials are combined with OR',
            infoCredentialGroups: 'With credential groups, all credentials of the group are being added simultaneously',
            addAttribute: 'Add attribute',
            addConfigGroup: 'Add configuration group',
            currentTime: 'Current time',
            credentialAttributes: {
                id: 'ID',
                firstName: 'first name',
                lastName: 'last name',
                birthdate: 'date of birth',
                expirationDate: 'expiration date'
            }

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
            editorPages: "Bearbeiten",
            editFloorplan: "Floorplan bearbeiten",
            groupRooms: "Räume gruppieren",
            issuerPages: "Issue Credential",
            imprint: "Imprint",
            logout: "Logout",
            roomSearch: "Search"
        },
        login: {
            wrongEmailPassword: "Wrong Password oder Username",
            loginFailed: "Login Failed",
            forgotPassword: "Forgot Password:",
            resetPassword: "Reset Password",
            notRegistered: "Not registered",
            toRegister: "Go to Register",
            password: "Password"
        },
        signUp:{
            prename: "Prename",
            name: "Name",
            password: "Password",
            passwordRepeat: "Repeat password",
            signup: "Sign up",
            alreadySignedUp: "Already signed up:"
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
        },
        issueCredential: {
            title: "{0} Credential Issuance",
            description: [
                "Welcome to the issuance process of the \"{0}\" Credential! Here you can create an official document that confirms the successful completion of a specific course or the attainment of certain skills or qualifications.",
                "To issue the credential, please fill out the fields on the right with the required attributes. Make sure that all data is correct and up-to-date. Once you have filled out all the necessary attributes, click the \"@:{'issueCredential.next'}\" button."
            ],
            checkConditions: "Please use the following checklist to ensure that all necessary steps have been taken before issuing the credential.",
            validation: {
                inputRequired: "This field is required.",
            },
            steps: {
                form: "Form",
                list: "Checklist",
                qrcode: "QR Code",
            },
            next: "Next",
            previous: "Previous",
            checklistHint:
                "To issue the credential, all conditions on the checklist must be met.",
            confirm: {
                title: "Are you sure you want to issue the credential?",
                message:
                    'Please ensure that all conditions have been met and all necessary steps have been taken before issuing the credential. Once issued, it cannot be undone.',
                ok: "Issue Credential",
                cancel: "Cancel",
            },
            addCredential: {
                title: "Your new credential has been successfully issued!",
                howTo: "How to add a credential to the BC Wallet:",
                steps: {
                    step1:
                        "Download the {0} from the App Store or Google Play Store.",
                    step2:
                        "Open the app and follow the instructions to set up a new account.",
                    step3:
                        'Once you have successfully set up your account, select the "Credentials" tab.',
                    step4:
                        "Tap the small plus sign in the upper right corner of the screen.",
                    step5:
                        'In the menu that appears, select the option "Scan a QR code".',
                    step6:
                        "Point the camera of your smartphone at the QR code you want to scan (the QR code should be located to the right of these instructions).",
                    step7:
                        "Review the information displayed on the screen to ensure that it is correct.",
                    step8:
                        'If all information is correct, tap "Accept" to add the credential to your wallet.',
                },
            },
            errors: {
                get: {
                    failed: "The credential could not be loaded.",
                    unauthorized:
                        "You do not have the necessary permissions to view the credential.",
                },
                issue: {
                    failed: "The credential could not be issued.",
                    unauthorized:
                        "You do not have the necessary permissions to issue the credential.",
                    failedDependency:
                        "Could not connect to the Credential Issuing Self-Service. Please ensure that you are connected to the university network.",
                },
                unknown: "Please try again later.",
            },
        },
        corporateDesign: {
            title: "Corporate Design",
            colors: "Colors",
            logo: "Logo",
            logoHint: "Only SVG files",
            favicon: "Favicon",
            faviconHint: "Only ICO files",
            background: "Background",
            textColor: "Text-Color",
            primaryInfo: "banner, buttons",
            secondaryInfo: "role badges",
            accentInfo: "text-color in banner, footer, buttons",
            darkInfo: "footer",
            lightBlueInfo: "...",
            positiveInfo: "positive notifications, save buttons",
            negativeInfo: "negative notifications, delete/reset buttons",
            infoInfo: "profile button, informational content",
            warningInfo: "warnings",
            confirm: {
                reset: {
                    title: "Are you sure you want to reset?",
                    message: "You will not be able to retrieve your current settings.",
                    ok: "Reset",
                    cancel: "Cancel"
                },
                save: {
                    title: "Are you sure you want to save?",
                    message: "You will not be able to retrieve your current settings.",
                    ok: "Save",
                    cancel: "Cancel"
                }
            },
            resetFailure: "Reset failed",
            saveFailure: "Saving failed"
        },
        imprint:{
            imprintTitle: "Imprint",
            imprintEditorTitle: "Imprint Editor",
            imprintEditorMessageSave: "Are you sure you want to save?",
            imprintEditorMessageDelete: "Are you sure you want to delete?",
            save:"Save",
            cancel: "Cancel",
            delete:"Delete",
            imprintEditorMessageSaveConfirmation:"Content saved successfully",
            imprintEditorMessageDeleteConfirmation:"Content successfully deleted"

        },

        issuermanagement: {
            title: "Issuer Management",
            firstname: "Prename",
            lastname: "Name",
            email: "E-Mail",
            credential:"Credential",
            search: "Search",
            issuerFilter:"Issuer filter",
            edit: "Edit",
            credentialsList:"List of all credentials",
            roomsList:"List of all rooms",
            roomId:"Room ID",
            credentials:"Credential",
            dialogTitle:"Credentials Management",
            save:"Save",
            cancel: "Cancel",
            saveConfirmationTitle: "Save?",
            saveConfirmationMessage: "Are you sure you want to save?",



        },
    }
}

let currentLanguage = sessionStorage.getItem("locale");
if(currentLanguage==null){
    currentLanguage = 'de'
}
const i18n = createI18n({
    legacy: false,
    locale: currentLanguage,
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
        sessionStorage.setItem("floorPlan", state.floorplan ? JSON.stringify(state.floorPlan) : '');
    },
    {deep: true}
);
