<template>
  <q-dialog ref="dialog" @hide="onDialogHide">
    <q-card style="min-width: 45em">
      <q-card-section>
        <div class="text-h6">Tür zuweisen</div>
      </q-card-section>
      <q-card-section class="row justify-around no-wrap">
        <q-input style="width: 18em" filled v-model="doorName" label="Türname" stack-label/>
        <q-select
            style="width: 18em"
            filled
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
      <door-config ref="configIn" :door-config="doorConfigIn"
                   :direction="JSON.stringify(this.doorConfigIn) !== JSON.stringify(this.doorConfigOut) ? Direction.IN : Direction.BOTH"
                   @changeDirection="changeDirectionOut"></door-config>
      <door-config v-show="$refs.configIn?.direction !== Direction.BOTH" ref="configOut"
                   :direction="JSON.stringify(this.doorConfigIn) !== JSON.stringify(this.doorConfigOut) ? Direction.OUT : Direction.BOTH"
                   :door-config="doorConfigOut" :is-config-out="true"></door-config>
      <q-card-actions align="right">
        <q-btn flat color="primary" label="Abbrechen" @click="onCancelClick"/>
        <q-btn flat color="primary" label="Speichern" :disable="!doorName || (!room && !door)" @click="onOKClick"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
import {ref} from "vue";
import DoorConfig from "@/main/vue/views/DoorConfig.vue";
import {Direction} from "@/main/vue/entity/doorConfiguration";

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
      config.doorConfigIn?.configParts.forEach(part => {
        console.log(part)
        part.credentials = part.credentials.map(credential => credential.credentialDefinitionId)
      })
      config.doorConfigOut?.configParts.forEach(part => part.credentials = part.credentials.map(credential => credential.credentialDefinitionId))
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
    const room = ref(null)
    const doorName = ref('')
    const roomOptions = ref(props.rooms)

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
      doorName
    }
  }
}
</script>