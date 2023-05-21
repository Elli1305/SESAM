<template>
  <q-dialog ref="dialog" @hide="onDialogHide">
    <q-card class="q-dialog-plugin">
      <q-card-section class="q-pa-md">
        <div class="text-h6">Gebäude bearbeiten</div>
        <div class="q-mt-md">
          <q-input outlined v-model="buildingName" label="Name"/>
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
import {useBuildingStore} from "@/main/vue/stores/buildings";
import {ref} from "vue";

export default {
  props: {
    building: {
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

      this.buildingStore.save({
        name: this.buildingName,
        id: this.$props.building.id,
        floors: this.$props.building.floors,
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
    const buildingStore = useBuildingStore()
    const buildingName = ref(props.building.name)
    return {buildingName, buildingStore}
  }
}
</script>