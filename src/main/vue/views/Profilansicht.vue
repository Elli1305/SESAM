<template>
  <q-page class="column justify-evenly" style="padding: 0 5em" >
    <p class="row text-h3 justify-center">{{t('profile.title')}}</p>
    <div class="self-center" style="margin: 2em; width: 25em" >
      <q-input id="prename" v-model="user.firstName" :label="t('profile.firstname')" outlined disable/>
      <q-space style="height: 1em" />
      <q-input id="surname" v-model="user.lastName" :label="t('profile.lastname')" outlined disable/>
      <q-space style="height: 1em" />
      <q-input id="email" v-model="user.username" :label="t('profile.email')" outlined disable/>
    </div>
    <div class="row justify-center" >
      <q-badge class="row justify-center" rounded color="secondary" text-color="primary" :style="{marginRight: '2em', height: '2em', width: '10em', opacity: adminopacity}" ><q-icon name="person" left />{{t('profile.admin')}}</q-badge>
      <q-badge class="row justify-center" rounded color="secondary" text-color="primary" :style="{marginRight: '2em', height: '2em', width: '10em', opacity: editorOpacity}"  ><q-icon name="person" left />{{t('profile.editor')}}</q-badge>
      <q-badge class="row justify-center" rounded color="secondary" text-color="primary" :style="{height: '2em', width: '10em', opacity: issueropacity}" ><q-icon name="person" left />{{t('profile.issuer')}}</q-badge>
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
    const user = userStore.user
    const { t } = useI18n()

    let adminOpacity = user.roles.some(r => r.role === 'ADMINISTRATOR' && r.granted) ? "100%" : "50%"
    let editorOpacity = user.roles.some(r => r.role === 'EDITOR' && r.granted) ? "100%" : "50%"
    let issuerOpacity = user.roles.some(r => r.role === 'ISSUER' && r.granted) ? "100%" : "50%"

    return {
      user: user,
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