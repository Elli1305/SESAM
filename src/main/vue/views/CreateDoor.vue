<template>
  <q-dialog ref="dialog" @hide="onDialogHide" persistent>
    <q-card style="width: 70em; max-width: 70em; background-color: var(--bg-color); color: var(--text-color)">
      <q-card-section class="row justify-between">
          <div class="text-h6">{{ t('floorPlan.addDoor') }}</div>
        <div>
          <q-btn v-if="door?.id" size="0.9em" flat icon="open_in_new" label="Tür anzeigen (rein)" :href="`https://sesam.gp-se.de:3060/${door.name}_${door.id}_in`" target="_blank"/>
          <q-btn v-if="door?.id" size="0.9em" flat icon="open_in_new" label="Tür anzeigen (raus)" :href="`https://sesam.gp-se.de:3060/${door.name}_${door.id}_out`" target="_blank"/>
        <q-btn flat rounded size="0.9em">
          <q-icon name="content_copy" size="1em" left/>
          {{ t('floorPlan.chooseConfig') }}
          <q-menu style="background-color: var(--bg-color); color: var(--text-color)">
            <q-list>
              <q-item clickable v-close-popup v-for="config in configOptions" @click="selectedConfig = config">
                <q-item-section>
                  <q-item-label>{{config.name}}</q-item-label>
                </q-item-section>
              </q-item>
            </q-list>
          </q-menu>
        </q-btn>
        </div>
      </q-card-section>
      <q-card-section class="row q-px-lg justify-between no-wrap">
        <div class="full-width">
          <q-input filled v-model="doorName" :label="t('floorPlan.doorName')" style="margin-bottom: 1em" stack-label/>
          <q-input filled v-model="doorId" :label="t('floorPlan.doorId')" v-if="doorId" readonly stack-label/>
        </div>
        <q-select
            class="q-ml-md"
            style="width: 40em"
            filled
            v-model="room"
            use-input
            hide-selected
            :label="t('floorPlan.pickRoom')"
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
      <q-scroll-area style="width: 100%; height: 40em">
        <q-card-section v-for="(selectConf,k) in qSelectgeneral.qSelectsSet">
          <q-card style="background-color: var(--bg-color); color: var(--text-color)" bordered flat>
            <q-toolbar class="bg-primary text-accent">

              <q-toolbar-title>{{t('floorPlan.config')}}</q-toolbar-title>
              <q-checkbox class="q-mr-lg" size="2em" dense v-model="qSelectgeneral.qSelectsSet[k].baseConfig"
                          :label="t('floorPlan.base')" @click="check(k)"/>
              <q-icon class="q-mr-xs" color="accent" size="1.25em" name="info_outlined">
                <q-tooltip style="background-color: var(--bg-color); color: var(--text-color); font-size: 1em"
                           max-width="15em" anchor="center right" self="center left">
                  {{t('floorPlan.baseConfig')}}
                </q-tooltip>
              </q-icon>
              <q-td v-if=!(checkLength())>
                <q-btn flat round icon="delete" size="0.75em" @click="removeConfig(k)"/>
              </q-td>
            </q-toolbar>


            <div class="row justify-center q-mt-lg no-wrap"
                 v-if="!qSelectgeneral.qSelectsSet[k].baseConfig">
              <q-input outlined rounded v-model="qSelectgeneral.qSelectsSet[k].startTime" mask="time" :rules="['time']">
                <template v-slot:append>
                  <q-icon name="access_time" class="cursor-pointer">
                    <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                      <q-time text-color="accent" style="background-color: var(--bg-color); color: var(--text-color)"
                              v-model="qSelectgeneral.qSelectsSet[k].startTime">
                        <q-icon
                            class="cursor-pointer"
                            style="position: absolute; margin-top: -17.5em; margin-left: 11.7em"
                            v-close-popup
                            name="clear"
                            color="accent"
                            size="1.5em"/>
                      </q-time>
                    </q-popup-proxy>
                  </q-icon>
                </template>
              </q-input>
              <div class="column justify-center q-mx-lg" style="height: 4em">
                <span style="font-size: 2em; color: grey">&#8210</span>
              </div>
              <q-input outlined rounded v-model="qSelectgeneral.qSelectsSet[k].endTime" mask="time" :rules="['time']"
                       :disabled="qSelectgeneral.qSelectsSet[k].baseConfig">
                <template v-slot:append>
                  <q-icon name="access_time" class="cursor-pointer">
                    <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                      <q-time style="background-color: var(--bg-color); color: var(--text-color)"
                              v-model="qSelectgeneral.qSelectsSet[k].endTime">
                        <q-icon
                            class="cursor-pointer"
                            style="position: absolute; margin-top: -17.5em; margin-left: 11.7em"
                            v-close-popup
                            name="clear"
                            color="accent"
                            size="1.5em"/>
                      </q-time>
                    </q-popup-proxy>
                  </q-icon>
                </template>
              </q-input>
            </div>
            <door-config :door-config="qSelectgeneral.qSelectsSet[k].doorConfigIn"
                         :direction="JSON.stringify(qSelectgeneral.qSelectsSet[k].doorConfigIn) !==
                        JSON.stringify(qSelectgeneral.qSelectsSet[k].doorConfigOut) ? Direction.IN : Direction.BOTH"
                         @changeDirection="changeDirectionOut($event, k)" ref="doorIn">
            </door-config>
            <door-config v-show="JSON.stringify(qSelectgeneral.qSelectsSet[k].doorConfigIn.direction) !==
                       JSON.stringify(qSelectgeneral.qSelectsSet[k].doorConfigOut.direction)"
                         :direction="Direction.BOTH"
                         :door-config="qSelectgeneral.qSelectsSet[k].doorConfigOut" :is-config-out="true"
                         ref="doorOut">
            </door-config>

            <q-btn class="q-ml-sm q-mb-sm" flat dense rounded color="primary" icon="add" @click="addConfiguration">
              {{t('floorPlan.addConfig')}}
            </q-btn>


          </q-card>
        </q-card-section>
      </q-scroll-area>
      <q-card-actions align="right">
        <q-btn flat color="primary" :label="t('common.cancel')" @click="onCancelClick"/>
        <q-btn flat color="primary" :label="t('common.save')"
               :disable="!doorName || (!room && !door) || !checkBaseConf()" @click="onOKClick"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
