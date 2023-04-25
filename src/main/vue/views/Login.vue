<template>
  <q-page class="items-center justify-center" style="display: flex">
    <div class="q-gutter-y-md column" style="max-width: 40em; min-width: 20em; display: flex">
      <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">Login</h1>
      <q-input outlined v-model="eMail" label="E-Mail"/>
      <q-input outlined :type="isPwd ? 'password' : 'text'" v-model="password" label="Passwort">
        <template v-slot:append>
          <q-icon
              :name="isPwd ? 'visibility_off' : 'visibility'"
              class="cursor-pointer"
              @click="isPwd = !isPwd"
          />
        </template>
      </q-input>
      <p style="font-size: 1em">Passwort vergessen:
        <a href="">Passwort zurücksetzen</a>
      </p>
      <q-btn @click="login" color="primary" label="Login"/>
      <p style="font-size: 1em">Nicht registriert:
        <a href="./signup">Zur Registrierung</a>
      </p>
    </div>
  </q-page>
</template>

<script>
import {ref} from "vue";
import {useUserStore} from "@/main/vue/stores/users";
import {useQuasar} from 'quasar'
import router from "@/main/vue/router";

export default {
  name: "Login",
  setup() {
    const eMail = ref('')
    const password = ref('')

    const userStore = useUserStore()
    const $q = useQuasar()

    async function login() {
      await userStore.requestToken({eMail: eMail.value, password: password.value})
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

      if (userStore.authenticated === true) {
        await router.push('/');
      }
    }

    return {eMail, password, login, isPwd: ref(true)}
  }
}
</script>

<style scoped>
</style>