<template>
  <q-page class="column justify-evenly" style="padding: 0 5em" >
    <p class="row text-h3 justify-center">{{t('corporateDesign.title')}}</p>
    <div class="row self-center no-wrap">
      <div class="column q-mx-xs">
        <q-input label="Primary" rounded outlined v-model="primary" class="my-input q-my-xs" bg-color="primary" style="width: fit-content">
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="primary" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-input label="Secondary" rounded outlined v-model="secondary" class="my-input q-my-xs" bg-color="secondary" style="width: fit-content">
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="secondary" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-input label="Accent" rounded outlined v-model="accent" class="my-input q-my-xs" bg-color="accent" style="width: fit-content">
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="accent" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
      </div>
      <div class="column q-mx-xs">
        <q-input label="Dark" rounded outlined v-model="dark" class="my-input q-my-xs" bg-color="dark" style="width: fit-content">
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="dark" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-input label="Light-Blue" rounded outlined v-model="lightBlue" class="my-input q-my-xs" bg-color="light-blue" style="width: fit-content">
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="lightBlue" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-input label="Positive" rounded outlined v-model="positive" class="my-input q-my-xs" bg-color="positive" style="width: fit-content">
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="positive" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
      </div>
      <div class="column q-mx-xs">
        <q-input label="Negative" rounded outlined v-model="negative" class="my-input q-my-xs" bg-color="negative" style="width: fit-content">
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="negative" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-input label="Info" rounded outlined v-model="info" class="my-input q-my-xs" bg-color="info" style="width: fit-content">
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="info" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-input label="Warning" rounded outlined v-model="warning" class="my-input q-my-xs" bg-color="warning" style="width: fit-content">
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="warning" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
      </div>
      <q-icon @mouseover="menu = true" @mouseleave="menu = false" name="info_outlined" color="info">
        <q-menu v-model="menu">
          <q-card class="q-pa-sm bg-grey-10" style="width: fit-content; height: fit-content">
            <p class="no-wrap q-my-xs" :style="{color: primary}">Primary: banner, buttons</p>
            <p class="no-wrap q-my-xs" :style="{color: secondary}">Secondary: badges (roles)</p>
            <p class="no-wrap q-my-xs" :style="{color: accent}">Accent: ...</p>
            <p class="no-wrap q-my-xs" :style="{color: dark}">Dark: ...</p>
            <p class="no-wrap q-my-xs" :style="{color: lightBlue}">Light-Blue: ...</p>
            <p class="no-wrap q-my-xs" :style="{color: positive}">Positive: positive notifications</p>
            <p class="no-wrap q-my-xs" :style="{color: negative}">Negative: negative notifications</p>
            <p class="no-wrap q-my-xs" :style="{color: info}">Info: profile button, informational content</p>
            <p class="no-wrap q-my-xs" :style="{color: warning}">Warning: warnings</p>
          </q-card>
        </q-menu>
      </q-icon>
    </div>
    <div class="row justify-evenly no-wrap" >
      <q-btn @click="confirmReset = true" round icon="restart_alt" color="negative" text-color="positive" style="width: 4em; height: 4em"/>
      <q-file rounded outlined :label="t('corporateDesign.logo')" v-model="logo" accept=".svg" :hint="t('corporateDesign.logoHint')">
        <template v-slot:prepend>
          <q-icon name="attach_file" />
        </template>
      </q-file>
      <q-file rounded outlined :label="t('corporateDesign.favicon')" v-model="favicon" accept=".ico" :hint="t('corporateDesign.faviconHint')">
        <template v-slot:prepend>
          <q-icon name="attach_file" />
        </template>
      </q-file>
      <q-btn @click="confirmSave = true" round icon="save" color="positive" text-color="negative" style="width: 4em; height: 4em"/>
    </div>

    <q-dialog v-model="confirmReset" persistent>
      <q-card>
        <q-card-section>
          <div class="text-h6">{{ t('corporateDesign.confirm.reset.title') }}</div>
        </q-card-section>
        <q-card-section class="row items-center">
          <span class="q-ml-sm">{{t('corporateDesign.confirm.reset.message')}}</span>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat :label="t('corporateDesign.confirm.reset.cancel')" color="primary" v-close-popup />
          <q-btn @click="reset" flat :label="t('corporateDesign.confirm.reset.ok')" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <q-dialog v-model="confirmSave" persistent>
      <q-card>
        <q-card-section>
          <div class="text-h6">{{ t('corporateDesign.confirm.save.title') }}</div>
        </q-card-section>
        <q-card-section class="row items-center">
          <span class="q-ml-sm">{{t('corporateDesign.confirm.save.message')}}</span>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat :label="t('corporateDesign.confirm.save.cancel')" color="primary" v-close-popup />
          <q-btn @click="save" flat :label="t('corporateDesign.confirm.save.ok')" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>

  </q-page>

</template>

<script>
import { ref } from 'vue'
import { useI18n } from "vue-i18n"
import {colors, useQuasar} from "quasar"
import corpdesign from "@/main/vue/api/corpdesign";

export default {
  name: "CorporateDesign",
  setup() {
    return {
      primary: ref(colors.getPaletteColor('primary')),
      secondary: ref(colors.getPaletteColor('secondary')),
      accent: ref(colors.getPaletteColor('accent')),
      dark: ref(colors.getPaletteColor('dark')),
      lightBlue: ref(colors.getPaletteColor('light-blue')),
      positive: ref(colors.getPaletteColor('positive')),
      negative: ref(colors.getPaletteColor('negative')),
      info: ref(colors.getPaletteColor('info')),
      warning: ref(colors.getPaletteColor('warning')),
      confirmReset: ref(false),
      confirmSave: ref(false)
    }
  },
  data() {
    const { t } = useI18n()
    const logo = ref(null)
    const favicon = ref(null)
    const $q = useQuasar()
    let menu = false

    function reset() {
      $q.notify({
        message: t('corporateDesign.resetSuccess'),
        color: "positive",
        textColor: "negative",
        timeout: "1500"
      })
    }
    function save() {
      corpdesign.saveLogo(logo.value)
      corpdesign.saveFavicon(favicon.value)
      $q.notify({
        message: t('corporateDesign.saveSuccess'),
        color: "positive",
        textColor: "negative",
        timeout: "1500"
      })
    }

    return {
      t,
      logo,
      favicon,
      reset,
      save,
      menu,
      colors
    }
  }
}
</script>

<style scoped>

</style>