import {reactive, ref, watch} from "vue";
import DoorConfig from "@/main/vue/views/DoorConfig.vue";
import {Direction} from "@/main/vue/entity/doorConfiguration";
import {useI18n} from "vue-i18n";
import {useConfigStore} from "@/main/vue/stores/config";
import {storeToRefs} from "pinia";
import {useQuasar} from "quasar";

const $q = useQuasar()


export default {
  computed: {
    Direction() {
      return Direction
    },

    multipleBaseConfigs() {
      return this.qSelectgeneral.qSelectsSet.filter(conf => conf.baseConfig).length > 1;

    }
  },
  components: {DoorConfig},
  props: {
    rooms: Array,
    door: {
      required: false
    },
    doorConfigIn: {
      required: false
    },
    doorConfigOut: {
      required: false
    }
  },

  emits: [
    'ok', 'hide'
  ],
  methods: {
    changeDirectionOut(direction, k) {

      if (direction === Direction.IN) {
        this.$refs.doorOut[k].direction = Direction.OUT
        this.qSelectgeneral.qSelectsSet[k].doorConfigOut.direction = Direction.IN
        this.qSelectgeneral.qSelectsSet[k].doorConfigIn.direction = Direction.OUT

      } else if (direction === Direction.OUT) {
        this.$refs.doorOut[k].direction = Direction.IN
        this.qSelectgeneral.qSelectsSet[k].doorConfigOut.direction = Direction.OUT
        this.qSelectgeneral.qSelectsSet[k].doorConfigIn.direction = Direction.IN

      } else if (direction === Direction.BOTH) {
        this.$refs.doorOut[k].direction = Direction.BOTH
        this.qSelectgeneral.qSelectsSet[k].doorConfigOut.direction = Direction.BOTH
        this.qSelectgeneral.qSelectsSet[k].doorConfigIn.direction = Direction.BOTH
      }

    },
    show() {
      this.$refs.dialog.show()
    },
    hide() {
      this.$refs.dialog.hide()
    },
    onDialogHide() {
      this.$emit('hide')
    },

    checkBaseConf() {
      let x = 0;
      let y = false;
      this.qSelectgeneral.qSelectsSet.forEach((element, index) => {
        if (this.qSelectgeneral.qSelectsSet[index].baseConfig === true) {
          x = x + 1;
        }
      })
      if (x === 1) {
        y = true
      }
      return y
    },

    addConfiguration() {
      this.qSelectgeneral.qSelectsSet.push( {
        doorConfigIn: {configParts: [{credentials: [], attributeFilter: [{ attribute: null,
              predicateType: null, value: null, currentDate: false}]}], description: "", direction: Direction.BOTH},
        doorConfigOut: {configParts: [{credentials: [], attributeFilter: [{attribute: null,
              predicateType: null, value: null, currentDate: false}]}], description: "", direction: Direction.BOTH},
        baseConfig: false,
        startTime: null,
        endTime: null
      })
    },
    removeConfig(i) {
      this.qSelectgeneral.qSelectsSet.splice(i, 1)
    },
    checkLength() {
      return (this.qSelectgeneral.qSelectsSet.length === 1);
    },


    onOKClick() {
      const allConfig = []
      this.qSelectgeneral.qSelectsSet.forEach((element, index) => {
        let config = {}

        config.baseConfig = this.qSelectgeneral.qSelectsSet[index].baseConfig
        config.startTime = this.qSelectgeneral.qSelectsSet[index].startTime
        config.endTime = this.qSelectgeneral.qSelectsSet[index].endTime
        if (this.$refs.doorIn[index].direction === Direction.BOTH) {
          config.doorConfigIn = JSON.parse(JSON.stringify(this.qSelectgeneral.qSelectsSet[index].doorConfigIn))
          config.doorConfigOut = JSON.parse(JSON.stringify(this.qSelectgeneral.qSelectsSet[index].doorConfigIn))
          config.doorConfigIn.description = this.qSelectgeneral.qSelectsSet[index].doorConfigIn.description
          config.doorConfigOut.description = this.qSelectgeneral.qSelectsSet[index].doorConfigIn.description
        } else if (this.$refs.doorIn[index].direction === Direction.IN) {
          config.doorConfigIn = JSON.parse(JSON.stringify(this.qSelectgeneral.qSelectsSet[index].doorConfigIn))
          config.doorConfigOut = JSON.parse(JSON.stringify(this.qSelectgeneral.qSelectsSet[index].doorConfigOut))
          config.doorConfigIn.description = this.qSelectgeneral.qSelectsSet[index].doorConfigIn.description
          config.doorConfigOut.description = this.qSelectgeneral.qSelectsSet[index].doorConfigOut.description
        } else if (this.$refs.doorIn[index].direction === Direction.OUT) {
          config.doorConfigIn = JSON.parse(JSON.stringify(this.qSelectgeneral.qSelectsSet[index].doorConfigOut))
          config.doorConfigOut = JSON.parse(JSON.stringify(this.qSelectgeneral.qSelectsSet[index].doorConfigIn))
          config.doorConfigIn.description = this.qSelectgeneral.qSelectsSet[index].doorConfigOut.description
          config.doorConfigOut.description = this.qSelectgeneral.qSelectsSet[index].doorConfigIn.description
        }
        console.log(config)
        allConfig.push(config)
      })
      this.$emit('ok', {
        room: this.room,
        doorName: this.doorName,
        configuration: allConfig
      })
      this.hide()
    },
    onCancelClick() {
      this.hide()
    },
  },
  setup(props) {
    const room = ref(null)
    const doorName = ref('')
    const doorId = ref('')
    const roomOptions = ref(props.rooms)
    const {t} = useI18n()
    const $q = useQuasar()
    const configStore = useConfigStore()
    configStore.getAllConfigs()
    const {allPreConfigs} = storeToRefs(configStore)
    const configOptions = ref()
    configOptions.value = configStore.allPreConfigs
    const selectedConfig = ref()

    const configIn = ref([])
    const configOut = ref([])
    const base = ref()

    watch(allPreConfigs, () => {
      configOptions.value = configStore.allPreConfigs
    })
    watch(selectedConfig, async () => {
      console.log(selectedConfig)
      if (selectedConfig.value == null) {
        qSelectgeneral.qSelectsSet.splice(0)
        qSelectgeneral.qSelectsSet.push({
          doorConfigIn: {configParts: [{credentials: [], attributeFilter: [{ attribute: null,
                predicateType: null, value: null, currentDate: false}]}], description: "", direction: Direction.BOTH},
          doorConfigOut: {configParts: [{credentials: [], attributeFilter: [{attribute: null,
                predicateType: null, value: null, currentDate: false}]}], description: "", direction: Direction.BOTH},
          baseConfig: false,
          startTime: null,
          endTime: null
        })
      } else {
        await configStore.getConfig(selectedConfig.value.id)
        let chosenConfig = configStore.currentConfig
        let tempConfig = []
        chosenConfig?.doorConfig.forEach((element) => {
          console.log("Wert",JSON.stringify(element.doorConfigIn)
              !== JSON.stringify(element.doorConfigOut))
          let object = {
            doorConfigIn: {
                direction: JSON.stringify(element.doorConfigIn)
                !== JSON.stringify(element.doorConfigOut) ? Direction.IN : Direction.BOTH,
              description: element.doorConfigIn.description,
              configParts: element.doorConfigIn.configParts
            },
            doorConfigOut: {
                direction: JSON.stringify(element.doorConfigIn)
                !== JSON.stringify(element.doorConfigOut) ? Direction.OUT : Direction.BOTH,
              description: element.doorConfigOut.description,
              configParts: element.doorConfigOut.configParts
            },
            startTime: element.startTime || '',
            endTime: element.endTime || '',
            baseConfig: element.baseConfig || false
          }
          tempConfig.push(object)
        })
        qSelectgeneral.qSelectsSet = tempConfig
        console.log("temp: ", tempConfig)
        console.log("qselect: ", qSelectgeneral.qSelectsSet)
      }
    })


    const disableSave = ref(false);

    const qSelectgeneral = reactive({
      qSelectsSet: [{
        doorConfigIn: {configParts: [{credentials: [], attributeFilter: [{ attribute: null,
              predicateType: null, value: null, currentDate: false}]}], description: "", direction: Direction.BOTH},
        doorConfigOut: {configParts: [{credentials: [], attributeFilter: [{attribute: null,
              predicateType: null, value: null, currentDate: false}]}], description: "", direction: Direction.BOTH},
        baseConfig: false,
        startTime: null,
        endTime: null
      }]
    })


    if (props.door) {
      doorName.value = props.door.name
      doorId.value = props.door.id
      if(props.door.doorConfigCmds.length > 0) {
        qSelectgeneral.qSelectsSet = props.door.doorConfigCmds
          props.door.doorConfigCmds.forEach((element, index) => {
              if(JSON.stringify(element.doorConfigIn) !== JSON.stringify(element.doorConfigOut)) {
                  qSelectgeneral.qSelectsSet[index].doorConfigIn.direction = Direction.IN
                  qSelectgeneral.qSelectsSet[index].doorConfigOut.direction = Direction.OUT
              } else {
                  qSelectgeneral.qSelectsSet[index].doorConfigIn.direction = Direction.BOTH
                  qSelectgeneral.qSelectsSet[index].doorConfigOut.direction = Direction.BOTH
              }
          })
      }
    }

    const check = (k) => {

      const baseConfCount = qSelectgeneral.qSelectsSet.filter(
          (config) => config.baseConfig
      ).length;
      if (baseConfCount > 1) {
        // Display warning or prevent saving
        $q.notify({
          type: 'negative',
          message: t('floorPlan.baseConfig'),
          caption: "Error",
          position: "top",
          color: 'negative',
          textColor: 'postitive',
          timeout: 3000,
          classes: "loginNotify"
        })
        qSelectgeneral.qSelectsSet.forEach((element, index) => {
          if (!(index === k)) {
            element.baseConfig = false;
          }
        })
        return false; // Prevent saving
      }
      return true; // Allow saving
    };

    const filterFn = function (val, update) {
      update(() => {
        const needle = val.toLowerCase()
        roomOptions.value = props.rooms.filter(room => room.name.toLowerCase().indexOf(needle) > -1)
      })
    }

    const filterConfigOptions = function (val, update) {
      update(() => {
        const needle = val.toLowerCase()
        configOptions.value = allPreConfigs.value.filter(config => config.name.toLowerCase().indexOf(needle) > -1)
      })
    }

    return {
      filterConfigOptions,
      qSelectgeneral,
      room,
      filterFn,
      doorName,
      doorId,
      disableSave,
      t,
      check,
      configOptions,
      selectedConfig,
      configOut,
      configIn
    }
  },
  mounted() {
    for (const test in this.$refs.doorIn) {
      console.log(test)
    }
  },
}
</script>
