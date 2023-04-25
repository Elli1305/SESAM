<template>
  <q-page class="items-center justify-center" style="display: flex">
    <div class="q-gutter-y-md column" style="max-width: 40em; min-width: 20em; display: flex">
      <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">{{ t("passwordChange.changePassword") }}</h1>
      <q-input v-model="password" outlined :type="isPwd ? 'password' : 'text'" :label="t('passwordChange.password')">
        <template v-slot:append>
          <q-icon class="q-mr-xs" color="grey" size="16px" name="info" />
          <q-tooltip class="bg-grey-8" anchor="top left" self="bottom left" :offset="[0, 8]">
            <div style="max-width: 20em">
              {{ t('passwordChange.passwordHint') }}
            </div>
          </q-tooltip>
          <q-icon :name="isPwd ? 'visibility_off' : 'visibility'" class="cursor-pointer" @click="isPwd = !isPwd" />
        </template>
      </q-input>
      <q-input v-model="passwordRepeat" outlined :type="isPwdRepeat ? 'password' : 'text'"
        :label="t('passwordChange.repeatPassword')">
        <template v-slot:append>
          <q-icon :name="isPwdRepeat ? 'visibility_off' : 'visibility'" class="cursor-pointer"
            @click="isPwdRepeat = !isPwdRepeat" />
        </template>
      </q-input>
      <q-btn @click="passwordChange" color="primary" :label="t('passwordChange.changePassword')" />
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useUserStore } from "../stores/users"
import { useQuasar } from 'quasar'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n';


const props = defineProps<{
  token: string,
}>();

const { t } = useI18n();

const password = ref('')
const passwordRepeat = ref('')
const isPwd = ref(true)
const isPwdRepeat = ref(true)

defineExpose({
  password,
  passwordRepeat,
  isPwd,
  isPwdRepeat,
})

const userStore = useUserStore()
const $q = useQuasar()
const router = useRouter()

async function passwordChange() {
  userStore.validatePassword(password.value, passwordRepeat.value)
  if (!userStore.validPassword) {
    $q.notify({
      type: 'negative',
      message: t('passwordChange.failedToReset'),
      caption: t('passwordChange.passwordDoesNotConform')
    });

    return;
  }

  if (!userStore.comparePassword) {
    $q.notify({
      type: 'negative',
      message: t('passwordChange.failedToReset'),
      caption: t('passwordChange.passwordsDontMatch')
    })

    return;
  }

  try {
    await userStore.changePassword(password.value, props.token)
  }
  catch (error: any) {
    console.log(error);

    if (error.response.status === 403 || error.response.status === 422) {
      $q.notify({
        type: 'negative',
        message: t('passwordChange.failedToReset'),
        caption: t('passwordChange.passwordDoesNotConform'),
        position: "top",
        timeout: 3000,
        classes: "loginNotify"
      })
    } else if (error.response.status === 500) {
      $q.notify({
        type: 'negative',
        message: t('passwordChange.failedToReset'),
        caption: t('common.internalServerError'),
        position: "top",
        timeout: 3000,
        classes: "loginNotify"
      })
    } else {
      $q.notify({
        type: 'negative',
        message: t('passwordChange.failedToReset'),
        caption: t('common.unknownError'),
        position: "top",
        timeout: 3000,
        classes: "loginNotify"
      })
    }

    return;
  }

  router.push('./login');
  $q.notify({
    type: 'positive',
    message: t('passwordChange.successfulReset'),
    position: "top",
    timeout: 3000,
    classes: "loginNotify"
  });
}
</script>

<style scoped></style>