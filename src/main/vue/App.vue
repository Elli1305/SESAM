<script setup>
import {RouterView} from 'vue-router'
import {useI18n} from "vue-i18n";
import {useUserStore} from "@/main/vue/stores/users"
import {useQuasar} from 'quasar'
import {useRouter} from "vue-router/dist/vue-router"
import CountryFlag from 'vue-country-flag-next'
import corpdesign from "@/main/vue/api/corpdesign";

const {t} = useI18n()
const userStore = useUserStore()
const $q = useQuasar()
const router = useRouter()
const i18nLocale = useI18n()
const r = document.querySelector(':root')

corpdesign.setColors()
corpdesign.getColors().then(c => {
  r.style.setProperty('--bg-color', c.data.bgC)
  r.style.setProperty('--text-color', c.data.textC)
})

function getLanguage() {
  return i18nLocale.locale.value.toString() === 'de' ? 'de' : 'gb';
}

function changeLanguage(language) {
  sessionStorage.setItem("locale", language)
  i18nLocale.locale.value = language
  location.reload()
}

async function logout() {
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
  <q-layout view="hHh lpR fff">

    <q-header elevated class="bg-primary text-white" height-hint="98">
      <q-toolbar class="row" style="margin: 0; padding: 24px">
        <div class="column">
          <q-img src="/Logo.svg" @click="router.push('/')" class="foldMenu"
                 style="height: 95px; width: 80px; margin-right: 24px"/>
        </div>
        <div class="column full-width justify-between no-wrap" style="height: 95px">
          <div id="upper" class="row justify-between" style="height: 42px">
            <div class="row text-accent">
              <q-toolbar-title style="font-weight: 800; font-size: 3.25em; line-height: 1">
                {{ t("home.header") }}
              </q-toolbar-title>
            </div>
            <div class="column" style="width: 42px; height: 42px">
              <q-btn class="row no-padding" round unelevated style="width: 3em">
                <country-flag class="self-center no-margin shadow-16"
                              style="height: 3em; width: 3em; border-radius: 100%"
                              :country="getLanguage()" size="normal"/>
                <q-menu fit transition-show="jump-down" transition-hide="jump-up"
                        style="background-color: var(--bg-color)">
                  <q-list>
                    <q-item @click="changeLanguage('de')" clickable v-close-popup>
                      <q-item-section text-color="black" style="width: 7.5em" unelevated>
                        <div class="row justify-start items-center no-wrap">
                          <country-flag rounded country='de' size="small"
                                        style="margin: 0 -0.5em 0.1em -1.5em; border: 1px solid black"/>
                          <p class="no-margin" style="line-height: 1">Deutsch</p>
                        </div>
                      </q-item-section>
                    </q-item>
                    <q-item @click="changeLanguage('en')" clickable v-close-popup>
                      <q-item-section style="width: 7.5em" unelevated>
                        <div class="row justify-start items-center no-wrap">
                          <country-flag rounded country='gb' size="small"
                                        style="margin: 0 -0.5em 0.1em -1.5em; border: 1px solid black"/>
                          <p class="no-margin" style="line-height: 1">English</p>
                        </div>
                      </q-item-section>
                    </q-item>
                  </q-list>
                </q-menu>
              </q-btn>
            </div>
          </div>
          <div id="lower" class="row justify-end items-center no-wrap">
            <div class="row">
              <router-link to="/" class="headerLink text-accent"><p class="headerText">{{ t("home.floorplan") }}</p>
              </router-link>
              <router-link to="/credentialview" class="headerLink text-accent"><p class="headerText">
                {{ t("home.credentials") }}</p></router-link>
              <div>
                <p v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'ADMINISTRATOR' && r.granted)"
                   class="headerText foldMenu text-accent">
                  {{ t("home.profileManagement") }}
                </p>
                <q-menu fit transition-show="jump-down" transition-hide="jump-up" anchor="bottom right"
                        self="top right" style="background-color: var(--bg-color)">
                  <div class="column">
                    <router-link to="/currentUserlist" class="q-ma-sm headerLink text-black">{{
                        t("home.currentUsers")
                      }}
                    </router-link>
                    <router-link to="/rolesRequest" class="q-ma-sm headerLink text-black">
                      {{ t("home.currentRegistrations") }}
                    </router-link>
                    <router-link to="/issuermanagement" class="q-ma-sm headerLink text-black">
                      {{ t("home.issuerManagement") }}
                    </router-link>
                  </div>
                </q-menu>
              </div>
              <div>
                <p v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'ADMINISTRATOR' && r.granted)"
                   class="headerText foldMenu text-accent">
                  {{ t("home.corporateDesign") }}
                </p>
                <q-menu fit transition-show="jump-down" transition-hide="jump-up" anchor="bottom right"
                        self="top right" style="background-color: var(--bg-color)">
                  <div class="column">
                    <router-link to="/corporatedesign" class="q-ma-sm headerLink text-black">
                      {{ t("home.editCorporateDesign") }}
                    </router-link>
                    <router-link to="/imprinteditor" class="q-ma-sm headerLink text-black">{{ t("home.editImprint") }}
                    </router-link>
                  </div>
                </q-menu>
              </div>
              <div>
                <p v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'ADMINISTRATOR' && r.granted)"
                   class="headerText foldMenu text-accent">
                  {{ t("home.credentialManagement") }}
                </p>
                <q-menu fit transition-show="jump-down" transition-hide="jump-up" anchor="bottom right"
                        self="top right" style="background-color: var(--bg-color)">
                  <div class="column">
                    <router-link to="/credentialmapping" class="q-ma-sm headerLink text-black">Credentialmapping
                    </router-link>
                    <router-link to="/credential_administration" class="q-ma-sm headerLink text-black">Credentials
                      verwalten
                    </router-link>
                  </div>
                </q-menu>
              </div>
              <div>
                <p v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted)"
                   class="headerText foldMenu text-accent">{{ t("home.editorPages") }}</p>
                <q-menu fit transition-show="jump-down" transition-hide="jump-up" anchor="bottom right"
                        self="top right" style="background-color: var(--bg-color)">
                  <div class="column">
                    <router-link to="/editFloorPlan" class="q-ma-sm headerLink text-black">{{
                        t("home.editFloorplan")
                      }}
                    </router-link>
                    <router-link to="/grouprooms" class="q-ma-sm headerLink text-black">{{
                        t("home.groupRooms")
                      }}
                    </router-link>
                     <router-link to="/predefinedConfigs" class="q-ma-sm headerLink text-black">{{
                        t("home.predefinedConfig")
                      }}
                     </router-link>
                  </div>
                </q-menu>
              </div>
              <router-link
                  v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'ISSUER' && r.granted)"
                  to="/credentials" class="headerLink text-accent">
                <p class="headerText">{{ t("home.issuerPages") }}</p>
              </router-link>
            </div>
            <div style="margin-left: 1em">
              <q-btn v-if="userStore.authenticated" rounded color="info" unelevated auto-close size="3em"
                     style="height: 3em; width: 3em; font-size: 1em; line-height: 1">
                <p style="margin-top: 0.1em; margin-bottom: 0; font-size: 1.5em; font-weight: 400; line-height: 1">
                  {{ userStore.user.firstName.charAt(0) + userStore.user.lastName.charAt(0) }}</p>
                <q-menu transition-show="jump-down" transition-hide="jump-up" style="background-color: var(--bg-color)">
                  <q-list>
                    <q-item to="/profile" clickable v-close-popup>
                      <q-item-section text-color="black" style="width: 7.5em" unelevated>
                        <div>
                          <q-icon left name="person"/>
                          {{ t('profile.title') }}
                        </div>
                      </q-item-section>
                    </q-item>
                    <q-item @click="logout" clickable v-close-popup>
                      <q-item-section style="width: 7.5em" unelevated>
                        <div>
                          <q-icon left name="logout"/>
                          {{ t('home.logout') }}
                        </div>
                      </q-item-section>
                    </q-item>
                  </q-list>
                </q-menu>
              </q-btn>
              <q-btn v-if="!userStore.authenticated" to="/login" style="height: 3em; width: 3em" unelevated>
                <q-icon name="login" size="2em"/>
              </q-btn>
            </div>
          </div>
        </div>
      </q-toolbar>
    </q-header>

    <q-page-container class="page">
      <router-view/>
    </q-page-container>

    <q-footer elevated>
      <q-toolbar class="bg-dark">
        <q-toolbar-title style="text-align: center; font-size: 1em">

          <router-link to="/imprint" style="color: white">{{ t('home.imprint') }}</router-link>

        </q-toolbar-title>
      </q-toolbar>
    </q-footer>

  </q-layout>

</template>

<style scoped>
:root {
  --bg-color: #ffffff;
  --text-color: #000000;
}

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
  text-decoration: none;
}

.foldMenu {
  cursor: pointer;
}

.page {
  background-color: var(--bg-color);
  color: var(--text-color);
}
</style>
