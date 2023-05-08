<template>
  <q-page class="column justify-evenly" style="padding: 0 5em" >
    <p class="row text-h3 justify-center">{{t('profile.title')}}</p>
    <div class="self-center" style="margin: 2em; width: 25em" >
      <q-input id="prename" v-model="firstname" :label="t('profile.firstname')" outlined readonly/>
      <q-space style="height: 1em" />
      <q-input id="surname" v-model="lastname" :label="t('profile.lastname')" outlined readonly/>
      <q-space style="height: 1em" />
      <q-input id="email" v-model="email" :label="t('profile.email')" outlined readonly/>
    </div>
    <div class="row justify-center" >
      <q-badge class="row justify-between" rounded color="secondary" text-color="primary" :style="{marginRight: '2em', padding: '0.4em 1em 0.4em 0.5em', fontSize: '1em', opacity: adminopacity}" ><q-icon name="account_circle" left/>{{t('profile.admin')}}</q-badge>
      <q-badge class="row justify-between" rounded color="secondary" text-color="primary" :style="{marginRight: '2em', padding: '0.4em 1em 0.4em 0.5em', fontSize: '1em', opacity: editorOpacity}"  ><q-icon name="account_circle" left />{{t('profile.editor')}}</q-badge>
      <q-badge class="row justify-between" rounded color="secondary" text-color="primary" :style="{padding: '0.4em 1em 0.4em 0.5em', fontSize: '1em', opacity: issueropacity}" ><q-icon name="account_circle" left />{{t('profile.issuer')}}</q-badge>
    </div>
  </q-page>
</template>

<script>
import {useUserStore} from "@/main/vue/stores/users"
import {useI18n} from "vue-i18n";

export default {
  name: "Profilansicht",
  props() {
    this.$refs.admin.$props.color = "info"
  },
  data() {
    const userStore = useUserStore()
    const firstName = userStore.firstName
    const lastName = userStore.lastName
    const eMail = userStore.eMail
    const roles = userStore.roles
    const { t } = useI18n()

    let adminOpacity = roles.some(r => r.role === 'ADMINISTRATOR' && r.granted) ? "100%" : "50%"
    let editorOpacity = roles.some(r => r.role === 'EDITOR' && r.granted) ? "100%" : "50%"
    let issuerOpacity = roles.some(r => r.role === 'ISSUER' && r.granted) ? "100%" : "50%"

    return {
      firstname: firstName,
      lastname: lastName,
      email: eMail,
      adminopacity: adminOpacity,
      editorOpacity: editorOpacity,
      issueropacity: issuerOpacity,
      t
    }
  }
}
</script>

<style scoped>
</style>