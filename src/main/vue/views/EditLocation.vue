<template>
  <q-dialog ref="dialog" @hide="onDialogHide">
    <q-card class="q-dialog-plugin">
      <q-card-section class="q-pa-md">
        <div class="text-h6">Standort bearbeiten</div>
        <div class="q-mt-md">
          <q-input outlined v-model="locationName" label="Name"/>
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
import {useLocationStore} from "@/main/vue/stores/locations";

export default {
  props: {
    location: {
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

      this.locationStore.save({
        name: this.locationName,
        id: this.$props.location.id,
        buildings: this.$props.location.buildings,
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
    const locationStore = useLocationStore()
    return {locationName: props.location.name, locationStore}
  }
}
</script>