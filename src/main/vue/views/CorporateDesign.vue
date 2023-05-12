<template>
  <q-page class="column justify-evenly" style="padding: 0 5em" >
    <p class="row text-h3 justify-center">{{t('corporateDesign.title')}}</p>
    <div class="row self-center no-wrap">
      <!-- <q-expansion-item icon="colorize" :label="t('corporateDesign.colors')"> -->
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
      <!-- </q-expansion-item> -->
    </div>
    <div class="row justify-center" >

    </div>
  </q-page>
</template>

<script>
import { ref } from 'vue'
import {useI18n} from "vue-i18n"
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
    }
  },
  data() {
    const { t } = useI18n()

    return {
      t
    }
  }
}
</script>

<style scoped>

</style>