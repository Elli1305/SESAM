<template>
    <q-page class="items-center justify-center" style="display: flex">
        <div class="q-gutter-y-md column" style="max-width: 40em; min-width: 20em; display: flex">
            <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">Registrierung</h1>
            <q-input outlined v-model="prename" label="Vorname"/>
            <q-input outlined v-model="lastname" label="Nachname"/>
            <q-input v-model="email" outlined type="email" label="Email" />
            <q-input v-model="password" outlined :type="isPwd ? 'password' : 'text'" label="Passwort" >
                <template v-slot:append>
                    <q-icon class="q-mr-xs" color="grey" size="16px" name="info" />
                    <q-tooltip class="bg-grey-8" anchor="top left" self="bottom left"
                               :offset="[0, 8]">Das Passwort muss mind. 8 Zeichen lang  <br> sein, ein Sonderzeichen,
                        eine Ziffer und  <br> einen Großbuchstaben beinhalten</q-tooltip>
                    <q-icon
                            :name="isPwd ? 'visibility_off' : 'visibility'"
                            class="cursor-pointer"
                            @click="isPwd = !isPwd"
                    />
                </template>
            </q-input>
            <q-input v-model="passwordRepeat" outlined :type="isPwdRepeat ? 'password' : 'text'" label="Passwort wiederholen">
                <template v-slot:append>
                    <q-icon
                            :name="isPwdRepeat ? 'visibility_off' : 'visibility'"
                            class="cursor-pointer"
                            @click="isPwdRepeat = !isPwdRepeat"
                    />
                </template>
            </q-input>
            <q-option-group
                    :options="options"
                    type="checkbox"
                    v-model="group"
                    size = "25px"
                    inline
            />
            <q-btn @click="signUp()" color="primary" label="Registrieren"/>
            <p style="font-size: 1em">Bereits registriert:
                <a href="./login">Zum Login</a>
            </p>
        </div>
    </q-page>

</template>

<script lang="ts">
import { ref } from 'vue'
import {useUserStore} from "../stores/users"
import {useQuasar} from 'quasar'
import {useRouter} from 'vue-router'
export default {
    name: "SignUp.vue",
    setup () {
        const $q = useQuasar()
        const router = useRouter()
        const email = ref('')
        const lastname = ref('')
        const prename = ref('')
        const password = ref('')
        const passwordRepeat = ref('')
        const group = ref([])
        const userStore = useUserStore()
        async function signUp() {
            userStore.validateEmail(email.value);
            userStore.validatePassword(password.value, passwordRepeat.value);
            if(!userStore.validEmail){
                $q.notify({
                    type: 'negative',
                    message: 'Registrierung fehlgeschlagen',
                    caption: 'E-Mail erfüllt nicht die Kriterien'
                })
            }
            if(!userStore.validPassword){
                $q.notify({
                    type: 'negative',
                    message: 'Registrierung fehlgeschlagen',
                    caption: 'Passwort erfüllt nicht die Kriterien'
                })
            }
            if(!userStore.comparePassword){
                $q.notify({
                    type: 'negative',
                    message: 'Registrierung fehlgeschlagen',
                    caption: 'Die Passworte stimmen nicht überein'
                })
            }
            if (!userStore.validPassword || !userStore.validEmail || !userStore.comparePassword) {
                return;
            }

            try {
              await userStore.signUp(email.value, password.value, prename.value, lastname.value, group.value);
            } catch (e) {
              console.error(e);

              $q.notify({
                type: 'negative',
                message: 'Nutzer konnte nicht registriert werden.',
                caption: 'Bitte versuchen Sie es später erneut.'
              });

              return;
            }

          await userStore.requestToken({eMail: email.value, password: password.value})
              .catch((error) => {
                console.log(error)
                if(error.response.status === 403) {
                  $q.notify({
                    type: 'negative',
                    message: 'Login Fehlgeschlagen',
                    caption: 'Falsches Passwort oder Benutzername',
                    position: "top",
                    timeout: 3000,
                    classes: "loginNotify"
                  })
                } else if(error.response.status === 500) {
                  $q.notify({
                    type: 'negative',
                    message: 'Login Fehlgeschlagen',
                    caption: 'Der Server konnte die Anfrage nicht verarbeiten',
                    position: "top",
                    timeout: 3000,
                    classes: "loginNotify"
                  })
                } else {
                  $q.notify({
                    type: 'negative',
                    message: 'Login Fehlgeschlagen',
                    caption: 'Etwas ist schiefgelaufen',
                    position: "top",
                    timeout: 3000,
                    classes: "loginNotify"
                  })
                }
              })

            if (userStore.authenticated) {
                router.push('/')
                $q.notify({
                    type: 'positive',
                    message: 'Registrierung erfolgreich'
                })
            } else {
                $q.notify({
                    type: 'negative',
                    message: 'Registrierung fehlgeschlagen',
                    caption: 'Nutzer (Email) bereits vergeben'
                })
            }
        }
        return {
            signUp,
            password,
            isPwd: ref(true),
            passwordRepeat,
            isPwdRepeat: ref(true),
            prename,
            lastname,
            email,
            group,
            options: [
                { label: 'Admin', value: 'ADMINISTRATOR' },
                { label: 'Bearbeiter', value: 'EDITOR'},
                { label: 'Herausgeber', value: 'ISSUER'}
            ]
        }
    }
}
</script>
<style scoped>
</style>