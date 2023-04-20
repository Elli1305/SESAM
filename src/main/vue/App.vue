<script setup>
import {RouterView} from 'vue-router'
import {useI18n} from "vue-i18n";
import {useUserStore} from "@/main/vue/stores/users"
import {useQuasar} from 'quasar'
import {useRouter} from "vue-router/dist/vue-router";

const { t } = useI18n()
const userStore = useUserStore()
const $q = useQuasar()
const router = useRouter()

  if(userStore.authenticated) {
    document.getElementById("profilePic")
    document.getElementById("registerBtn")
    document.getElementById("loginBtn")
  } else {
    document.getElementById("profilePic")
    document.getElementById("registerBtn")
    document.getElementById("loginBtn")
  }
    async function logout (){
      await userStore.logout
      if (!userStore.authenticated) {
        $q.notify({
          type: 'positive',
          message: 'Sie haben sich erfolgreich abgemeldet',
          caption: 'Logout erfolgreich',
          position: "top",
          timeout: 3000,
          classes: "loginNotify"
        })
        router.push('/home')
      }
}


</script>
<template>
  <q-layout view="hHh lpR fFf">

    <q-header elevated class="bg-primary text-white" height-hint="98">
      <q-toolbar class="q-gutter-y-md column fit" style="padding: 1em">
        <div class="row self-start" style="height: fit-content">
          <q-toolbar-title style="font-size: 2em; font-weight: bolder">
            {{t("home.header")}}
          </q-toolbar-title>
        </div>
        <div class="row self-end" style="margin-top: 0">
          <p>Floorplan</p>
          <q-space style="width: 1em" />
          <p>Credentials</p>
          <q-space style="width: 1em" />
          <p>Information</p>
          <q-space style="width: 3em" />
          <q-btn @click="logout" color="primary" label="Logout"/>
          <q-btn id="profilePic" round>
            <q-avatar size="42px">
              <img src="../resources/Profilbild.png">
            </q-avatar>
          </q-btn>
          <router-link :to="'/signup'">
            <q-btn id="registerBtn" class="shadow-1" label="Registrierung"/>
          </router-link>
          <q-space style="width: 1em" />
          <router-link :to="'/login'">
            <q-btn id="loginBtn" class="shadow-1" label="Login"/>
          </router-link>
        </div>
      </q-toolbar>
    </q-header>

    <q-page-container>
      <router-view/>
    </q-page-container>

    <q-footer elevated class="bg-grey-8 text-white">
      <q-toolbar class="bg-grey-7">
        <q-toolbar-title style="text-align: center; font-size: 1em">
          <a href="">Impressum</a>
        </q-toolbar-title>
      </q-toolbar>
    </q-footer>

  </q-layout>

</template>

<style scoped>
  a {
    color: white;
    font-variant-caps: small-caps;
  }
  p {
    font-size: 1.5em;
    font-variant-caps: small-caps;
    vertical-align: center;
    margin: 0;
  }
</style>
