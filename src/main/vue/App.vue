<script setup>
import {RouterView} from 'vue-router'
import {useI18n} from "vue-i18n";
import {useUserStore} from "@/main/vue/stores/users"
import {useQuasar} from 'quasar'
import {useRouter} from "vue-router/dist/vue-router"
import CountryFlag from 'vue-country-flag-next'

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
      <q-toolbar class="row" style="margin: 0; padding: 24px">
        <div class="column">
          <q-img src="src/main/resources/brand_design/T_logo_white.svg" @click="router.push('/')" class="foldMenu" style="height: 95px; width: 80px; margin-right: 24px"/>
        </div>
        <div class="column full-width justify-between" style="height: 95px">
          <div id="upper" class="row justify-between" style="height: 42px">
            <div class="row">
              <q-toolbar-title style="font-weight: 800; font-size: 3.25em; line-height: 1">
                {{t("home.header")}}
              </q-toolbar-title>
            </div>
            <div class="row justify-center" style="width: 42px; height: 42px">
              <country-flag style="border: white solid 1px" :country="this.$i18n.locale.toString() === 'de' ? 'de' : 'gb'" shadow size="normal"/>
            </div>
          </div>
          <div id="lower" class="row justify-end items-center no-wrap">
            <div class="row">
              <router-link to="/" class="headerLink"><p class="headerText">{{t("home.floorplan")}}</p></router-link>
              <router-link to="/" class="headerLink"><p class="headerText">{{t("home.credentials")}}</p></router-link>
              <div>
                <p v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'ADMINISTRATOR' && r.granted)"
                   class="headerText foldMenu">
                  {{t("home.profileManagement")}}
                </p>
                <q-menu fit transition-show="jump-down" transition-hide="jump-up" anchor="bottom right" self="top right">
                  <div class="column">
                    <router-link to="/" class="q-ma-sm headerLink text-black">{{t("home.currentUsers")}}</router-link>
                    <router-link to="/" class="q-ma-sm headerLink text-black">{{t("home.currentRegistrations")}}</router-link>
                    <router-link to="/" class="q-ma-sm headerLink text-black">{{t("home.issuerManagement")}}</router-link>
                  </div>
                </q-menu>
              </div>
              <div>
                <p v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'ADMINISTRATOR' && r.granted)"
                   class="headerText foldMenu">
                  {{t("home.corporateDesign")}}
                </p>
                <q-menu fit transition-show="jump-down" transition-hide="jump-up" anchor="bottom right" self="top right">
                  <div class="column">
                    <router-link to="/" class="q-ma-sm headerLink text-black">{{t("home.editCorporateDesign")}}</router-link>
                    <router-link to="/ImprintEditor" class="q-ma-sm headerLink text-black">{{t("home.editImprint")}}</router-link>
                  </div>
                </q-menu>
              </div>
              <router-link
                  v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'ADMINISTRATOR' && r.granted)"
                  to="/" class="headerLink">
                <p class="headerText">{{t("home.credentialManagement")}}</p>
              </router-link>
              <router-link
                  v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted)"
                  to="/" class="headerLink">
                <p class="headerText">{{t("home.editorPages")}}</p>
              </router-link>
              <router-link
                  v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'ISSUER' && r.granted)"
                  to="/" class="headerLink">
                <p class="headerText">{{t("home.issuerPages")}}</p>
              </router-link>
            </div>
            <div style="margin-left: 1em">
              <q-btn v-if="userStore.authenticated" rounded color="info" unelevated auto-close size="3em" style="height: 3em; width: 3em; font-size: 1em; line-height: 1">
                <p style="margin-top: 0.1em; margin-bottom: 0; font-size: 1.5em; font-weight: 400; line-height: 1">{{userStore.user.firstName.charAt(0) + userStore.user.lastName.charAt(0)}}</p>
                <q-menu transition-show="jump-down" transition-hide="jump-up">
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
              <q-btn v-if="!userStore.authenticated" to="/login" style="height: 3em; width: 3em" unelevated >
                <q-icon name="login" size="2em"/>
              </q-btn>
            </div>
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

          <router-link to="./imprint" style="color: white">{{t('home.imprint')}}</router-link>

        </q-toolbar-title>
      </q-toolbar>
    </q-footer>

  </q-layout>

</template>

<style scoped>
  @font-face {
    font-family: "TeleNeoVariable-Upright";
    src: local("TeleNeoVariable-Upright"),
      url(../../fonts/TeleNeoVariable-Upright.ttf) format("truetype");
  }
  .headerText {
    margin-bottom: 0;
    margin-right: 1em;
    line-height: 1;
    font-weight: 500;
    font-size: 1.5em;
    vertical-align: center;
  }
  .headerLink {
    color: white;
    text-decoration: none;
  }
  .foldMenu {
    cursor: pointer;
  }
</style>
