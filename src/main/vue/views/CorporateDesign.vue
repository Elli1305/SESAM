<template>
  <q-page class="column justify-evenly items-center" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{ t('admin.corporateDesign.title') }}

      <q-icon @mouseover="menu = true" @mouseleave="menu = false" name="info_outlined" color="info" size="0.3em">
        <q-menu v-model="menu">
          <q-card class="column q-pa-sm no-wrap" style="width: fit-content; height: fit-content; background-color: var(--bg-color); color: var(--text-color)">
            <div class="row no-wrap q-my-xs">
              <p class="row items-center no-wrap q-ma-none">
                <q-badge class="q-mr-xs" rounded
                         :style="{width: '1em', height: '1em', backgroundColor: primaryLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                Primary:
              </p>
              <p class="row no-wrap q-ml-xs q-ma-none">{{ t('admin.corporateDesign.info.primary') }}</p>
            </div>
            <div class="row no-wrap q-my-xs">
              <p class="row items-center no-wrap q-ma-none">
                <q-badge class="q-mr-xs" rounded
                         :style="{width: '1em', height: '1em', backgroundColor: secondaryLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                Secondary:
              </p>
              <p class="row no-wrap q-ml-xs q-ma-none">{{ t('admin.corporateDesign.info.secondary') }}</p>
            </div>
            <div class="row no-wrap q-my-xs">
              <p class="row items-center no-wrap q-ma-none">
                <q-badge class="q-mr-xs" rounded
                         :style="{width: '1em', height: '1em', backgroundColor: accentLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                Accent:
              </p>
              <p class="row no-wrap q-ml-xs q-ma-none">{{ t('admin.corporateDesign.info.accent') }}</p>
            </div>
            <div class="row no-wrap q-my-xs">
              <p class="row items-center no-wrap q-ma-none">
                <q-badge class="q-mr-xs" rounded
                         :style="{width: '1em', height: '1em', backgroundColor: darkLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                Dark:
              </p>
              <p class="row no-wrap q-ml-xs q-ma-none">{{ t('admin.corporateDesign.info.dark') }}</p>
            </div>
            <div class="row no-wrap q-my-xs">
              <p class="row items-center no-wrap q-ma-none">
                <q-badge class="q-mr-xs" rounded
                         :style="{width: '1em', height: '1em', backgroundColor: lightLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                Light:
              </p>
              <p class="row no-wrap q-ml-xs q-ma-none">{{ t('admin.corporateDesign.info.light') }}</p>
            </div>
            <div class="row no-wrap q-my-xs">
              <p class="row items-center no-wrap q-ma-none">
                <q-badge class="q-mr-xs" rounded
                         :style="{width: '1em', height: '1em', backgroundColor: positiveLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                Positive:
              </p>
              <p class="row no-wrap q-ml-xs q-ma-none">{{ t('admin.corporateDesign.info.positive') }}</p>
            </div>
            <div class="row no-wrap q-my-xs">
              <p class="row items-center no-wrap q-ma-none">
                <q-badge class="q-mr-xs" rounded
                         :style="{width: '1em', height: '1em', backgroundColor: negativeLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                Negative:
              </p>
              <p class="row no-wrap q-ml-xs q-ma-none">{{ t('admin.corporateDesign.info.negative') }}</p>
            </div>
            <div class="row no-wrap q-my-xs">
              <p class="row items-center no-wrap q-ma-none">
                <q-badge class="q-mr-xs" rounded
                         :style="{width: '1em', height: '1em', backgroundColor: infoLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                Info:
              </p>
              <p class="row no-wrap q-ml-xs q-ma-none">{{ t('admin.corporateDesign.info.info') }}</p>
            </div>
            <div class="row no-wrap q-my-xs">
              <p class="row items-center no-wrap q-ma-none">
                <q-badge class="q-mr-xs" rounded
                         :style="{width: '1em', height: '1em', backgroundColor: warningLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                Warning:
              </p>
              <p class="row no-wrap q-ml-xs q-ma-none">{{ t('admin.corporateDesign.info.warning') }}</p>
            </div>
          </q-card>
        </q-menu>
      </q-icon>
    </p>
    <div class="column">
      <q-tabs
          v-model="tab"
          dense
          class="text-grey"
          active-color="primary"
          indicator-color="primary"
          align="justify">
        <q-tab name="light" label="Light"/>
        <q-tab name="dark" label="Dark"/>
      </q-tabs>

      <q-separator/>

      <q-tab-panels v-model="tab" animated style="width: 80vw; height: 50vh; background-color: var(--bg-color); color: var(--text-color)">
        <q-tab-panel name="light" class="q-py-md q-px-none">
          <div class="column justify-between no-wrap full-height">
            <div class="row justify-center no-wrap">
              <div class="column q-mx-xs">
                <q-input label="Primary" rounded outlined v-model="primaryLight"
                         class="my-input q-my-xs" style="width: 14em">
                  <template v-slot:prepend>
                    <q-badge rounded
                             :style="{width: '3em', height: '3em', backgroundColor: primaryLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                  </template>
                  <template v-slot:append>
                    <q-icon name="colorize" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-color v-model="primaryLight"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
                <q-input label="Secondary" rounded outlined v-model="secondaryLight"
                         class="my-input q-my-xs" style="width: 14em">
                  <template v-slot:prepend>
                    <q-badge rounded
                             :style="{width: '3em', height: '3em', backgroundColor: secondaryLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                  </template>
                  <template v-slot:append>
                    <q-icon name="colorize" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-color v-model="secondaryLight"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
                <q-input label="Accent" rounded outlined v-model="accentLight" class="my-input q-my-xs"
                         style="width: 14em">
                  <template v-slot:prepend>
                    <q-badge rounded
                             :style="{width: '3em', height: '3em', backgroundColor: accentLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                  </template>
                  <template v-slot:append>
                    <q-icon name="colorize" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-color v-model="accentLight"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
              </div>
              <div class="column q-mx-xs">
                <q-input label="Dark" rounded outlined v-model="darkLight" class="my-input q-my-xs"
                         style="width: 14em">
                  <template v-slot:prepend>
                    <q-badge rounded
                             :style="{width: '3em', height: '3em', backgroundColor: darkLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                  </template>
                  <template v-slot:append>
                    <q-icon name="colorize" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-color v-model="darkLight"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
                <q-input label="Light-Blue" rounded outlined v-model="lightLight"
                         class="my-input q-my-xs" style="width: 14em">
                  <template v-slot:prepend>
                    <q-badge rounded
                             :style="{width: '3em', height: '3em', backgroundColor: lightLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                  </template>
                  <template v-slot:append>
                    <q-icon name="colorize" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-color v-model="lightLight"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
                <q-input label="Positive" rounded outlined v-model="positiveLight"
                         class="my-input q-my-xs" style="width: 14em">
                  <template v-slot:prepend>
                    <q-badge rounded
                             :style="{width: '3em', height: '3em', backgroundColor: positiveLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                  </template>
                  <template v-slot:append>
                    <q-icon name="colorize" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-color v-model="positiveLight"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
              </div>
              <div class="column q-mx-xs">
                <q-input label="Negative" rounded outlined v-model="negativeLight"
                         class="my-input q-my-xs" style="width: 14em">
                  <template v-slot:prepend>
                    <q-badge rounded
                             :style="{width: '3em', height: '3em', backgroundColor: negativeLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                  </template>
                  <template v-slot:append>
                    <q-icon name="colorize" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-color v-model="negativeLight"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
                <q-input label="Info" rounded outlined v-model="infoLight" class="my-input q-my-xs"
                         style="width: 14em">
                  <template v-slot:prepend>
                    <q-badge rounded
                             :style="{width: '3em', height: '3em', backgroundColor: infoLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                  </template>
                  <template v-slot:append>
                    <q-icon name="colorize" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-color v-model="infoLight"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
                <q-input label="Warning" rounded outlined v-model="warningLight"
                         class="my-input q-my-xs" style="width: 14em">
                  <template v-slot:prepend>
                    <q-badge rounded
                             :style="{width: '3em', height: '3em', backgroundColor: warningLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                  </template>
                  <template v-slot:append>
                    <q-icon name="colorize" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-color v-model="warningLight"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
              </div>
            </div>
            <div class="row justify-center no-wrap">
              <q-input :label="t('admin.corporateDesign.background')" rounded outlined v-model="bgColorLight"
                       class="my-input q-my-xs q-mx-xs" style="width: 14em">
                <template v-slot:prepend>
                  <q-badge rounded
                           :style="{width: '3em', height: '3em', backgroundColor: bgColorLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                </template>
                <template v-slot:append>
                  <q-icon name="colorize" class="cursor-pointer">
                    <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                      <q-color v-model="bgColorLight"/>
                    </q-popup-proxy>
                  </q-icon>
                </template>
              </q-input>
              <q-input :label="t('admin.corporateDesign.textColor')" rounded outlined v-model="fontColorLight"
                       class="my-input q-my-xs q-mx-xs" style="width: 14em">
                <template v-slot:prepend>
                  <q-badge rounded
                           :style="{width: '3em', height: '3em', backgroundColor: fontColorLight, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                </template>
                <template v-slot:append>
                  <q-icon name="colorize" class="cursor-pointer">
                    <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                      <q-color v-model="fontColorLight"/>
                    </q-popup-proxy>
                  </q-icon>
                </template>
              </q-input>
            </div>
            <div class="row justify-evenly no-wrap">
              <q-btn @click="confirmReset = true" round icon="restart_alt" color="negative" text-color="positive"
                     style="width: 4em; height: 4em"/>
              <q-file rounded outlined :label="t('admin.corporateDesign.logo')" v-model="logo" accept=".svg"
                      :hint="t('admin.corporateDesign.logoHint')" style="width: 14em; background-color: var(--bg-color); color: var(--text-color)">
                <template v-slot:prepend>
                  <q-icon name="attach_file"/>
                </template>
              </q-file>
              <q-file rounded outlined :label="t('admin.corporateDesign.favicon')" v-model="favicon" accept=".ico"
                      :hint="t('admin.corporateDesign.faviconHint')" style="width: 14em; background-color: var(--bg-color); color: var(--text-color)">
                <template v-slot:prepend>
                  <q-icon name="attach_file"/>
                </template>
              </q-file>
              <q-btn @click="confirmSave = true" round icon="save" color="positive" text-color="negative"
                     style="width: 4em; height: 4em"/>
            </div>
          </div>
        </q-tab-panel>

        <q-tab-panel name="dark" class="q-py-md q-px-none">
          <div class="column justify-between no-wrap full-height">
            <div class="row justify-center no-wrap">
              <div class="column q-mx-xs">
                <q-input label="Primary" rounded outlined v-model="primaryDark"
                         class="my-input q-my-xs" style="width: 14em">
                  <template v-slot:prepend>
                    <q-badge rounded
                             :style="{width: '3em', height: '3em', backgroundColor: primaryDark, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                  </template>
                  <template v-slot:append>
                    <q-icon name="colorize" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-color v-model="primaryDark"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
                <q-input label="Secondary" rounded outlined v-model="secondaryDark"
                         class="my-input q-my-xs" style="width: 14em">
                  <template v-slot:prepend>
                    <q-badge rounded
                             :style="{width: '3em', height: '3em', backgroundColor: secondaryDark, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                  </template>
                  <template v-slot:append>
                    <q-icon name="colorize" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-color v-model="secondaryDark"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
                <q-input label="Accent" rounded outlined v-model="accentDark" class="my-input q-my-xs"
                         style="width: 14em">
                  <template v-slot:prepend>
                    <q-badge rounded
                             :style="{width: '3em', height: '3em', backgroundColor: accentDark, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                  </template>
                  <template v-slot:append>
                    <q-icon name="colorize" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-color v-model="accentDark"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
              </div>
              <div class="column q-mx-xs">
                <q-input label="Dark" rounded outlined v-model="darkDark" class="my-input q-my-xs"
                         style="width: 14em">
                  <template v-slot:prepend>
                    <q-badge rounded
                             :style="{width: '3em', height: '3em', backgroundColor: darkDark, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                  </template>
                  <template v-slot:append>
                    <q-icon name="colorize" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-color v-model="darkDark"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
                <q-input label="Light-Blue" rounded outlined v-model="lightDark"
                         class="my-input q-my-xs" style="width: 14em">
                  <template v-slot:prepend>
                    <q-badge rounded
                             :style="{width: '3em', height: '3em', backgroundColor: lightDark, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                  </template>
                  <template v-slot:append>
                    <q-icon name="colorize" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-color v-model="lightDark"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
                <q-input label="Positive" rounded outlined v-model="positiveDark"
                         class="my-input q-my-xs" style="width: 14em">
                  <template v-slot:prepend>
                    <q-badge rounded
                             :style="{width: '3em', height: '3em', backgroundColor: positiveDark, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                  </template>
                  <template v-slot:append>
                    <q-icon name="colorize" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-color v-model="positiveDark"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
              </div>
              <div class="column q-mx-xs">
                <q-input label="Negative" rounded outlined v-model="negativeDark"
                         class="my-input q-my-xs" style="width: 14em">
                  <template v-slot:prepend>
                    <q-badge rounded
                             :style="{width: '3em', height: '3em', backgroundColor: negativeDark, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                  </template>
                  <template v-slot:append>
                    <q-icon name="colorize" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-color v-model="negativeDark"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
                <q-input label="Info" rounded outlined v-model="infoDark" class="my-input q-my-xs"
                         style="width: 14em">
                  <template v-slot:prepend>
                    <q-badge rounded
                             :style="{width: '3em', height: '3em', backgroundColor: infoDark, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                  </template>
                  <template v-slot:append>
                    <q-icon name="colorize" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-color v-model="infoDark"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
                <q-input label="Warning" rounded outlined v-model="warningDark"
                         class="my-input q-my-xs" style="width: 14em">
                  <template v-slot:prepend>
                    <q-badge rounded
                             :style="{width: '3em', height: '3em', backgroundColor: warningDark, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                  </template>
                  <template v-slot:append>
                    <q-icon name="colorize" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-color v-model="warningDark"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
              </div>
            </div>
            <div class="row justify-center no-wrap">
              <q-input :label="t('admin.corporateDesign.background')" rounded outlined v-model="bgColorDark"
                       class="my-input q-my-xs q-mx-xs" style="width: 14em">
                <template v-slot:prepend>
                  <q-badge rounded
                           :style="{width: '3em', height: '3em', backgroundColor: bgColorDark, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                </template>
                <template v-slot:append>
                  <q-icon name="colorize" class="cursor-pointer">
                    <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                      <q-color v-model="bgColorDark"/>
                    </q-popup-proxy>
                  </q-icon>
                </template>
              </q-input>
              <q-input :label="t('admin.corporateDesign.textColor')" rounded outlined v-model="fontColorDark"
                       class="my-input q-my-xs q-mx-xs" style="width: 14em">
                <template v-slot:prepend>
                  <q-badge rounded
                           :style="{width: '3em', height: '3em', backgroundColor: fontColorDark, borderRadius: '100%', border: '1px solid lightgrey'}"/>
                </template>
                <template v-slot:append>
                  <q-icon name="colorize" class="cursor-pointer">
                    <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                      <q-color v-model="fontColorDark"/>
                    </q-popup-proxy>
                  </q-icon>
                </template>
              </q-input>
            </div>
            <div class="row justify-evenly no-wrap">
              <q-btn @click="confirmReset = true" round icon="restart_alt" color="negative" text-color="positive"
                     style="width: 4em; height: 4em"/>
              <q-file rounded outlined :label="t('admin.corporateDesign.logo')" v-model="logo" accept=".svg"
                      :hint="t('admin.corporateDesign.logoHint')" style="width: 14em; background-color: var(--bg-color); color: var(--text-color)">
                <template v-slot:prepend>
                  <q-icon name="attach_file"/>
                </template>
              </q-file>
              <q-file rounded outlined :label="t('admin.corporateDesign.favicon')" v-model="favicon" accept=".ico"
                      :hint="t('admin.corporateDesign.faviconHint')" style="width: 14em; background-color: var(--bg-color); color: var(--text-color)">
                <template v-slot:prepend>
                  <q-icon name="attach_file"/>
                </template>
              </q-file>
              <q-btn @click="confirmSave = true" round icon="save" color="positive" text-color="negative"
                     style="width: 4em; height: 4em"/>
            </div>
          </div>
        </q-tab-panel>
      </q-tab-panels>
    </div>

    <q-dialog v-model="confirmReset" persistent>
      <q-card style="background-color: var(--bg-color); color: var(--text-color)">
        <q-card-section>
          <div class="text-h6">{{ t('admin.corporateDesign.reset.title') }}</div>
        </q-card-section>
        <q-card-section class="row items-center">
          <span class="q-ml-sm">{{ t('admin.corporateDesign.reset.message') }}</span>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat :label="t('common.cancel')" color="primary" v-close-popup/>
          <q-btn @click="reset" flat :label="t('admin.corporateDesign.reset.reset')" color="primary" v-close-popup/>
        </q-card-actions>
      </q-card>
    </q-dialog>

    <q-dialog v-model="confirmSave" persistent>
      <q-card style="background-color: var(--bg-color); color: var(--text-color)">
        <q-card-section>
          <div class="text-h6">{{ t('admin.corporateDesign.save.title') }}</div>
        </q-card-section>
        <q-card-section class="row items-center">
          <span class="q-ml-sm">{{ t('admin.corporateDesign.save.message') }}</span>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat :label="t('common.cancel')" color="primary" v-close-popup/>
          <q-btn @click="save" flat :label="t('common.save')" color="primary" v-close-popup/>
        </q-card-actions>
      </q-card>
    </q-dialog>

  </q-page>

