<template>
  <div>
    <div class="row items-baseline" style="height: 6em">
      <q-btn flat icon="arrow_back" @click="$emit('backClicked')"></q-btn>
      <p class="text-h6" style="line-height: 1">{{ room?.name }}</p>
    </div>
    <q-list flat bordered v-for="door in config?.doors">
      <q-expansion-item expanded :label="door.name">
        <q-card>
          <q-separator/>
          <q-card-section>

            <div v-for="(configpart, index) in door.proofConfigIn[0]?.configParts"
                 style='padding: 0.2em; border-radius: 1em'>
              <div style='padding: 0.1em 0.5em; border-radius: 1em;'>
                <div v-for="(credential, i) in configpart?.credentials">
                  <q-chip dense color="primary" text-color="white"> {{credential.name}} </q-chip> <b v-if="i !== configpart.credentials.length - 1">ODER</b>
                </div>
                <div style='margin-top: 0.5em; padding: 0.1em 0.5em; border-radius: 1em;'>
                  <b>mit</b>
                  <p style="line-height: 1; margin: 0.1em" v-for="(attributeFilter, j) in configpart?.attributeFilter">
                    {{
                      attributeFilter.attribute.label + " " + attributeFilter.predicateType + " " + attributeFilter.value
                    }} <b v-if="j !== configpart.attributeFilter.length - 1">ODER</b>
                  </p>
                </div>
              </div>
              <div v-if="index !== door.proofConfigIn[0]?.configParts.length - 1">
                <b>UND</b>
              </div>
            </div>
          </q-card-section>
        </q-card>

      </q-expansion-item>

    </q-list>
  </div>
</template>

<script>

import {ref} from "vue";
import api from "@/main/vue/api";
import DoorConfig from "@/main/vue/views/DoorConfig.vue";
import {Direction} from "@/main/vue/entity/doorConfiguration";

export default {
  name: "RoomDetailView",
  computed: {
    Direction() {
      return Direction
    }
  },
  components: {DoorConfig},
  props: {
    room: {
      required: true
    },
  },
  emits: [
    'backClicked'
  ],
  setup(props) {
    const loading = ref(true)
    const config = ref();
    api.room.getRoomDetails(props.room.id)
        .then(room => {
          config.value = room.data
          console.log(config.value)
          loading.value = false
        })
        .catch(() => loading.value = false)
    return {config}
  }
}
</script>

<style scoped>

</style>