<template>
  <q-page class="column justify-evenly" style="padding: 0 5em" >
    <p class="row text-h3 justify-center">{{t('corporateDesign.title')}}</p>
    <div class="row self-center no-wrap">
      <div class="column q-mx-xs">
        <q-input rounded outlined v-model="primary" class="my-input q-my-xs" bg-color="primary" style="width: fit-content">
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="primary" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-input rounded outlined v-model="secondary" class="my-input q-my-xs" bg-color="secondary" style="width: fit-content">
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="secondary" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-input rounded outlined v-model="accent" class="my-input q-my-xs" bg-color="accent" style="width: fit-content">
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
        <q-input rounded outlined v-model="dark" class="my-input q-my-xs" bg-color="dark" style="width: fit-content">
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="dark" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-input rounded outlined v-model="lightBlue" class="my-input q-my-xs" bg-color="light-blue" style="width: fit-content">
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="lightBlue" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-input rounded outlined v-model="positive" class="my-input q-my-xs" bg-color="positive" style="width: fit-content">
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
        <q-input rounded outlined v-model="negative" class="my-input q-my-xs" bg-color="negative" style="width: fit-content">
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="negative" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-input rounded outlined v-model="info" class="my-input q-my-xs" bg-color="info" style="width: fit-content">
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="info" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-input rounded outlined v-model="warning" class="my-input q-my-xs" bg-color="warning" style="width: fit-content">
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="warning" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
      </div>
    </div>
    <div class="row justify-center" >
      <q-btn @click="confirm = true" round icon="restart_alt" color="negative" style="width: 4em; height: 4em; margin-right: 4em"/>
      <q-file class="q-mx-xs" hide-bottom-space hide-hint rounded outlined :label="t('corporateDesign.logo')" v-model="logo" accept=".svg" hint="Only SVG files">
        <template v-slot:prepend>
          <q-icon name="attach_file" />
        </template>
      </q-file>
      <q-file class="q-mx-xs" hide-hint rounded outlined :label="t('corporateDesign.favicon')" v-model="favicon" accept=".ico" hint="Only ICO files">
        <template v-slot:prepend>
          <q-icon name="attach_file" />
        </template>
      </q-file>
    </div>

    <q-dialog v-model="confirm" persistent>
      <q-card>
        <q-card-section>
          <div class="text-h6">{{ t('corporateDesign.confirm.title') }}</div>
        </q-card-section>
        <q-card-section class="row items-center">
          <span class="q-ml-sm">{{t('corporateDesign.confirm.message')}}</span>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat :label="t('corporateDesign.confirm.cancel')" color="primary" v-close-popup />
          <q-btn flat :label="t('corporateDesign.confirm.ok')" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>

</template>

<script>
import { ref } from 'vue'
import { useI18n } from "vue-i18n"
import {colors} from "quasar"

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
      confirm: ref(false)
    }
  },
  data() {
    const { t } = useI18n()
    const logo = new Image()
    const favicon = new Image()

    return {
      t,
      logo,
      favicon
    }
  }
}
</script>

<style scoped>

</style>