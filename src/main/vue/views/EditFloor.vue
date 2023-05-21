<template>
  <q-dialog ref="dialog" @hide="onDialogHide">
    <q-card class="q-dialog-plugin">
      <q-card-section class="q-pa-md">
        <div class="text-h6">Ebene bearbeiten</div>
        <div class="q-mt-md">
          <q-input outlined v-model.number="floorLevel" type="number" label="Etagen Nummer"/>
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
    floor: {
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

      this.floorStore.save({
        floorLevel: this.floorLevel,
        id: this.$props.floor.id,
        floorPlanPath: this.$props.floor.floorPlanPath,
        rooms: this.$props.floor.rooms
      }).then((location) => {
        this.$emit('ok', location)
        this.hide()
      })

    },
    onCancelClick() {
      this.hide()
    }
  },
  setup(props) {
    const floorStore = useFloorStore()
    const floorLevel = ref(props.floor.floorLevel)
    return {floorLevel, floorStore}
  }
}
</script>