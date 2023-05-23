<template>
  <q-page class="column justify-evenly" style="padding: 0 5em" >
    <p class="row text-h3 justify-center">{{t('corporateDesign.title')}}</p>
    <div class="row self-center no-wrap">
      <div class="column q-mx-xs">
        <q-input label="Primary" label-color="grey-10" rounded outlined v-model="primary" class="my-input q-my-xs" style="width: 14em">
          <template v-slot:prepend>
            <q-badge rounded :style="{width: '3em', height: '3em', backgroundColor: primary, borderRadius: '100%'}"/>
          </template>
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="primary" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-input label="Secondary" label-color="grey-10" rounded outlined v-model="secondary" class="my-input q-my-xs" style="width: 14em">
          <template v-slot:prepend>
            <q-badge rounded :style="{width: '3em', height: '3em', backgroundColor: secondary, borderRadius: '100%'}"/>
          </template>
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="secondary" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-input label="Accent" label-color="grey-10" rounded outlined v-model="accent" class="my-input q-my-xs" style="width: 14em">
          <template v-slot:prepend>
            <q-badge rounded :style="{width: '3em', height: '3em', backgroundColor: accent, borderRadius: '100%'}"/>
          </template>
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
        <q-input label="Dark" label-color="grey-10" rounded outlined v-model="dark" class="my-input q-my-xs" style="width: 14em">
          <template v-slot:prepend>
            <q-badge rounded :style="{width: '3em', height: '3em', backgroundColor: dark, borderRadius: '100%'}"/>
          </template>
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="dark" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-input label="Light-Blue" label-color="grey-10" rounded outlined v-model="lightBlue" class="my-input q-my-xs" style="width: 14em">
          <template v-slot:prepend>
            <q-badge rounded :style="{width: '3em', height: '3em', backgroundColor: lightBlue, borderRadius: '100%'}"/>
          </template>
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="lightBlue" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-input label="Positive" label-color="grey-10" rounded outlined v-model="positive" class="my-input q-my-xs" style="width: 14em">
          <template v-slot:prepend>
            <q-badge rounded :style="{width: '3em', height: '3em', backgroundColor: positive, borderRadius: '100%'}"/>
          </template>
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
        <q-input label="Negative" label-color="grey-10" rounded outlined v-model="negative" class="my-input q-my-xs" style="width: 14em">
          <template v-slot:prepend>
            <q-badge rounded :style="{width: '3em', height: '3em', backgroundColor: negative, borderRadius: '100%'}"/>
          </template>
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="negative" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-input label="Info" label-color="grey-10" rounded outlined v-model="info" class="my-input q-my-xs" style="width: 14em">
          <template v-slot:prepend>
            <q-badge rounded :style="{width: '3em', height: '3em', backgroundColor: info, borderRadius: '100%'}"/>
          </template>
          <template v-slot:append>
            <q-icon name="colorize" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-color v-model="info" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-input label="Warning" label-color="grey-10" rounded outlined v-model="warning" class="my-input q-my-xs" style="width: 14em">
          <template v-slot:prepend>
            <q-badge rounded :style="{width: '3em', height: '3em', backgroundColor: warning, borderRadius: '100%'}"/>
          </template>
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
    <div class="row justify-center no-wrap" >
      <q-input label="Background" rounded outlined v-model="bgColor" class="my-input q-my-xs q-mx-xs" style="width: 14em">
        <template v-slot:prepend>
          <q-badge rounded :style="{width: '3em', height: '3em', backgroundColor: bgColor, borderRadius: '100%'}"/>
        </template>
        <template v-slot:append>
          <q-icon name="colorize" class="cursor-pointer">
            <q-popup-proxy cover transition-show="scale" transition-hide="scale">
              <q-color v-model="bgColor" />
            </q-popup-proxy>
          </q-icon>
        </template>
      </q-input>
      <q-input label="Text-Color" rounded outlined v-model="fontColor" class="my-input q-my-xs q-mx-xs" style="width: 14em">
        <template v-slot:prepend>
          <q-badge rounded :style="{width: '3em', height: '3em', backgroundColor: fontColor, borderRadius: '100%'}"/>
        </template>
        <template v-slot:append>
          <q-icon name="colorize" class="cursor-pointer">
            <q-popup-proxy cover transition-show="scale" transition-hide="scale">
              <q-color v-model="fontColor" />
            </q-popup-proxy>
          </q-icon>
        </template>
      </q-input>
    </div>
    <div class="row justify-evenly no-wrap" >
      <q-btn @click="confirmReset = true" round icon="restart_alt" color="negative" text-color="positive" style="width: 4em; height: 4em"/>
      <q-file rounded outlined :label="t('corporateDesign.logo')" v-model="logo" accept=".svg" :hint="t('corporateDesign.logoHint')" bg-color="white" style="width: 14em">
        <template v-slot:prepend>
          <q-icon name="attach_file" />
        </template>
      </q-file>
      <q-file rounded outlined :label="t('corporateDesign.favicon')" v-model="favicon" accept=".ico" :hint="t('corporateDesign.faviconHint')" bg-color="white" style="width: 14em">
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
import {useQuasar} from "quasar"
import corpdesign from "@/main/vue/api/corpdesign";

