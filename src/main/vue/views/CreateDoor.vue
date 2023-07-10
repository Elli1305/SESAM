<template>
  <q-dialog ref="dialog" @hide="onDialogHide">
    <q-card style="min-width: 60em">
      <q-card-section>
        <div class="text-h6">{{ t('floorPlan.addDoor') }}</div>
      </q-card-section>
      <q-card-section class="row q-px-lg justify-between no-wrap">
        <q-input class="full-width" filled v-model="doorName" :label="t('floorPlan.doorName')" stack-label/>
        <q-select
            class="q-ml-md"
            style="min-width: 20em"
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
        <q-select
             class="q-ml-md"
             style="min-width: 20em"
             filled
             use-input
             hide-selected
             fill-input
             input-debounce="0"
             @filter="filterFn"
             :label="t('floorPlan.chooseConfig')"
             option-label="name"
             v-model="selectedConfig"
             :options="configOptions"
             clearable
          >
            <template v-slot:no-option>
                <q-item>
                    <q-item-section class="text-grey">
                        No results
                    </q-item-section>
                </q-item>
            </template>
        </q-select>
      </q-card-section>
      <door-config ref="configIn" :door-config="doorConfigIn"
                   :direction="JSON.stringify(this.doorConfigIn) !== JSON.stringify(this.doorConfigOut) ? Direction.IN : Direction.BOTH"
                   @changeDirection="changeDirectionOut"></door-config>
      <door-config v-show="$refs.configIn?.direction !== Direction.BOTH" ref="configOut"
                   :direction="JSON.stringify(this.doorConfigIn) !== JSON.stringify(this.doorConfigOut) ? Direction.OUT : Direction.BOTH"
                   :door-config="doorConfigOut" :is-config-out="true"></door-config>
      <q-card-actions align="right">
        <q-btn flat color="primary" :label="t('common.cancel')" @click="onCancelClick"/>
        <q-btn flat color="primary" :label="t('common.save')" :disable="!doorName || (!room && !door)" @click="onOKClick"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
import {ref, watch} from "vue";
import DoorConfig from "@/main/vue/views/DoorConfig.vue";
import {Direction} from "@/main/vue/entity/doorConfiguration";
import {useI18n} from "vue-i18n";
import {useConfigStore} from "@/main/vue/stores/config";
import {storeToRefs} from "pinia";

export default {
  computed: {
    Direction() {
      return Direction
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
    changeDirectionOut(direction) {
      if (direction === Direction.IN) {
        this.$refs.configOut.direction = Direction.OUT
      } else if (direction === Direction.OUT) {
        this.$refs.configOut.direction = Direction.IN
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

    onOKClick() {
      let config = {}

      console.log('qselects', this.$refs.configIn.qSelects)
      if (this.$refs.configIn.direction === Direction.BOTH) {
        config.doorConfigIn = JSON.parse(JSON.stringify(this.$refs.configIn.qSelects))
        config.doorConfigOut = JSON.parse(JSON.stringify(this.$refs.configIn.qSelects))
        config.doorConfigIn.description = this.$refs.configIn.configDescription
        config.doorConfigOut.description = this.$refs.configIn.configDescription
      } else if (this.$refs.configIn.direction === Direction.IN) {
        config.doorConfigIn = JSON.parse(JSON.stringify(this.$refs.configIn.qSelects))
        config.doorConfigOut = JSON.parse(JSON.stringify(this.$refs.configOut.qSelects))
        config.doorConfigIn.description = this.$refs.configIn.configDescription
        config.doorConfigOut.description = this.$refs.configOut.configDescription
      } else if (this.$refs.configIn.direction === Direction.OUT) {
        config.doorConfigIn = JSON.parse(JSON.stringify(this.$refs.configOut.qSelects))
        config.doorConfigOut = JSON.parse(JSON.stringify(this.$refs.configIn.qSelects))
        config.doorConfigIn.description = this.$refs.configOut.configDescription
        config.doorConfigOut.description = this.$refs.configIn.configDescription
      }
      this.$emit('ok', {
        room: this.room,
        doorName: this.doorName,
        configuration: config
      })
      this.hide()
    },
    onCancelClick() {
      this.hide()
    },
  },
  setup(props) {
      const {t} = useI18n()
      const room = ref(null)
      const doorName = ref('')
      const roomOptions = ref(props.rooms)
      const configStore = useConfigStore()
      configStore.getAllConfigs()
      const {allPreConfigs} = storeToRefs(configStore)
      const configOptions = ref()
      configOptions.value = configStore.allPreConfigs
      const selectedConfig = ref()

      const configIn = ref()
      const configOut = ref()

      watch(allPreConfigs, () => {
          configOptions.value = configStore.allPreConfigs
      })

      watch(selectedConfig, async () => {
          if (selectedConfig.value == null) {
              configIn.value.configDescription = null
              configIn.value.qSelects.configParts = []
              configOut.value.configDescription = null
              configOut.value.qSelects.configParts = []
              configIn.value.direction = Direction.BOTH
          } else {
              await configStore.getConfig(selectedConfig.value.id)
              let chosenConfig = configStore.currentConfig
              configIn.value.configDescription = chosenConfig?.doorConfigIn.description
              configIn.value.qSelects.configParts = chosenConfig?.doorConfigIn.configParts
              configOut.value.configDescription = chosenConfig?.doorConfigOut.description
              configOut.value.qSelects.configParts = chosenConfig?.doorConfigOut.configParts
              if (JSON.stringify(chosenConfig?.doorConfigIn) !== JSON.stringify(chosenConfig?.doorConfigOut)) {
                  configIn.value.direction = Direction.IN
                  configOut.value.direction = Direction.OUT
              }
          }
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

    return {
        room,
        filterFn,
        doorName,
        configOptions,
        selectedConfig,
        configOut,
        configIn,
        t
    }
  }
}
</script>