</template>

<script>
import {ref} from 'vue'
import {useI18n} from "vue-i18n"
import {useQuasar} from "quasar"
import corpdesign from "@/main/vue/api/corpdesign";
import {useAppStore} from "@/main/vue/stores/app";

export default {
  name: "CorporateDesign",
  setup() {
    return {
      confirmReset: ref(false),
      confirmSave: ref(false)
    }
  },
  data() {
    const {t} = useI18n()
    const logo = ref(null)
    const favicon = ref(null)
    const $q = useQuasar()
    const appStore = useAppStore()
    const r = document.querySelector(':root')
    let menu = false
    let primaryLight = ref('')
    let secondaryLight = ref('')
    let accentLight = ref('')
    let darkLight = ref('')
    let lightLight = ref('')
    let positiveLight = ref('')
    let negativeLight = ref('')
    let infoLight = ref('')
    let warningLight = ref('')
    let bgColorLight = ref('')
    let fontColorLight = ref('')
    let primaryDark = ref('')
    let secondaryDark = ref('')
    let accentDark = ref('')
    let darkDark = ref('')
    let lightDark = ref('')
    let positiveDark = ref('')
    let negativeDark = ref('')
    let infoDark = ref('')
    let warningDark = ref('')
    let bgColorDark = ref('')
    let fontColorDark = ref('')
    let tab = ref('light')
    corpdesign.getColors('LIGHT').then(colors => {
      bgColorLight.value = colors.data.bgC
      fontColorLight.value = colors.data.textC
      primaryLight.value = colors.data.primaryColor
      secondaryLight.value = colors.data.secondary
      accentLight.value = colors.data.accent
      darkLight.value = colors.data.dark
      lightLight.value = colors.data.light
      positiveLight.value = colors.data.positive
      negativeLight.value = colors.data.negative
      infoLight.value = colors.data.info
      warningLight.value = colors.data.warning
    })
    corpdesign.getColors('DARK').then(colors => {
      bgColorDark.value = colors.data.bgC
      fontColorDark.value = colors.data.textC
      primaryDark.value = colors.data.primaryColor
      secondaryDark.value = colors.data.secondary
      accentDark.value = colors.data.accent
      darkDark.value = colors.data.dark
      lightDark.value = colors.data.light
      positiveDark.value = colors.data.positive
      negativeDark.value = colors.data.negative
      infoDark.value = colors.data.info
      warningDark.value = colors.data.warning
    })

    function reset() {
      const colorTheme = tab.value.toUpperCase()
      corpdesign.reset(colorTheme).then(c => {
        corpdesign.setColors(localStorage.getItem('colorTheme'))
        corpdesign.getColors(localStorage.getItem('colorTheme')).then(colors => {
          r.style.setProperty('--bg-color', colors.data.bgC)
          r.style.setProperty('--text-color', colors.data.textC)
          r.style.setProperty('--light', colors.data.light)
        })
        appStore.forceRender()
      }).catch(() => {
        $q.notify({
          message: t('admin.corporateDesign.resetFailure'),
          color: "negative",
          textColor: "positive",
          timeout: "1500"
        })
      })
    }

    function save() {
      if (logo.value != null)
        if (tab.value.toUpperCase() === 'LIGHT')
          corpdesign.saveLogoLight(logo.value)
        else
          corpdesign.saveLogoDark(logo.value)
      if (favicon.value != null)
        corpdesign.saveFavicon(favicon.value)
      const colorTheme = tab.value.toUpperCase()
      if (colorTheme === 'LIGHT') {
        corpdesign.saveColors(colorTheme, {
          theme: colorTheme,
          logoPath: '/Logo.svg',
          bgC: bgColorLight.value,
          textC: fontColorLight.value,
          primaryColor: primaryLight.value,
          secondary: secondaryLight.value,
          accent: accentLight.value,
          dark: darkLight.value,
          light: lightLight.value,
          positive: positiveLight.value,
          negative: negativeLight.value,
          info: infoLight.value,
          warning: warningLight.value,
        }).then(c => {
          corpdesign.setColors(localStorage.getItem('colorTheme'))
          corpdesign.getColors(localStorage.getItem('colorTheme')).then(colors => {
            r.style.setProperty('--bg-color', colors.data.bgC)
            r.style.setProperty('--text-color', colors.data.textC)
            r.style.setProperty('--light', colors.data.light)
          })
          appStore.forceRender()
        }).catch(() => {
          $q.notify({
            message: t('admin.corporateDesign.saveFailure'),
            color: "negative",
            textColor: "positive",
            timeout: "1500"
          })
        })
      } else if (colorTheme === 'DARK') {
        corpdesign.saveColors(colorTheme, {
          theme: colorTheme,
          logoPath: '/Logo-Dark.svg',
          bgC: bgColorDark.value,
          textC: fontColorDark.value,
          primaryColor: primaryDark.value,
          secondary: secondaryDark.value,
          accent: accentDark.value,
          dark: darkDark.value,
          light: lightDark.value,
          positive: positiveDark.value,
          negative: negativeDark.value,
          info: infoDark.value,
          warning: warningDark.value,
        }).then(c => {
          corpdesign.setColors(localStorage.getItem('colorTheme'))
          corpdesign.getColors(localStorage.getItem('colorTheme')).then(colors => {
            r.style.setProperty('--bg-color', colors.data.bgC)
            r.style.setProperty('--text-color', colors.data.textC)
            r.style.setProperty('--light', colors.data.light)
          })
          appStore.forceRender()
        }).catch(() => {
          $q.notify({
            message: t('admin.corporateDesign.saveFailure'),
            color: "negative",
            textColor: "positive",
            timeout: "1500"
          })
        })
      } else {
        console.error('Wrong color theme')
      }
    }

    return {
      t,
      logo,
      favicon,
      reset,
      save,
      menu,
      primaryLight,
      secondaryLight,
      accentLight,
      darkLight,
      lightLight,
      positiveLight,
      negativeLight,
      infoLight,
      warningLight,
      bgColorLight,
      fontColorLight,
      primaryDark,
      secondaryDark,
      accentDark,
      darkDark,
      lightDark,
      positiveDark,
      negativeDark,
      infoDark,
      warningDark,
      bgColorDark,
      fontColorDark,
      r,
      tab
    }
  }
}
</script>

<style scoped>

</style>