<template>
  <q-page>
      <div style="padding-top: 1em; display: flex; justify-content: center">
          <div class="q-gutter-y-md column" style="max-width: 40em; min-width: 20em">
              <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">Login</h1>
              <q-input outlined v-model="eMail" label="E-Mail" :dense="dense" />
              <q-input outlined :type="isPwd ? 'password' : 'text'" v-model="password" label="Passwort" :dense="dense">
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
              <q-btn @click="login" color="primary" label="Login" />
              <p style="font-size: 1em">Nicht registriert:
                  <a href="">Zur Registrierung</a>
              </p>
          </div>
      </div>
  </q-page>
</template>

<script>
import {ref} from "vue";
import {useUserStore} from "@/main/vue/stores/users";

export default {
    name: "Login",
    setup() {
        const eMail = ref('')
        const password = ref('')

        const userStore = useUserStore()

        function login() {
            userStore.requestToken({eMail: eMail.value, password: password.value})
        }

        return {eMail, password, login, isPwd: ref(true)}
    }
}
</script>

<style scoped>
</style>