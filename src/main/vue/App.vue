<script setup>
import {RouterView} from 'vue-router'
import {useI18n} from "vue-i18n";
import {useUserStore} from "@/main/vue/stores/users"
import {useQuasar} from 'quasar'
import {useRouter} from "vue-router/dist/vue-router"

const { t } = useI18n()
const userStore = useUserStore()
const $q = useQuasar()
const router = useRouter()

    async function logout (){
      await userStore.logout()
      if (!userStore.authenticated) {
        $q.notify({
          type: 'positive',
          message: 'Sie haben sich erfolgreich abgemeldet',
          caption: 'Logout erfolgreich',
          position: "top",
          timeout: 3000,
          classes: "loginNotify"
        })
        await router.push('/')
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
          <router-link to="/" style="color: white; text-decoration: none"><p>{{t("home.floorplan")}}</p></router-link>
          <q-space style="width: 1em" />
          <p>{{t("home.credentials")}}</p>
          <q-space style="width: 1em" />
          <p>{{t("home.information")}}</p>
          <q-space style="width: 3em" />
            <q-btn v-if="userStore.authenticated" :label="userStore.firstName.charAt(0) + userStore.lastName.charAt(0)" rounded color="info" unelevated auto-close style="width: 36px; height: 36px;" >
              <q-menu>
                <q-list>
                  <q-item to="/profile" clickable v-close-popup>
                    <q-item-section text-color="black" style="width: 7.5em" unelevated><div><q-icon left name="person"/>{{t('profile.title')}}</div></q-item-section>
                  </q-item>
                  <q-item @click="logout" clickable v-close-popup>
                    <q-item-section style="width: 7.5em" unelevated><div><q-icon left name="logout"/>{{t('home.logout')}}</div></q-item-section>
                  </q-item>
                </q-list>
              </q-menu>
            </q-btn>
            <div v-if="!userStore.authenticated" class="row" >
              <q-btn to="/signup" class="shadow-1" :label="t('home.signup')"/>
            <q-space style="width: 1em" />
              <q-btn to="/login" class="shadow-1" :label="t('home.signin')"/>
           </div>
        </div>
      </q-toolbar>
    </q-header>

    <q-page-container>
      <router-view/>
    </q-page-container>

    <q-footer elevated class="bg-grey-8 text-white">
      <q-toolbar class="bg-grey-7">
        <q-toolbar-title style="text-align: center; font-size: 1em">

          <a href="/impressum" style="color: white; font-variant-caps: small-caps;">{{t('home.imprint')}}</a>

        </q-toolbar-title>
      </q-toolbar>
    </q-footer>

  </q-layout>

</template>

<style scoped>
  p {
    font-size: 1.5em;
    font-variant-caps: small-caps;
    vertical-align: center;
    margin: 0;
  }
</style>
