<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{t("passwordReset.resetPassword")}}</p>
    <div class="column self-center items-center justify-evenly no-wrap" style="width: 22.5em; height: 25em">
      <q-input class="full-width" v-model="email" outlined type="email" :label="t('passwordReset.email')" />
      <q-btn class="full-width" @click="resetPassword" color="primary" :label="t('passwordReset.resetPassword')" />
    </div>
  </q-page>
</template>

<script lang="ts">
import { ref } from 'vue'
import { useUserStore } from "../stores/users"
import { useQuasar } from 'quasar'
import { useI18n } from 'vue-i18n';

export default {
  name: "PasswordReset",
  setup() {
    const { t } = useI18n();

    const $q = useQuasar()
    const userStore = useUserStore()
    const email = ref('')
    async function resetPassword() {
      userStore.validateEmail(email.value);
      if (!userStore.validEmail) {
        $q.notify({
          type: 'negative',
          message: t('passwordReset.emailSendFailed'),
          caption: t('passwordReset.emailDoesNotConform'),
          classes: "loginNotify"
        })
      }

      try {
        await userStore.resetPassword(email.value)
      } catch (error: any) {
        if (error.response.status === 403 || error.response.status === 422) {
          $q.notify({
            type: 'negative',
            message: t('passwordReset.emailSendFailed'),
            caption: t('passwordReset.emailNonExistent'),
            position: "top",
            timeout: 3000,
            classes: "loginNotify"
          })
        } else if (error.response.status === 500) {
          $q.notify({
            type: 'negative',
            message: t('passwordReset.emailSendFailed'),
            caption: t('common.internalServerError'),
            position: "top",
            timeout: 3000,
            classes: "loginNotify"
          })
        } else {
          $q.notify({
            type: 'negative',
            message: t('passwordReset.emailSendFailed'),
            caption: t('common.unkownError'),
            position: "top",
            timeout: 3000,
            classes: "loginNotify"
          })
        }

        return;
      }

      $q.notify({
        type: 'positive',
        message: t('passwordReset.positiveEmail'),
        position: "top",
        timeout: 3000,
        classes: "loginNotify"
      });
    }

    return { email, resetPassword, t }
  }
}
</script>

<style scoped></style>