<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">Login</p>
    <q-form @submit.prevent="login" class="column self-center items-center justify-evenly no-wrap" style="width: 22.5em; height: 25em">
      <div class="full-width">
        <q-input class="q-my-sm" outlined v-model="eMail" :label="t('profile.email')"/>
        <q-input class="q-my-sm" outlined :type="isPwd ? 'password' : 'text'" v-model="password" :label="t('profile.password')">
          <template v-slot:append>
            <q-icon
                :name="isPwd ? 'visibility_off' : 'visibility'"
                class="cursor-pointer"
                @click="isPwd = !isPwd"
                color="grey"
                size="0.75em"
            />
          </template>
        </q-input>
      </div>
      <p class="full-width" style="font-size: 1em">{{ t("login.forgotPassword") }}
        <router-link class="text-info" to="./passwordreset">{{t('login.resetPassword')}}</router-link>
      </p>
      <q-btn class="full-width" color="primary" text-color="accent" label="Login" type="submit"/>
      <p class="full-width" style="font-size: 1em">{{ t("login.notRegistered") }}
        <router-link class="text-info" to="./signup">{{ t("login.toRegister") }}</router-link>
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
                color: 'negative',
                textColor: 'positive',
                timeout: 3000,
                classes: "loginNotify"
              })
            } else if(error.response.status === 500) {
              $q.notify({
                type: 'negative',
                message: t('login.loginFailed'),
                caption: t('common.internalServerError'),
                color: 'negative',
                textColor: 'positive',
                timeout: 3000,
                classes: "loginNotify"
              })
            } else {
              $q.notify({
                type: 'negative',
                message: t('login.loginFailed'),
                caption: t('common.unknownError'),
                color: 'negative',
                textColor: 'positive',
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