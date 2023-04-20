<template>
  <q-page class="items-center justify-center" style="display: flex">
    <div class="q-gutter-y-md column" style="max-width: 40em; min-width: 20em; display: flex">
      <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">Passwort ändern</h1>
      <div class="q-gutter-md col items-start q-mt-xs">
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
      </div>
      <q-btn @click="passwortChange" color="primary" label="Passwort ändern"/>
    </div>
  </q-page>
</template>

<script>
import { ref } from 'vue'

import {useUserStore} from "../stores/users"
import {useQuasar} from 'quasar'
import {useRouter} from 'vue-router'
export default {
  name: "PasswortChange",
  setup () {
    const password = ref('')
    const passwordRepeat = ref('')
    const $q = useQuasar()
    const router = useRouter()

    function passwordChange() {
      userStore.validatePassword(password.value, passwordRepeat)
      if (!userStore.validPassword) {
        $q.notify({
          type: 'negative',
          message: 'Passwort konnte nicht zurückgesetzt werden',
          caption: 'Passwort erfüllt nicht die Kriterien'
        })
      }
      if (!userStore.comparePassword) {
        $q.notify({
          type: 'negative',
          message: 'Passwort konnte nicht zurückgesetzt werden',
          caption: 'Die Passwörter stimmen nicht überein'
        })
      } else {
        router.push('./login')
      }
      return {
        passwordChange,
        password,
        isPwd: ref(true),
        passwordRepeat,
        isPwdRepeat: ref(true)
      }
    }
  }
}
</script>

<style scoped>

</style>