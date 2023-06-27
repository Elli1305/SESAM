<template>
  <q-dialog ref="dialog" @hide="onDialogHide">
    <q-card style="width: 40em">
      <q-card-section>
        <div class="text-h6">Tür zuweisen</div>
      </q-card-section>
      <q-card-section class="row justify-around no-wrap">
        <q-input style="width: 18em" filled v-model="doorName" label="Türname" stack-label/>
        <q-select
            style="width: 18em"
            filledv-for="(select,i) in qSelectgeneral.qSelectsSet[k]general.qSelectsSet[k].configParts"
            v-model="room"
            use-input
            hide-selected
            label="Raum auswählen"
            option-label="name"
            fill-input
            input-debounce="0"
            :options="rooms"
            v-if="!door"
            @filter="filterFn">
          <template v-slot:no-option>
            <q-item>
              <q-item-section class="text-grey">
                No results
              </q-item-section>
            </q-item>
          </template>
        </q-select>
      </q-card-section>
      <q-card-section  v-for="(selectConf,k) in qSelectgeneral.qSelectsSet">
        <q-card bordered flat>
          <q-toolbar class="bg-primary text-accent">



              <div class="colomn q-mt-sm justify-around items-center no-wrap">
                  <div class="row q-mt-sm justify-around items-center no-wrap">
                  <q-toolbar-title>Konfigurationsgruppen</q-toolbar-title>
                  <q-icon class="q-mr-xs" color="accent" size="1.25em" name="info_outlined">
                      <q-tooltip class="bg-grey-14" anchor="bottom middle" self="top middle" :offset="[0,0]">
                          Konfigurationsgruppen sind untereinander mit UND verknüpft
                      </q-tooltip>
                  </q-icon>
                  </div>

                  <div class="q-pa-md">
                      <div class="q-gutter-sm">
                          <q-checkbox v-model="qSelectgeneral.qSelectsSet[k].basisConf" label="Basis Konfiguration" color="primary" />
                      </div>
                  </div>
                  <div class="row q-mt-sm justify-around items-center no-wrap">
                      <div class="q-pa-sm row">
                          <q-input filled bg-color="white" v-model="qSelectgeneral.qSelectsSet[k].startTime" mask="time" :rules="['time']" :disable="qSelectgeneral.qSelectsSet[k].basisConf">
                              <template v-slot:append>
                                  <q-icon name="access_time" class="cursor-pointer">
                                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                          <q-time v-model="qSelectgeneral.qSelectsSet[k].startTime">
                                              <div class="row items-center justify-end">
                                                  <q-btn v-close-popup label="Close" color="primary" flat />
                                              </div>
                                          </q-time>
                                      </q-popup-proxy>
                                  </q-icon>
                              </template>
                          </q-input>
                      </div>

                      <div class="q-gutter-sm row">
                          <q-input filled bg-color="white" v-model="qSelectgeneral.qSelectsSet[k].endTime" mask="time" :rules="['time']" :disable="qSelectgeneral.qSelectsSet[k].basisConf">
                              <template v-slot:append>
                                  <q-icon name="access_time" class="cursor-pointer">
                                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                          <q-time v-model="qSelectgeneral.qSelectsSet[k].endTime">
                                              <div class="row items-center justify-end">
                                                  <q-btn v-close-popup label="Close" color="primary" flat />
                                              </div>
                                          </q-time>
                                      </q-popup-proxy>
                                  </q-icon>
                              </template>
                          </q-input>
                      </div>
                  </div>




                  </div>
          </q-toolbar>
          <q-card-section>
            <q-input filled v-model="qSelectgeneral.qSelectsSet[k].configDescription" label="Beschreibung der Konfiguration" stack-label/>
          </q-card-section>
          <q-card-section v-for="(select,i) in qSelectgeneral.qSelectsSet[k].configParts">
            <q-card bordered flat>
              <q-toolbar class="bg-primary text-white shadow-2">
                <q-toolbar-title>Konfiguration</q-toolbar-title>

                <q-btn flat round icon="delete" size="0.75em" @click="removeConfigGroup(i,k)"/>
              </q-toolbar>
              <q-card-section class="column">
                <q-select
                    class="q-mb-sm"
                    filled
                    multiple
                    label="Credentials"
                    option-label="name"
                    hint="Credentials in dieser Auswahl sind ODER-Verknüpft"
                    :options="credentialStore.allCredentials"
                    v-model="qSelectgeneral.qSelectsSet[k].configParts[i].credentials"
                    use-chips>
                  <template v-slot:selected-item="scope">
                    <q-chip
                        class="q-pa-sm"
                        style="line-height: 1"
                        :label="scope.opt.name"
                        :tabindex="scope.tabindex"
                        dense
                        removable
                        icon-remove="clear"
                        @remove="scope.removeAtIndex(scope.index)"/>
                  </template>
                  <template v-slot:no-option>
                    <q-item>
                      <q-item-section class="text-grey">
                        No results
                      </q-item-section>
                    </q-item>
                  </template>
                </q-select>
                <div class="row q-mt-sm justify-around items-center no-wrap"
                     v-for="(attributeFilter,j) in qSelectgeneral.qSelectsSet[k].configParts[i].attributeFilter" style="min-width: 100%">
                  <q-select style="width: 12em"
                      rounded outlined dropdown-icon="expand_more" v-model="qSelectgeneral.qSelectsSet[k].configParts[i].attributeFilter[j].attribute"
                      :options="commonAttributeFilter(qSelectgeneral.qSelectsSet[k].configParts[i].credentials)"
                      :display-value="qSelectgeneral.qSelectsSet[k].configParts[i].attributeFilter[j].attribute?.label"
                      @update:model-value="resetPredicateType(i,j,k)">

                  </q-select>
                  <q-select style="width: 5em"
                      rounded outlined dropdown-icon="expand_more" v-model="qSelectgeneral.qSelectsSet[k].configParts[i].attributeFilter[j].predicateType" ref="predicateType"
                      :options="getPredicates(qSelectgeneral.qSelectsSet[k].configParts[i].attributeFilter[j].attribute)"/>
                  <q-input style="width: 10em"
                      rounded outlined v-model="qSelectgeneral.qSelectsSet[k].configParts[i].attributeFilter[j].value"
                     :type="getType(qSelectgeneral.qSelectsSet[k].configParts[i].attributeFilter[j].attribute)"
                     :disable="qSelectgeneral.qSelectsSet[k].configParts[i].attributeFilter[j].currentDate" ref="input">
                  </q-input>
                  <q-btn style="width: 3em; height: 3em"
                      flat round icon="delete" @click="removeFilter(i,j,k)"/>
                </div>
              </q-card-section>
              <q-btn class="q-ml-sm q-mb-sm" flat dense rounded color="primary" icon="add" @click="addAttributeFilter(i,k)">Attribut hinzufügen</q-btn>
            </q-card>
          </q-card-section>
          <q-btn class="q-ml-sm q-mb-sm" flat dense rounded color="primary" icon="add" @click="addConfigurationGroup(k)">
            Konfigurationsgruppe hinzufügen
          </q-btn>
        </q-card>
      </q-card-section>
        <q-btn class="q-ml-sm q-mb-sm" flat dense rounded color="primary" icon="add" @click="addConfiguration">
            Konfiguration hinzufügen
        </q-btn>
      <q-card-actions align="right">
        <q-btn flat color="primary" label="Abbrechen" @click="onCancelClick"/>
        <q-btn flat color="primary" label="Speichern" :disable="!doorName || (!room && !door) || !checkBaseConf()" @click="onOKClick"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
