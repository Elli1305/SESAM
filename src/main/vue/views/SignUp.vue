<template>
  <q-page class="column justify-evenly items-center" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">Registrierung</p>
    <div class="column items-center justify-evenly content-stretch" style="width: 22.5em; height: 25em">
      <q-input class="full-width" outlined v-model="prename" :label="t('profile.firstName')"/>
      <q-input class="full-width" outlined v-model="lastname" :label="t('profile.lastName')"/>
      <q-input class="full-width" v-model="email" outlined type="email" label="Email" />
      <q-input class="full-width" v-model="password" outlined :type="isPwd ? 'password' : 'text'" :label="t('profile.password')">
        <template v-slot:append>
          <q-icon
              :name="isPwd ? 'visibility_off' : 'visibility'"
              class="cursor-pointer"
              @click="isPwd = !isPwd"
              color="grey"
              size="0.75em"/>
          <q-icon style="position: absolute; margin-left: 1.25em; margin-top: 0.25em" class="self-start" color="info" size="0.5em" name="info_outlined">
            <q-tooltip class="bg-grey-8" anchor="bottom right" self="top middle" :offset="[0, 0]">
              {{t('passwordChange.passwordHint')}}
            </q-tooltip>
          </q-icon>
        </template>
      </q-input>
      <q-input class="full-width" v-model="passwordRepeat" outlined :type="isPwdRepeat ? 'password' : 'text'" :label="t('signUp.passwordRepeat')">
        <template v-slot:append>
          <q-icon
              :name="isPwdRepeat ? 'visibility_off' : 'visibility'"
              class="cursor-pointer"
              @click="isPwdRepeat = !isPwdRepeat"
              color="grey"
              size="0.75em"/>
        </template>
      </q-input>
      <q-option-group
          class="row justify-evenly"
          style="width: 22.5em"
          :options="options"
          type="checkbox"
          v-model="group"
          size = "25px"
          inline/>
    </div>
      <q-btn style="width: 22.5em" @click="signUp()" color="primary" :label="t('home.signUp')"/>
      <p style="width: 22.5em; font-size: 1em">{{ t('signUp.alreadySignedUp') }}
          <router-link to="./login"> Login</router-link>
      </p>
  </q-page>

</template>

<script lang="ts">
import { ref } from 'vue'
import {useUserStore} from "../stores/users"
import {useQuasar} from 'quasar'
import {useRouter} from 'vue-router'
import {useI18n} from "vue-i18n";
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
        const { t } = useI18n()
        async function signUp() {
            userStore.validateEmail(email.value);
            userStore.validatePassword(password.value, passwordRepeat.value);
            if(!userStore.validEmail){
                $q.notify({
                    type: 'negative',
                    message: 'Registrierung fehlgeschlagen',
                    caption: 'E-Mail erfüllt nicht die Kriterien',
                  color: 'negative',
                  textColor: 'positive',
                })
            }
            if(!userStore.validPassword){
                $q.notify({
                    type: 'negative',
                    message: 'Registrierung fehlgeschlagen',
                    caption: 'Passwort erfüllt nicht die Kriterien',
                  color: 'negative',
                  textColor: 'positive',
                })
            }
            if(!userStore.comparePassword){
                $q.notify({
                    type: 'negative',
                    message: 'Registrierung fehlgeschlagen',
                    caption: 'Die Passworte stimmen nicht überein',
                  color: 'negative',
                  textColor: 'positive',
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
                caption: 'Bitte versuchen Sie es später erneut.',
                color: 'negative',
                textColor: 'positive',
              });

              return;
            }

          await userStore.requestToken({eMail: email.value, password: password.value})
              .catch((error) => {
                console.log(error)
                if(error.response.status === 403 || error.response.status === 422) {
                  $q.notify({
                    type: 'negative',
                    message: 'Login Fehlgeschlagen',
                    caption: 'Falsches Passwort oder Benutzername',
                    position: "top",
                    color: 'negative',
                    textColor: 'positive',
                    timeout: 3000,
                    classes: "loginNotify"
                  })
                } else if(error.response.status === 500) {
                  $q.notify({
                    type: 'negative',
                    message: 'Login Fehlgeschlagen',
                    caption: 'Der Server konnte die Anfrage nicht verarbeiten',
                    position: "top",
                    color: 'negative',
                    textColor: 'positive',
                    timeout: 3000,
                    classes: "loginNotify"
                  })
                } else {
                  $q.notify({
                    type: 'negative',
                    message: 'Login Fehlgeschlagen',
                    caption: 'Etwas ist schiefgelaufen',
                    position: "top",
                    color: 'negative',
                    textColor: 'positive',
                    timeout: 3000,
                    classes: "loginNotify"
                  })
                }
              })

            if (userStore.authenticated) {
                await router.push('/')
                $q.notify({
                  type: 'positive',
                  message: 'Registrierung erfolgreich',
                  color: 'positive',
                  textColor: 'negative',
                })
            } else {
                $q.notify({
                  type: 'negative',
                  message: 'Registrierung fehlgeschlagen',
                  caption: 'Nutzer (Email) bereits vergeben',
                  color: 'negative',
                  textColor: 'positive',
                })
            }
        }
        return {
            t,
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