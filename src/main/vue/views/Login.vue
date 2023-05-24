<template>
  <q-page class="items-center justify-center" style="display: flex">
    <q-form @submit.prevent="login" class="q-gutter-y-md column" style="max-width: 40em; min-width: 20em; display: flex">
      <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">Login</h1>
      <q-input outlined v-model="eMail" label="E-Mail"/>
      <q-input outlined :type="isPwd ? 'password' : 'text'" v-model="password" :label="t('login.password')">
        <template v-slot:append>
          <q-icon
              :name="isPwd ? 'visibility_off' : 'visibility'"
              class="cursor-pointer"
              @click="isPwd = !isPwd"
          />
        </template>
      </q-input>
      <p style="font-size: 1em">{{ t("login.forgotPassword") }}
        <router-link to="./passwordreset">{{t('login.resetPassword')}}</router-link>
      </p>
      <q-btn color="primary" label="Login" type="submit"/>
      <p style="font-size: 1em">{{ t("login.notRegistered") }}
        <router-link to="./signup">{{ t("login.toRegister") }}</router-link>
      </p>
    </q-form>
  </q-page>
</template>

<script>
import {ref} from "vue";
import {useUserStore} from "@/main/vue/stores/users";
import {useQuasar} from 'quasar'
import router from "@/main/vue/router";
import {useI18n} from "vue-i18n";
import { useRoute } from "vue-router";


export default {
  name: "Login",
  setup() {
    const eMail = ref('')
    const password = ref('')

    const userStore = useUserStore()
    const $q = useQuasar()
    const { t } = useI18n()
    const r = useRoute();

    async function login() {
      await userStore.requestToken({eMail: eMail.value, password: password.value})
          .catch((error) => {
            console.log(error)
            if(error.response.status === 403) {
              $q.notify({
                type: 'negative',
                message: t('login.loginFailed'),
                caption: t('login.wrongEmailPassword'),
                position: "top",
                timeout: 3000,
                classes: "loginNotify"
              })
            } else if(error.response.status === 500) {
              $q.notify({
                type: 'negative',
                message: t('login.loginFailed'),
                caption: t('common.internalServerError'),
                position: "top",
                timeout: 3000,
                classes: "loginNotify"
              })
            } else {
              $q.notify({
                type: 'negative',
                message: t('login.loginFailed'),
                caption: t('common.unknownError'),
                position: "top",
                timeout: 3000,
                classes: "loginNotify"
              })
            }
          })


      if (userStore.authenticated === true) {
        const redirect = r.query['redirect'];

        await router.push(typeof redirect !== 'string' ? '/' : redirect);
      }
    }

    return {eMail, password, login, isPwd: ref(true),t}
  }
}
</script>

<style scoped>
</style>