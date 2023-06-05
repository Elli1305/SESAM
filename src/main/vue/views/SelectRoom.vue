<template>
  <q-dialog ref="dialog" @hide="onDialogHide">
    <q-card>
      <q-card-section>
        <div class="text-h6">Tür zuweisen</div>
      </q-card-section>
      <q-card-section>
        <q-input class="q-mb-md" filled v-model="doorName" label="Türname" stack-label/>
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