import {ref} from "vue";
import {useCredentialStore} from "@/main/vue/stores/credential";
import {PredicateType} from "@/main/vue/entity/doorConfiguration";
import {useQuasar} from "quasar";
const starttime = ref()
const endtime = ref()
const $q = useQuasar()
export default {
  //propslist:{
      props: {
          rooms: Array,
          door: {
              required: false
          },
          doorConfig: {
              required: false
          }
      },
  //},

  emits: [
    'ok', 'hide'
  ],

  methods: {
    show() {
      this.$refs.dialog.show()
    },
    hide() {
      this.$refs.dialog.hide()
    },
    onDialogHide() {
      this.$emit('hide')
    },

    onOKClick(props) {
      if(this.checkBaseConf()){
          // muss ich das mit dem emit für jede
          for(const k of this.qSelectgeneral.qSelectsSet){
              this.$emit('ok', {
                  room: this.room,
                  doorName: this.doorName,
                  configuration: k,
                  configDescription: this.configDescription
              })
              this.hide()

              if (props.doorConfig) {
                  k.configDescription.value = props.doorConfig
              }

          }
      }
      else{
          $q.notify({
              type: 'negative',
              message: t('You must have at least one base Configuration'),
              caption: t('common.internalServerError'),
              position: "top",
              color: 'negative',
              textColor: 'postitive',
              timeout: 3000,
              classes: "loginNotify"
          })
      }
    },
    onCancelClick() {
      this.hide()
    },
    getPredicates(attribute) {
      if (attribute?.type.toLowerCase() === 'text') {
        return [PredicateType.EQUALS]
      } else if (attribute?.type.toLowerCase() === 'number' || attribute?.type.toLowerCase() === 'date') {
        return Object.values(PredicateType)
      }
    },
    resetPredicateType(i, j, k) {
      this.qSelectgeneral.qSelectsSet[k].configParts[i].attributeFilter[j].predicateType = null;
      this.qSelectgeneral.qSelectsSet[k].configParts[i].attributeFilter[j].value = null;
    },

    getType(attribute) {
      return attribute?.type.toLowerCase()
    },
    addAttributeFilter(i,k) {
      this.qSelectgeneral.qSelectsSet[k].configParts[i].attributeFilter.push({
        attribute: null,
        predicateType: null,
        value: null,
        currentDate: false
      })
    },
    removeConfigGroup(i, k) {
      this.qSelectgeneral.qSelectsSet[k].configParts.splice(i, 1)
    },
    removeFilter(i, j, k) {
      this.qSelectgeneral.qSelectsSet[k].configParts[i].attributeFilter.splice(j, 1)
    },
    addConfigurationGroup(k) {
      this.qSelectgeneral.qSelectsSet[k].configParts.push({
        credentials: [],
        attributeFilter: [{
          attribute: null,
          predicateType: null,
          value: null,
          currentDate: false
        }]
      })
    },
      addConfiguration() {
        this.qSelectgeneral.qSelectsSet.push({
            configDescription: null,
            basisConf: false,
            startTime: null,
            endTime: null,
            configParts: [{
                credentials: [],
                attributeFilter: [{
                    attribute: null,
                    predicateType: null,
                    value: null,
                    currentDate: false
                }]
            }]
        })

      },
      checkBaseConf(){
          let baseConfExists = false
      //check if one of the Konfigurations is a base one. USe basisConf
          for(const conf of this.qSelectgeneral.qSelectsSet){
              console.log(conf.basisConf.valueOf());
              if (conf.basisConf.valueOf() === true){
                  baseConfExists = true
              }

          }
          return baseConfExists
      },
    setDate(i, j) {
      if (this.qSelectgeneral.qSelectsSet[k].configParts[i].attributeFilter[j].currentDate) {
        const date = new Date();
        this.qSelectgeneral.qSelectsSet[k].configParts[i].attributeFilter[j].value = date.toISOString().split('T')[0];
      }
    }
  },
  setup(props) {
    const room = ref(null)
    const doorName = ref('')
    const roomOptions = ref(props.rooms)
    const credentialStore = useCredentialStore()
    credentialStore.getAllCredentials()
    const credentials = ref()
    //const configDescription = ref()
    const baseConfig = ref()

    const qSelects = {
        configDescription: null,
        basisConf: false,
        startTime: null,
      configParts: [{
        credentials: [],
        attributeFilter: [{
          attribute: null,
          predicateType: null,
          value: null,
          currentDate: false
        }]
      }]
    }

   const qSelectgeneral = ref({
       qSelectsSet: [qSelects]
   })



    if (props.door) {
      doorName.value = props.door.name
    }

    const filterFn = function (val, update, abort) {
      update(() => {
        const needle = val.toLowerCase()
        roomOptions.value = props.rooms.filter(room => room.name.toLowerCase().indexOf(needle) > -1)
      })
    }

    const commonAttributeFilter = function (credentials) {
      let formEntrys = credentials.map((credential) => {
        return credential.form
      })
      if (formEntrys.length > 1) {
        return formEntrys.shift().filter((v) => {
          return formEntrys.every((a) => {
            return a.some(ele => ele.attributeName === v.attributeName);
          });
        })
      }
      return formEntrys[0];
    }

    return {
      qSelectgeneral,
      baseConfig,
      starttime,
      endtime,
      room,
      credentials,
      filterFn,
      doorName,
      credentialStore,
      qSelects,
      //configDescription,
      commonAttributeFilter
    }
  }
}
</script>