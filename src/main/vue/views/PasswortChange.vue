<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{t("passwordChange.changePassword")}}</p>
    <div class="column self-center items-center justify-evenly no-wrap" style="width: 22.5em; height: 25em">
      <div class="full-width">
        <q-input class="q-my-md" v-model="password" outlined :type="isPwd ? 'password' : 'text'" :label="t('profile.password')">
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
        <q-input class="q-my-md" v-model="passwordRepeat" outlined :type="isPwdRepeat ? 'password' : 'text'"
          :label="t('passwordChange.repeatPassword')">
          <template v-slot:append>
            <q-icon :name="isPwdRepeat ? 'visibility_off' : 'visibility'" class="cursor-pointer"
              @click="isPwdRepeat = !isPwdRepeat" />
          </template>
        </q-input>
      </div>
      <q-btn class="full-width" @click="passwordChange" color="primary" :label="t('passwordChange.changePassword')" />
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