export default {
  name: "CorporateDesign",
  setup() {
    return {
      confirmReset: ref(false),
      confirmSave: ref(false)
    }
  },
  data() {
    const { t } = useI18n()
    const logo = ref(null)
    const favicon = ref(null)
    const $q = useQuasar()
    const r = document.querySelector(':root')
    let menu = false
    let primary = ref('')
    let secondary = ref('')
    let accent = ref('')
    let dark = ref('')
    let lightBlue = ref('')
    let positive = ref('')
    let negative = ref('')
    let info = ref('')
    let warning = ref('')
    let bgColor = ref('')
    let fontColor = ref('')
    corpdesign.getColors().then( colors => {
      bgColor.value = colors.data.bgC
      fontColor.value = colors.data.textC
      primary.value = colors.data.primaryColor
      secondary.value = colors.data.secondary
      accent.value = colors.data.accent
      dark.value = colors.data.dark
      lightBlue.value = colors.data.lightBlue
      positive.value = colors.data.positive
      negative.value = colors.data.negative
      info.value = colors.data.info
      warning.value = colors.data.warning
    })

    function reset() {
      corpdesign.reset().then( colors => {
        corpdesign.setColors()
        r.style.setProperty('--bg-color', colors.data.bgC)
        r.style.setProperty('--text-color', colors.data.textC)
        location.reload()
      }).catch( () => {
        $q.notify({
          message: t('corporateDesign.resetFailure'),
          color: "negative",
          textColor: "positive",
          timeout: "1500"
        })
      })
    }
    function save() {
      if (logo.value != null)
        corpdesign.saveLogo(logo.value)
      if (favicon.value != null)
        corpdesign.saveFavicon(favicon.value)
      corpdesign.saveColors({
        bgC: bgColor.value,
        textC: fontColor.value,
        primaryColor: primary.value,
        secondary: secondary.value,
        accent: accent.value,
        dark: dark.value,
        lightBlue: lightBlue.value,
        positive: positive.value,
        negative: negative.value,
        info: info.value,
        warning: warning.value,
      }).then( colors => {
        corpdesign.setColors()
        r.style.setProperty('--bg-color', colors.data.bgC)
        r.style.setProperty('--text-color', colors.data.textC)
        location.reload()
      }).catch( () => {
        $q.notify({
          message: t('corporateDesign.saveFailure'),
          color: "negative",
          textColor: "positive",
          timeout: "1500"
        })
      })
    }

    return {
      t,
      logo,
      favicon,
      reset,
      save,
      menu,
      primary,
      secondary,
      accent,
      dark,
      lightBlue,
      positive,
      negative,
      info,
      warning,
      bgColor,
      fontColor,
      r
    }
  }
}
</script>

<style scoped>

</style>