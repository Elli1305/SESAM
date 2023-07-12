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
        common: {
            // General words
            cancel: 'Abbrechen',
            save: 'Speichern',
            delete: "Löschen",
            done: "Fertig",
            edit: "Bearbeiten",
            search: "Suche",
            der: "der",
            die: "die",
            das: "das",
            of: "von",
            // Concept words
            credential: "Credential",
            credentials: "Credential",
            category: "Kategorie",
            categories: "Kategorien",
            // Error messages
            internalServerError: "Der Server konnte die Anfrage nicht verarbeiten",
            unknownError: "Ein unbekannter Fehler ist aufgetreten",
            noData: "Keine Daten vorhanden.",
            noResults: "Keine Einträge gefunden.",
        },
        home: {
            applicationName: "SESAM",
            signUp: "Registrierung",
            signIn: "Anmeldung",
            // Floorplan
            floorPlan: "Gebäudeplan",
            // Profile management
            profileManagement: "Profilverwaltung",
            currentUsers: "Nutzer verwalten",
            currentRegistrations: "Angefragte Rollen",
            issuerManagement: "Herausgeber verwalten",
            // Corporate design
            corporateDesign: "Corporate Design",
            editCorporateDesign: "Corporate Design anpassen",
            editImprint: "Impressum bearbeiten",
            // Credential management
            credentialManagement: "Credentialverwaltung",
            manageCredentialCategories: "Kategorien von Credentials verwalten",
            manageCredentials: "Credentials verwalten",
            editorPages: "Bearbeiten",
            editFloorplan: "Gebäudeplan bearbeiten",
            groupRooms: "Räume gruppieren",
            issuerPages: "Credential Ausstellen",
            imprint: "Impressum",
            logout: "Logout",
            predefinedConfig: "Konfiguration erstellen"
        },
        login: {
            wrongEmailPassword: "Falsches Passwort oder Benutzername",
            loginFailed: "Login fehlgeschlagen",
            forgotPassword: "Passwort vergessen: ",
            resetPassword: "Passwort zurücksetzen",
            notRegistered: "Nicht registriert: ",
            toRegister: "Zur Registrierung",
        },
        logout: {
            title: "Logout erfolgreich",
            success: "Sie haben sich erfolgreich abgemeldet"
        },
        signUp: {
            title: "Registrierung",
            passwordRepeat: "Passwort wiederholen",
            alreadySignedUp: "Bereits registriert: ",
            error: "Registrierung fehlgeschlagen",
            errorCaption : "Nutzer (E-Mail) ist bereits vergeben",
            success: "Registrierung war erfolgreich",
            nameError: "Name erfüllt nicht die Kriterien",
            emailError: "Keine gültige E-Mail-Adresse",
            passwordError: "Passwort erfüllt nicht die Kriterien",
            passwordDuplicateError: "Passwörter stimmen nicht überein",
            warning: "Nutzer konnte nicht registriert werden",
            later: "Bitte versuchen Sie es später erneut"
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
            passwordHint: "Das Passwort muss mind. 8 Zeichen lang sein und ein Sonderzeichen, eine Ziffer und einen Großbuchstaben beinhalten",
            repeatPassword: "Passwort wiederholen",
            failedToReset: "Password zurücksetzen fehlgeschlagen",
            passwordDoesNotConform: "Passwort erfüllt nicht die Kriterien",
            passwordsDontMatch: "Die Passwörter stimmen nicht überein",
            successfulReset: "Passwort zurücksetzen war erfolgreich"
        },
        profile: {
            title: "Profilansicht",
            firstName: "Vorname",
            lastName: "Nachname",
            email: "E-mail",
            password: "Passwort",
            role: "Rolle",
            roles: "Rollen",
            administrator: "Admin",
            editor: "Bearbeiter",
            issuer: "Herausgeber",
            administrators: "Administratoren",
            editors: "Bearbeiter",
            issuers: "Herausgeber",
        },
        floorPlan: {
            // Locations
            locations: 'Standorte',
            addLocation: 'Standort hinzufügen',
            editLocation: 'Standort bearbeiten',
            deleteLocation: "Standort löschen",
            deleteLocationQuestion: "Sind Sie sicher, dass Sie den Standort löschen wollen?",
            locationEditSuccess: "Standort wurde erfolgreich gespeichert",
            locationSaveSuccess: "Standort wurde erfolgreich hinzugefügt",
            // Buildings
            addBuilding: 'Gebäude hinzufügen',
            editBuilding: 'Gebäude bearbeiten',
            deleteBuilding: 'Gebäude löschen',
            deleteBuildingQuestion: "Sind Sie sicher, dass Sie das Gebäude löschen wollen?",
            buildingEditSuccess: "Gebäude wurde erfolgreich gespeichert",
            buildingSaveSuccess: "Gebäude wurde erfolgreich hinzugefügt",
            // Floors
            floorLevel: 'Etagennummer',
            addFloor: 'Etage hinzufügen',
            editFloor: 'Etage bearbeiten',
            deleteFloor: "Etage löschen",
            deleteFloorQuestion: "Sind Sie sicher, dass Sie die Etage löschen wollen?",
            floorEditSuccess: "Etage wurde erfolgreich gespeichert",
            floorSaveSuccess: "Etage wurde erfolgreich hinzugefügt",
            // Rooms
            room: 'Raum',
            rooms: 'Räume',
            roomName: 'Raumname',
            editRoom: 'Raum bearbeiten',
            // Doors
            doors: 'Türen',
            doorName: 'Türname',
            addDoor: 'Tür zuweisen',
            editDoor: 'Tür bearbeiten',
            pickRoom: 'Raum auswählen',
            confirmDeletion: 'Löschen bestätigen',
            confirmDeletionText: 'Sind Sie sicher, dass Sie die Tür löschen möchten?',
            doorCreateFailed: 'Tür konnte nicht erstellt werden',
            // Groups
            groupName: 'Gruppenname',
            editGroup: 'Gruppe bearbeiten',
            roomAmount: 'Anzahl an Räumen',
            // Configuration
            config: 'Konfiguration',
            configDescription: 'Beschreibung der Konfiguration',
            configGroup: 'Konfigurationsgruppe',
            infoConfigGroups: 'Konfigurationsgruppen sind untereinander mit UND verknüpft',
            chooseConfig: "Konfiguration auswählen",
            addConfigGroup: 'Konfigurationsgruppe hinzufügen',
            direction: 'Richtung',
            directions: {
                in: 'rein',
                both: 'beide',
                out: 'raus',
            },
            infoCredential: 'Credentials sind untereinander mit ODER verknüpft',
            infoCredentialGroups: 'Durch Credentialgruppen werden alle Credentials der Gruppe auf einmal ausgewählt',
            addAttribute: 'Attribut hinzufügen',
            credentialAttributes: {
                id: 'ID',
                firstName: 'Vorname',
                lastName: 'Nachname',
                birthdate: 'Geburtstagsdatum',
                expirationDate: 'Ablaufdatum'
            },
            currentTime: 'Aktueller Zeitpunkt',
            //
            floorPlanUpload: 'Etagenplan hochladen',
        },
        credentialView: {
            title: "Credentialansicht",
            location: "Standort",
            availableCredentials: "Verfügbare Credential",
            qualification: "Vergleichbare Credential",
            room: "Raum: ",
            allCredentials: "Alle Credentials",
            type: "Typ",
            showCredentials: "Alle anzeigen",
            all: "Alle",
            intern: "Intern",
            extern: "Extern"

        },
        admin: {
            currentUsers: {
                title: "Nutzerverwaltung",
                editUser: {
                    title: "Nutzer Bearbeiten",
                    changeRoles: "Rollen vergeben/entfernen: ",
                    deleteUser: "Nutzer Löschen",
                    question: "Wollen Sie diesen Nutzer wirklich löschen?",
                    deleteOwnAccount: "Eigener Account kann nicht gelöscht werden",
                    otherAdmin: "Bitte wenden Sie sich an einen anderen Administrator",
                },
            },
            requestedRoles: {
                title: "Angefragte Rollen",
            },
            issuerManagement: {
                title: "Herausgeberverwaltung",
                issuableCredential: "Ausstellbare Credential",
                office: "Büro",
                dialogTitle: "Herausgeber verwalten",
            },
            corporateDesign: {
                title: "Corporate Design",
                logo: "Logo",
                logoHint: "Nur SVG Dateien",
                favicon: "Favicon",
                faviconHint: "Nur ICO Dateien",
                background: "Hintergrund",
                textColor: "Schriftfarbe",
                info: {
                    primary: "Banner, Buttons",
                    secondary: "Rollenschilder",
                    accent: "Schriftfarbe in Banner, Footer, Buttons",
                    dark: "Footer",
                    light: "Icons",
                    positive: "Positive Nachricht, Speichern Button",
                    negative: "Negative Nachricht, Löschen/Zurücksetzen Button",
                    info: "Profilbild, Informationen",
                    warning: "Warnungen",
                },
                reset: {
                    title: "Sind Sie sicher, dass Sie zurücksetzen wollen?",
                    message: "Danach können Sie Ihre aktuellen Einstellungen nicht wiederherstellen.",
                    reset: "Zurücksetzen",
                },
                save: {
                    title: "Sind Sie sicher, dass Sie speichern wollen?",
                    message: "Danach können Sie Ihre aktuellen Einstellungen nicht wiederherstellen.",
                },
                resetFailure: "Zurücksetzen fehlgeschlagen",
                saveFailure: "Speichern fehlgeschlagen"
            },
            imprint: {
                imprintTitle: "Impressum",
                imprintEditorTitle: "Impressum Bearbeiten",
                imprintEditorMessageSave: "Sind Sie sicher, dass Sie speichern wollen?",
                imprintEditorMessageDelete: "Sind Sie sicher, dass Sie das Impressum löschen wollen?",
                imprintEditorMessageSaveConfirmation: "Inhalt erfolgreich gespeichert",
                imprintEditorMessageDeleteConfirmation: "Inhalt erfolgreich gelöscht"
            },
            credentialMapping: {
                title: 'Credentialmapping',
                newCategory: 'Neue Kategorie',
                categoryName: 'Kategoriename',
                internal: 'Interne Credentials',
                external: 'Externe Credentials',
                createCategory: 'Neue Kategorie erstellen',
                changeCategory: 'Kategorie ändern',
                deleteCategory: 'Kategorie löschen',
                delete: 'Möchten Sie die Kategorie wirklich löschen?',
                info: "Jedes Credential darf nur in einer Gruppe sein. Es wird bei Auswahl möglicherweise aus einer anderen Kategorie entfernt."
            },
            credentialAdministration: {
                title: "Credentials verwalten",
                type: "Art",
                new: "Neues Credential",
                attribute: "Name des Attributes",
                edit: "Credential bearbeiten",
                checklist: "Checkliste",
                credentialAttribute: "Credentialattribute",
                loadCredentialSchema: "Credential-Schema laden"
            }
        },
        editor: {
            groupRooms: {
                title: "Raumgruppierungen",
                chooseBuilding: "Gebäude wählen",
                floor: "Etage: ",
                chooseLocation: "Location wählen",
                newGroup: "Neue Gruppe",
                editGroup: "Gruppierung bearbeiten",
                chooseRooms: "Räume auswählen",
                question: "Wollen Sie diese Gruppierung wirklich löschen?",
                deleteGroup: "Gruppe löschen",
                addRoomsToSelected: "Willst du die ausgewählten Räume wirklich zu {0} hinzufügen?",
                doorconfiguration: "Türkonfigurationen",
                nameOfGroup: "Name der neuen Gruppe",
                groups: "Raumgruppen",
                info: "Räume einer neuen Gruppe hinzufügen oder eine bereits bestehende Gruppe auswählen und 'Räume zu ausgewählter Gruppe hinzufügen' drücken",
                addRooms: "Zu gewählter Gruppe hinzufügen",
                addRoomsToNewGroup:"Zu neuer Gruppe hinzufügen",
                noGroupSelected: "Keine Gruppe ausgewählt",
                noRoomSelected: "Keine Räume ausgewählt",
                checkNameMessage: "Name darf nicht leer sein",
                checkNameCaption: "Mindestens ein Buchstabe, eine Ziffer oder ein Zeichen.",
                doorconfig: "Türkonfiguration anpassen",
                select: "Auswählen",
                doors: "Türen",
                groupsConfig: "Anpassung der Türkonfiguration für Raumgruppen",
                roomSelection: "Räume auswählen",
                group: "Gruppe",
            },
            config:{
              noConfig: "Nicht konfiguriert"
            },
            predefinedConfigs: {
                title: "Vordefinierte Konfigurationen",
                deleteAlert: "Konfiguration Löschen",
                new: "Neue Konfiguration",
                deleteQuestion: "Möchten Sie diese Konfiguration wirklich löschen?",
                name: "Konfigurationsbezeichnung",
                nameConfig: "Bezeichnung"
            },
            credentialEditing: {
                credentialSchemaLoadFailed: 'Das Credential Schema konnte nicht vom Ledger geladen werden.',
                errors: {
                    'ERR_LEDGER_COMMUNICATION_FAILED': 'Die Kommunikation mit dem Ledger ist fehlgeschlagen.',
                    'ERR_INVALID_STRUCTURE': 'Die Credential Definition ID ist fehlerhaft.',
                    'ERR_CREDENTIAL_DEFINITION_NOT_FOUND': 'Die angegebene Credential Definition ID konnte nicht auf dem Ledger gefunden werden.'
                }
            }
        },
        issuer: {
            issue: "Ausstellen",
            issueCredential: {
                title: "{0} Credential Ausstellung",
                description: [
                    "Herzlich Willkommen zum Ausstellungsprozess des \"{0}\" Credentials! Hier können Sie ein offizielles Dokument erstellen, das die erfolgreiche Absolvierung eines bestimmten Kurses oder die Erlangung bestimmter Fähigkeiten oder Qualifikationen bestätigt.",
                    "Um das Credential auszustellen, füllen Sie bitte die neben stehenden Felder mit den geforderten Attributen aus. Stellen Sie sicher, dass alle Daten korrekt und aktuell sind. Sobald Sie alle erforderlichen Attribute ausgefüllt haben, klicken Sie auf die Schaltfläche \"@:{'issueCredential.next'}\"."
                ],
                checkConditions: "Bitte verwenden Sie die folgende Checkliste, um sicherzustellen, dass alle notwendigen Schritte ausgeführt wurden, bevor Sie das Credential ausstellen.",
                validation: {
                    validationRules: "Validierungsregeln",
                    ruleErrors: {
                        inputRequired: "Dieses Feld ist erforderlich.",
                        equal: "Dieses Feld muss gleich {0} sein",
                        notEqual: "Dieses Feld darf nicht gleich {0} sein",
                        lessThan: "Dieses Feld muss kleiner als {0} sein",
                        greaterThan: "Dieses Feld muss größer als {0} sein",
                        lessEqual: "Dieses Feld muss kleiner gleich {0} sein",
                        greaterEqual: "Dieses Feld muss größer gleich {0} sein",
                        range: "Dieses Feld muss zwischen {0} und {1} liegen",
                        equalLength: "Die Länge dieses Texts muss gleich {0} sein",
                        notEqualLength: "Die Länge dieses Texts darf nicht gleich {0} sein",
                        lessThanLength: "Die Länge dieses Texts muss kleiner als {0} sein",
                        greaterThanLength: "Die Länge dieses Texts muss größer als {0} sein",
                        lessEqualLength: "Die Länge dieses Texts muss kleiner gleich {0} sein",
                        greaterEqualLength: "Die Länge dieses Texts muss größer gleich {0} sein",
                    },
                    vType: {
                        comparison: "Vergleich",
                        range: "Bereich",
                        regEx: "Regulärer Ausdruck",
                        length: "Länge",
                    },
                    addValidationRule: "Validierungsregel hinzufügen",
                    description: "Beschreibung",
                    chooseType: "Wähle einen Typ",
                    chooseAttribute: "Wähle ein Attribut",
                    presetsLabel: "Vordef. Regeln",
                    presets: {
                        // Numbers
                        positiveWithZero: "Positiv",
                        positiveWithoutZero: "Positiv (exkl. 0)",
                        negativeWithZero: "Negativ",
                        negativeWithoutZero: "Negativ (exkl. 0)",
                        // Dates
                        futureWithToday: "Zukunft (inkl. heute)",
                        futureWithoutToday: "Zukunft",
                        pastWithToday: "Vergangenheit (inkl. heute)",
                        pastWithoutToday: "Vergangenheit",
                        today: "Heute",
                        // Strings
                        names: "Namen",
                        noSpecialCharacter: "Keine Sonderzeichen",
                        email: "Email",
                        phoneNumbers: "Telefonnummern",
                        uppercase: "Nur Großbuchstaben",
                        lowercase: "Nur Kleinbuchstaben",
                    },
                    info: "Validierungsregeln können festgelegt werden, um die Eingabe beim Ausstellen eines Credentials einzuschränken. Sie können aus vordefinierten Regeln wählen oder Ihre eigene definieren.",
                },
                steps: {
                    form: "Formular",
                    list: "Checkliste",
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
                },
                addCredential: {
                    title: "Ihr neues Credential wurde erfolgreich ausgestellt!",
                    howTo: "So fügen Sie ein Credential zur BC Wallet hinzu: ",
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
    },
    en: {
        common: {
            // General words
            cancel: 'Cancel',
            save: 'Save',
            delete: "Delete",
            done: "Done",
            edit: "Edit",
            search: "Search",
            der: "the",
            die: "the",
            das: "the",
            of: "of",
            // Concept words
            credential: "Credential",
            credentials: "Credentials",
            category: "Category",
            categories: "Categories",
            // Error messages
            internalServerError: "The server could not process the request",
            unknownError: "An unknown error occurred",
            noData: "No data available.",
            noResults: "No entries found.",
        },
        home: {
            applicationName: "SESAM",
            signUp: "Registration",
            signIn: "Login",
            // Floorplan
            floorPlan: "Floorplan",
            // Profile management
            profileManagement: "Profile Management",
            currentUsers: "Manage users",
            currentRegistrations: "Requested roles",
            issuerManagement: "Manage issuers",
            // Corporate design
            corporateDesign: "Corporate Design",
            editCorporateDesign: "Edit Corporate Design",
            editImprint: "Edit Imprint",
            // Credential management
            credentialManagement: "Credential Management",
            manageCredentialCategories: "Manage credential categories",
            manageCredentials: "Manage credentials",
            editorPages: "Edit",
            editFloorplan: "Edit floor plan",
            groupRooms: "Group rooms",
            issuerPages: "Issue Credentials",
            imprint: "Imprint",
            logout: "Logout",
            predefinedConfig: "Create Configuration"
        },
        login: {
            wrongEmailPassword: "Wrong password oder username",
            loginFailed: "Login failed",
            forgotPassword: "Forgot password: ",
            resetPassword: "Reset password",
            notRegistered: "Not registered: ",
            toRegister: "Register",
        },
        logout: {
            title: "Logout was successful",
            success: "You logged out successfully"
        },
        signUp: {
            title: "Registration",
            passwordRepeat: "Repeat password",
            alreadySignedUp: "Already signed up: ",
            error: "Registration was not successful",
            errorCaption : "User (Email) is already registered",
            success: "Registration was successful",
            nameError: "Name does not fullfill the requirements",
            emailError: "Not a valid Email adress",
            passwordError: "Password does not fullfill the requirements",
            passwordDuplicateError: "Passwords are different from each ther",
            warning: "Registration of the user was not successful",
            later: "Try again later"
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
            passwordHint:
                "The password must include a min. of 8 characters, 1 special character, 1 number, and 1 capital letter",
            repeatPassword: "Repeat password",
            failedToReset: "Failed to reset password",
            passwordDoesNotConform: "Password does not match the required criteria",
            passwordsDontMatch: "Passwords do not match",
            successfulReset: "Password reset was successful",
        },
        profile: {
            title: "Profile View",
            firstName: "First name",
            lastName: "Last name",
            email: "Email",
            password: "Password",
            role: "Role",
            roles: "Roles",
            administrator: "Admin",
            editor: "Editor",
            issuer: "Issuer",
            administrators: "Administrators",
            editors: "Editors",
            issuers: "Issuers",
        },
        floorPlan: {
            // Locations
            locations: 'Locations',
            addLocation: 'Add location',
            editLocation: 'Edit location',
            deleteLocation: "Delete location",
            deleteLocationQuestion: "Are you sure you want to delete this location?",
            locationEditSuccess: "Location was saved successfully",
            locationSaveSuccess: "Location was added successfully",
            // Buildings
            addBuilding: 'Add building',
            editBuilding: 'Edit building',
            deleteBuilding: 'Delete building',
            deleteBuildingQuestion: "Are you sure you want to delete this building?",
            buildingEditSuccess: "Building was saved successfully",
            buildingSaveSuccess: "Building was added successfullyt",
            // Floors
            floorLevel: 'Floor level',
            addFloor: 'Add floor',
            editFloor: 'Edit floor',
            deleteFloor: "Delete floor",
            deleteFloorQuestion: "Are you sure you want to delete this floor?",
            floorEditSuccess: "Floor was saved successfully",
            floorSaveSuccess: "Floor was added successfully",
            // Rooms
            room: 'Room',
            rooms: 'Rooms',
            roomName: 'Room name',
            editRoom: 'Edit Room',
            // Doors
            doors: 'Doors',
            doorName: 'Door name',
            addDoor: 'Assign door',
            editDoor: 'Edit Door',
            pickRoom: 'Choose room',
            confirmDeletion: 'Confirm deletion',
            confirmDeletionText: 'Are you sure you want to delete the door?',
            doorCreateFailed: 'Door could not be created',
            // Groups
            groupName: 'Group name',
            editGroup: 'Edit group',
            roomAmount: 'Amount of rooms',
            // Configuration
            config: 'Configuration',
            configDescription: 'Description of the configuration',
            configGroup: 'Configuration Group',
            infoConfigGroups: 'Configuration groups are combined with AND',
            chooseConfig: "Choose Predefined Config",
            addConfigGroup: 'Add configuration group',
            direction: 'Direction',
            directions: {
                in: 'in',
                both: 'both',
                out: 'out',
            },
            infoCredential: 'Credentials are combined with OR',
            infoCredentialGroups: 'With credential groups, all credentials of the group are being added simultaneously',
            addAttribute: 'Add attribute',
            credentialAttributes: {
                id: 'ID',
                firstName: 'first name',
                lastName: 'last name',
                birthdate: 'date of birth',
                expirationDate: 'expiration date'
            },
            currentTime: 'Current time',
            //
            floorPlanUpload: 'Upload floor plan',
        },
        credentialView: {
            title: "Credentials",
            location: "Location",
            availableCredentials: "Available Credentials",
            qualification: "Comparable Credentials",
            room: "Room: ",
            allCredentials: "All Credentials",
            type: "Type",
            showCredentials: "Show all",
            all: "All",
            intern: "Internal",
            extern: "External"
        },
        admin: {
            currentUsers: {
                title: "User Management",
                editUser: {
                    title: "Edit User",
                    changeRoles: "Give/take roles: ",
                    deleteUser: "Delete User",
                    question: "Do you really want to delete this user?",
                    deleteOwnAccount: "You can not delete your own account",
                    otherAdmin: "Please ask another administrator for help",
                },
            },
            requestedRoles: {
                title: "Requested Roles",
            },
            issuerManagement: {
                title: "Issuer Management",
                issuableCredential: "Issuable credentials",
                office: "Office",
                dialogTitle: "Manage Issuer",
            },
            corporateDesign: {
                title: "Corporate Design",
                logo: "Logo",
                logoHint: "Only SVG files",
                favicon: "Favicon",
                faviconHint: "Only ICO files",
                background: "Background",
                textColor: "Text-Color",
                info: {
                    primary: "banner, buttons",
                    secondary: "role badges",
                    accent: "text-color in banner, footer, buttons",
                    dark: "footer",
                    light: "icons",
                    positive: "positive notifications, save buttons",
                    negative: "negative notifications, delete/reset buttons",
                    info: "profile button, informational content",
                    warning: "warnings",
                },
                reset: {
                    title: "Are you sure you want to reset?",
                    message: "You will not be able to retrieve your current settings.",
                    reset: "Reset",
                },
                save: {
                    title: "Are you sure you want to save?",
                    message: "You will not be able to retrieve your current settings.",
                },
                resetFailure: "Reset failed",
                saveFailure: "Saving failed"
            },
            imprint: {
                imprintTitle: "Imprint",
                imprintEditorTitle: "Imprint Editor",
                imprintEditorMessageSave: "Are you sure you want to save?",
                imprintEditorMessageDelete: "Are you sure you want to delete?",
                imprintEditorMessageSaveConfirmation: "Content saved successfully",
                imprintEditorMessageDeleteConfirmation: "Content successfully deleted"
            },
            credentialMapping: {
                title: 'Credential mapping',
                newCategory: 'New category',
                categoryName: 'Category name',
                internal: 'Internal credentials',
                external: 'External credentials',
                createCategory: 'Create new category',
                changeCategory: 'Change category',
                deleteCategory: 'Delete category',
                delete: 'Are you sure you want to delete the category?',
                info: "Each credential is only allowed in one group. If you select the credential, it might be removed from another category"
            },
            credentialAdministration: {
                title: "Manage Credentials",
                type: "Type",
                new: "New Credential",
                attribute: "Name of the attribute",
                edit: "Edit Credential",
                checklist: "Checklist",
                credentialAttribute: "Credential attributes",
                loadCredentialSchema: "Load credential schema"
            }
        },
        editor: {
            groupRooms: {
                title: "Room Groups",
                chooseBuilding: "Choose a building",
                chooseLocation: "Choose a location",
                floor: "Floor: ",
                newGroup: "New group",
                editGroup: "Edit group",
                chooseRooms: "Choose the rooms",
                question: "Do you really want to delete this group?",
                deleteGroup: "Delete group",
                addRoomsToSelected: "Do you really want to add these rooms to {0}?",
                nameOfGroup: "Name of the new group",
                doorconfiguration: "Door configurations",
                groups: "Room Groups",
                info: "Add rooms either to a new group or choose an existing group",
                addRooms: "Add rooms to selected group",
                addRoomsToNewGroup:"Add rooms to a new group",
                noGroupSelected: "No group selected",
                noRoomSelected: "No rooms selected",
                checkNameMessage: "Name must not be empty",
                checkNameCaption: "At least one letter, number or symbol.",
                doorconfig: "Adjust door configuration",
                select: "Select",
                doors: "Doors",
                groupsConfig: "Adjustment of door configurations for groups of rooms",
                roomSelection: "Choose Rooms",
                group: "Group",
            },
            config:{
                noConfig: "not configured"
            },
            predefinedConfigs: {
                title: "Predefined configurations",
                deleteAlert: "Delete configuration",
                new: "New configuration",
                deleteQuestion: "Are you sure you want to delete this configuration?",
                name: "Configuration Name",
                nameConfig: "Name"
            },
        },
        issuer: {
            issue: "Issue",
            issueCredential: {
                title: "{0} Credential Issuance",
                description: [
                    "Welcome to the issuance process of the \"{0}\" Credential! Here you can create an official document that confirms the successful completion of a specific course or the attainment of certain skills or qualifications.",
                    "To issue the credential, please fill out the fields on the right with the required attributes. Make sure that all data is correct and up-to-date. Once you have filled out all the necessary attributes, click the \"@:{'issueCredential.next'}\" button."
                ],
                checkConditions: "Please use the following checklist to ensure that all necessary steps have been taken before issuing the credential.",
                validation: {
                    validationRules: "Validation Rules",
                    ruleErrors: {
                        inputRequired: "This field is required.",
                        equal: "This field should be equal to {0}",
                        notEqual: "This field should not be equal to {0}",
                        lessThan: "This field should be less than {0}",
                        greaterThan: "This field should be greater than {0}",
                        lessEqual: "This field should be less than or equal to {0}",
                        greaterEqual: "This field should be greater than or equal to {0}",
                        range: "This field should be between {0} and {1}",
                        equalLength: "This text's length should be equal to {0}",
                        notEqualLength: "This text's length should not be equal to {0}",
                        lessThanLength: "This text's length should be less than to {0}",
                        greaterThanLength: "This text's length should be greater than to {0}",
                        lessEqualLength: "This text's length should be less than or equal to {0}",
                        greaterEqualLength: "This text's length should be greater than or equal to {0}",
                    },
                    vType: {
                        comparison: "Comparison",
                        range: "Range",
                        regEx: "Regular expression",
                        length: "Length",
                        comparisonWithAttribute: "Comparison with another attribute",
                        rangeWithAttributes: "Range between attributes",
                        lengthWithAttribute: "Length compared to another attribute",
                    },
                    addValidationRule: "Add validation rule",
                    description: "Description",
                    chooseType: "Choose a type",
                    chooseAttribute: "Choose an attribute",
                    presetsLabel: "Presets",
                    presets: {
                        // Numbers
                        positiveWithZero: "Positive",
                        positiveWithoutZero: "Positive (excl. 0)",
                        negativeWithZero: "Negative",
                        negativeWithoutZero: "Negative (excl. 0)",
                        // Dates
                        futureWithToday: "Future (incl. today)",
                        futureWithoutToday: "Future",
                        pastWithToday: "Past (incl. today)",
                        pastWithoutToday: "Past",
                        today: "Today",
                        // Strings
                        names: "Names",
                        noSpecialCharacter: "No special characters",
                        email: "Email",
                        phoneNumbers: "Phone numbers",
                        uppercase: "Only uppercase",
                        lowercase: "Only lowercase",
                    },
                    info: "You can define validation rules. These will regulate the inputs while issuing a credential. You can choose from presets or create your own custom rule.",
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
                },
                addCredential: {
                    title: "Your new credential has been successfully issued!",
                    howTo: "How to add a credential to the BC Wallet: ",
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
        },
    },
}
let currentLanguage = sessionStorage.getItem("locale");
if (currentLanguage == null) {
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

