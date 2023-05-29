<template>
  <q-dialog ref="dialog" @hide="onDialogHide">
    <q-card class="q-dialog-plugin">
      <q-card-section class="q-pa-md">
        <div class="text-h6">Tür zuweisen</div>
        <div class="q-mt-md">
          <q-select
              filled
              v-model="room"
              use-input
              hide-selected
              label="Raum auswählen"
              option-label="name"
              fill-input
              input-debounce="0"
              :options="rooms"
              @filter="filterFn"
              style="width: 250px; padding-bottom: 32px"
          >
            <template v-slot:no-option>
              <q-item>
                <q-item-section class="text-grey">
                  No results
                </q-item-section>
              </q-item>
            </template>
          </q-select>
        </div>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn color="primary" label="Abbrechen" @click="onCancelClick"/>
        <q-btn color="primary" label="Speichern" @click="onOKClick"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
import {useFloorStore} from "@/main/vue/stores/floor";
import {ref} from "vue";

export default {
  props: {
    rooms: {
      required: true
    }
  },

  emits: [
    'ok', 'hide'
  ],

  methods: {
    show() {
      console.log(this.$refs)
      this.$refs.dialog.show()
    },
    hide() {
      this.$refs.dialog.hide()
    },
    onDialogHide() {
      this.$emit('hide')
    },

    onOKClick() {
      this.$emit('ok', this.room)
      this.hide()
    },
    onCancelClick() {
      this.hide()
    }
  },
  setup(props) {
    const room = ref(null)
    const roomOptions = ref(props.rooms)
    const filterFn = function (val, update, abort) {
      update(() => {
        const needle = val.toLowerCase()
        roomOptions.value = props.rooms.filter(room => room.name.toLowerCase().indexOf(needle) > -1)
      })
    }

    return {room, filterFn}
  }
}
</script>