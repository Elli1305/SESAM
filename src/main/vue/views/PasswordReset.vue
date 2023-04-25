<template>
  <q-page class="items-center justify-center" style="display: flex">
    <div class="q-gutter-y-md column" style="max-width: 40em; min-width: 20em; display: flex">
      <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">{{ t("passwordReset.resetPassword") }}</h1>
      <q-input v-model="email" outlined type="email" :label="t('passwordReset.email')" />
      <q-btn @click="resetPassword" color="primary" :label="t('passwordReset.resetPassword')" />
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
        console.log(error)
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