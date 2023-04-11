<template>
  <q-page>
    <div class="col-auto fixed-center ">
      <div class="row justify-center full-height full-width text-center">
        <div class="text-h4">Registrierung</div>
      </div>
      <div class="row-auto q-ma-sm">


        <div class="q-pa-md">

          <div class="q-gutter-md col items-start">
            <q-input outlined v-model="prename" hint="Vorname"/>
            <q-input outlined v-model="lastname" hint="Nachname"/>
            <q-input v-model="email" outlined type="email" hint="Email" />
          </div>

          <div class="q-gutter-md col items-start q-mt-xs">
            <q-input v-model="password" outlined :type="isPwd ? 'password' : 'text'" hint="Passwort" >
              <template v-slot:counter>
                <q-icon
                    :name="isPwd ? 'visibility_off' : 'visibility'"
                    class="cursor-pointer"
                    @click="isPwd = !isPwd"
                />
              </template>
            </q-input>

            <q-input v-model="passwordRepeat" outlined :type="isPwdRepeat ? 'password' : 'text'" hint="Passwort wiederholen">
              <template v-slot:counter>
                <q-icon
                    :name="isPwdRepeat ? 'visibility_off' : 'visibility'"
                    class="cursor-pointer"
                    @click="isPwdRepeat = !isPwdRepeat"
                />
              </template>
            </q-input>
          </div>



        </div>
        <div class="row-auto">
          <div class="col q-mt-md">
            <div class="q-ml-md">
              <q-option-group
                  :options="options"
                  type="checkbox"
                  v-model="group"
                  size = "25px"
              />
            </div>
          </div>
        </div>

        <div class="col q-mt-md">

          <q-btn  size = "15px"  rounded label="Registrieren" @click="signUp()" color="secondary" class="row-auto q-ma-xs"></q-btn>
          <q-btn size = "15px" rounded label="Zur Anmeldung" @click="getToLogin()" color="secondary" class="row-auto q-ma-xs"></q-btn>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>

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
    const password = ref('')
    const passwordRepeat = ref('')
    const userStore = useUserStore()
    function signUp() {
      userStore.authenticate(email.value)
      userStore.validatePassword(password.value, passwordRepeat)
      if (userStore.authenticated && userStore.validPassword) {
        router.push('/')
        $q.notify({
          type: 'positive',
          message: 'Registrierung erfolgreich'
        })
      } else {
        if(!userStore.authenticated){
            $q.notify({
                type: 'negative',
                message: 'Registrierung fehlgeschlagen',
                caption: 'Nutzer (Email) bereits vergeben'
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
      }
    }

    return {
      signUp,
      password,
      isPwd: ref(true),
      passwordRepeat,
      isPwdRepeat: ref(true),
      prename: ref(''),
      lastname: ref(''),
      email,
      group: ref([]),
      options: [
        { label: 'Admin', value: 'adminRole' },
        { label: 'Bearbeiter', value: 'editorRole'},
        { label: 'Herausgeber', value: 'publisherRole'}
      ]
    }
  }

}
</script>

<style scoped>
</style>