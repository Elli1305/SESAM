<template>
  <q-dialog ref="dialog" @hide="onDialogHide">
    <q-card class="q-dialog-plugin">
      <q-card-section class="q-pa-md">
        <div class="text-h6">Tür zuweisen</div>
        <div class="q-mt-md">
          <q-input filled v-model="doorName" label="Türname" stack-label
                   style="width: 250px; padding-bottom: 32px"/>
        </div>
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
        <q-btn flat color="primary" :label="t('corporateDesign.confirm.save.cancel')" @click="onCancelClick"/>
        <q-btn flat color="primary" :label="t('corporateDesign.confirm.save.ok')" @click="onOKClick"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
import {ref} from "vue";
import {useI18n} from "vue-i18n";

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
      this.$refs.dialog.show()
    },
    hide() {
      this.$refs.dialog.hide()
    },
    onDialogHide() {
      this.$emit('hide')
    },

    onOKClick() {
      this.$emit('ok', {room: this.room, doorName: this.doorName})
      this.hide()
    },
    onCancelClick() {
      this.hide()
    }
  },
  setup(props) {
    const room = ref(null)
    const doorName = ref('')
    const roomOptions = ref(props.rooms)
    const { t } = useI18n()
    const filterFn = function (val, update) {
      update(() => {
        const needle = val.toLowerCase()
        roomOptions.value = props.rooms.filter(room => room.name.toLowerCase().indexOf(needle) > -1)
      })
    }

    return {room, filterFn, doorName, t}
  }
}
</script>