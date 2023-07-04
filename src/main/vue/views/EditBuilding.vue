<template>
  <q-dialog ref="dialog" @hide="onDialogHide">
    <q-card class="q-dialog-plugin">
      <q-card-section class="q-pa-md">
        <div v-if="building.id" class="text-h6">{{t('floorPlan.editBuilding')}}</div>
        <div v-if="!building.id" class="text-h6">{{t('floorPlan.addBuilding')}}</div>
        <div class="q-mt-md">
          <q-input outlined v-model="buildingName" label="Name"/>
        </div>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn flat color="primary" :label="t('admin.corporateDesign.confirm.save.cancel')" @click="onCancelClick"/>
        <q-btn flat color="primary" :label="t('admin.corporateDesign.confirm.save.ok')" @click="onOKClick"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
import {useBuildingStore} from "@/main/vue/stores/buildings";
import {ref} from "vue";
import {useI18n} from "vue-i18n";

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
    const { t } = useI18n()
    const buildingName = ref(props.building.name)
    return {buildingName, buildingStore, t}
  }
}
</script>