<template>
  <q-page>
    <div class="col-auto fixed-center ">
      <div class="row-auto">
        <div class="text-h4 q-ma-sm">Registrierung</div>
      </div>
      <div class="row-auto q-ma-sm">


        <div class="q-pa-md">

          <div class="q-gutter-md col items-start">
            <q-input outlined v-model="prename" label="Vorname"/>
            <q-input outlined v-model="lastname" label="Nachname"/>
            <q-input v-model="email" outlined type="email" label="Email" />
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

            <q-input v-model="passwordWdh" outlined :type="isPwdWdh ? 'password' : 'text'" hint="Passwort wiederholen">
              <template v-slot:counter>
                <q-icon
                    :name="isPwdWdh ? 'visibility_off' : 'visibility'"
                    class="cursor-pointer"
                    @click="isPwdWdh = !isPwdWdh"
                />
              </template>
            </q-input>
          </div>



        </div>
        <div class="row-auto">
          <div class="col q-mt-sm">
            <div class="q-pa-md">
              <q-option-group
                  :options="options"
                  type="checkbox"
                  v-model="group"
              />
            </div>
          </div>
        </div>

        <div class="col q-mt-xl">

          <q-btn  size = "15px"  rounded label="Registrieren" @click="registrieren()" color="secondary" class="row-auto q-ma-xs"></q-btn>
          <q-btn size = "15px" rounded label="Zur Anmeldung" @click="zuLogin()" color="secondary" class="row-auto q-ma-xs"></q-btn>
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
  name: "Registrierung.vue",
  setup () {
    const $q = useQuasar()
    const router = useRouter()
    const email = ref('')

    const userStore = useUserStore()
    function registrieren() {
      userStore.authenticate(email.value)
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
      registrieren,
      password: ref(''),
      isPwd: ref(true),
      passwordWdh: ref(''),
      isPwdWdh: ref(true),
      prename: ref(''),
      lastname: ref(''),
      email,
      group: ref([]),
      options: [
        { label: 'Admin', value: 'adminRole' },
        { label: 'Bearbeiter', value: 'bearbeiterRole'},
        { label: 'Herausgeber', value: 'herausgeberRole'}
      ]
    }
  }

}
</script>

<style scoped>
</style>