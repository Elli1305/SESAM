<template>
  <q-dialog ref="dialog" @hide="onDialogHide">
    <q-card class="q-dialog-plugin">
      <q-card-section class="q-pa-md">
        <div v-if="floor.id" class="text-h6">{{t('floorPlan.editFloor')}}</div>
        <div v-if="!floor.id" class="text-h6">{{t('floorPlan.addFloor')}}</div>
        <div class="q-mt-md">
          <q-input outlined v-model.number="floorLevel" type="number" :label="t('floorPlan.floorLevel')"/>
        </div>
        <div class="q-mt-md">
          <q-file outlined :label="t('floorPlan.floorPlanUpload')" v-model="image" accept=".png,.jpg,.jpeg,.gif,.svg,.tiff"
                  bg-color="white">
            <template v-slot:prepend>
              <q-icon name="attach_file"/>
            </template>
          </q-file>
        </div>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn flat color="primary" :label="t('common.cancel')" @click="onCancelClick"/>
        <q-btn flat color="primary" :label="t('common.save')" @click="onOKClick" :disable="validate()"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
import {useFloorStore} from "@/main/vue/stores/floor";
import {ref} from "vue";
import {useI18n} from "vue-i18n";

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
      }, this.image).then((floor) => {
        this.$emit('ok', floor)
        this.hide()
      })
    },
    onCancelClick() {
      this.hide()
    },
    validate() {
      return !((this.floorLevel || (this.floorLevel === 0)) && (this.$props.floor.id || this.image))
    }
  },
  setup(props) {
    const floorStore = useFloorStore()
    const floorLevel = ref(props.floor.floorLevel)
    const image = ref()
    const { t } = useI18n()

    return {floorLevel, floorStore, image, t}
  }
}
</script>