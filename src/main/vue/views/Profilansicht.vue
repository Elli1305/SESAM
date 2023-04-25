<template>
  <q-page class="column justify-evenly" style="padding: 0 5em" >
    <p class="row text-h3 justify-center">Profilansicht</p>
    <div class="self-center" style="margin: 2em; width: 25em" >
      <q-input id="prename" v-model="firstname" label="Vorname" outlined disable/>
      <q-space style="height: 1em" />
      <q-input id="surname" v-model="lastname" label="Name" outlined disable/>
      <q-space style="height: 1em" />
      <q-input id="email" v-model="email" label="E-Mail" outlined disable/>
    </div>
    <div class="row justify-center" >
      <q-badge ref="admin" class="row justify-center" rounded color="secondary" text-color="primary" :style="{marginRight: '2em', height: '2em', width: '10em', opacity: this.$data.adminopacity}" ><q-icon name="person" left /> Admin </q-badge>
      <q-badge id="editor-id" class="row justify-center" rounded color="secondary" text-color="primary" :style="{marginRight: '2em', height: '2em', width: '10em', opacity: this.$data.editorOpacity}"  ><q-icon name="person" left /> Bearbeiter </q-badge>
      <q-badge id="issuer" class="row justify-center" rounded color="secondary" text-color="primary" :style="{height: '2em', width: '10em', opacity: this.$data.issueropacity}" ><q-icon name="person" left /> Herausgeber </q-badge>
    </div>
  </q-page>
</template>

<script>
import {useUserStore} from "@/main/vue/stores/users"

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

    let adminOpacity = roles.includes('ADMINISTRATOR') ? "100%" : "50%"
    let editorOpacity = roles.includes('EDITOR') ? "100%" : "50%"
    let issuerOpacity = roles.includes('ISSUER') ? "100%" : "50%"

    return {
      firstname: firstName,
      lastname: lastName,
      email: eMail,
      adminopacity: adminOpacity,
      editorOpacity: editorOpacity,
      issueropacity: issuerOpacity
    }
  }
}
</script>

<style scoped>
</style>