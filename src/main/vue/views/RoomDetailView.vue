<template>
  <div>
    <div class="row">
      <q-btn flat icon="arrow_back" size="sm" @click="$emit('backClicked')"></q-btn>
      <p class="text-h6" style="margin-left: 1em; text-align: center">{{ room?.name }}</p>
    </div>
    <q-scroll-area style="height: 30em; min-width: 21em; overflow: hidden; max-width: 22em; "
                   :horizontal-thumb-style="{ right: '4px', borderRadius: '5px', background: 'red', width: '10px', opacity: 0,  }"
    >
      <p class="text-subtitle1">Aktive Tür-Konfigurationen:</p>
      <q-list flat bordered v-for="door in activeConfigs">
        <q-expansion-item expanded :label="door?.doorName" :caption="door.baseConfig? 'Basiskonfiguration' : (door.startTime && door.endTime) ? door.startTime + '-' + door.endTime : ''">
          <q-card>
            <q-separator/>
            <q-card-section v-if="JSON.stringify(door.doorConfigIn) === JSON.stringify(door.doorConfigOut)">
              <div v-for="(configpart, index) in door.doorConfigIn?.configParts"
                   style='padding: 0.2em; border-radius: 1em'>
                <div style='padding: 0.1em 0.5em; border-radius: 1em;'>
                  <div v-for="(credential, i) in configpart?.credentials">
                    <q-chip color="primary" text-color="white"> {{ credential.name }}</q-chip>
                    <b v-if="i !== configpart.credentials.length - 1">ODER</b>
                  </div>
                  <div style='margin-top: 0.5em; padding: 0.1em 0.5em; border-radius: 1em;'>
                    <b>mit</b>
                    <p style="line-height: 1; margin: 0.1em"
                       v-for="(attributeFilter, j) in configpart?.attributeFilter">
                      {{
                        attributeFilter.attribute.label + " " + attributeFilter.predicateType + " " + attributeFilter.value
                      }}
                      <b v-if="j !== configpart.attributeFilter.length - 1">ODER</b>
                    </p>
                  </div>
                </div>
                <div v-if="index !== door.doorConfigIn?.configParts.length - 1">
                  <b>UND</b>
                </div>
              </div>
            </q-card-section>
            <q-card-section v-if="JSON.stringify(door.doorConfigIn) !== JSON.stringify(door.doorConfigOut)">
              <p class="text-subtitle2">Rein</p>
              <div v-for="(configpart, index) in door.doorConfigIn?.configParts"
                   style='padding: 0.2em; border-radius: 1em'>
                <div style='padding: 0.1em 0.5em; border-radius: 1em;'>
                  <div v-for="(credential, i) in configpart?.credentials">
                    <q-chip dense color="primary" text-color="white"> {{ credential.name }}</q-chip>
                    <b v-if="i !== configpart.credentials.length - 1">ODER</b>
                  </div>
                  <div style='margin-top: 0.5em; padding: 0.1em 0.5em; border-radius: 1em;'>
                    <b>mit</b>
                    <p style="line-height: 1; margin: 0.1em"
                       v-for="(attributeFilter, j) in configpart?.attributeFilter">
                      {{
                        attributeFilter.attribute.label + " " + attributeFilter.predicateType + " " + attributeFilter.value
                      }}
                      <b v-if="j !== configpart.attributeFilter.length - 1">ODER</b>
                    </p>
                  </div>
                </div>
                <div v-if="index !== door.doorConfigIn?.configParts.length - 1">
                  <b>UND</b>
                </div>
              </div>
              <p class="text-subtitle2">Raus</p>
              <div v-for="(configpart, index) in door.doorConfigOut?.configParts"
                   style='padding: 0.2em; border-radius: 1em'>
                <div style='padding: 0.1em 0.5em; border-radius: 1em;'>
                  <div v-for="(credential, i) in configpart?.credentials">
                    <q-chip dense color="primary" text-color="white"> {{ credential.name }}</q-chip>
                    <b v-if="i !== configpart.credentials.length - 1">ODER</b>
                  </div>
                  <div style='margin-top: 0.5em; padding: 0.1em 0.5em; border-radius: 1em;'>
                    <b>mit</b>
                    <p style="line-height: 1; margin: 0.1em"
                       v-for="(attributeFilter, j) in configpart?.attributeFilter">
                      {{
                        attributeFilter.attribute.label + " " + attributeFilter.predicateType + " " + attributeFilter.value
                      }}
                      <b v-if="j !== configpart.attributeFilter.length - 1">ODER</b>
                    </p>
                  </div>
                </div>
                <div v-if="index !== door.doorConfigOut?.configParts.length - 1">
                  <b>UND</b>
                </div>
              </div>
            </q-card-section>
          </q-card>

        </q-expansion-item>

      </q-list>
      <p class="text-subtitle1" v-if="inactiveConfigs.length > 0">Inaktive Tür-Konfigurationen:</p>
      <q-list flat bordered v-for="door in inactiveConfigs">
        <q-expansion-item expanded :label="door.doorName" :caption="door.baseConfig? 'Basiskonfiguration' : door.startTime + '-' + door.endTime">
          <q-card>
            <q-separator/>
            <q-card-section v-if="JSON.stringify(door.doorConfigIn) === JSON.stringify(door.doorConfigOut)">
              <div v-for="(configpart, index) in door.doorConfigIn?.configParts"
                   style='padding: 0.2em; border-radius: 1em'>
                <div style='padding: 0.1em 0.5em; border-radius: 1em;'>
                  <div v-for="(credential, i) in configpart?.credentials">
                    <q-chip color="primary" text-color="white"> {{ credential.name }}</q-chip>
                    <b v-if="i !== configpart.credentials.length - 1">ODER</b>
                  </div>
                  <div style='margin-top: 0.5em; padding: 0.1em 0.5em; border-radius: 1em;'>
                    <b>mit</b>
                    <p style="line-height: 1; margin: 0.1em"
                       v-for="(attributeFilter, j) in configpart?.attributeFilter">
                      {{
                        attributeFilter.attribute.label + " " + attributeFilter.predicateType + " " + attributeFilter.value
                      }}
                      <b v-if="j !== configpart.attributeFilter.length - 1">ODER</b>
                    </p>
                  </div>
                </div>
                <div v-if="index !== door.doorConfigIn?.configParts.length - 1">
                  <b>UND</b>
                </div>
              </div>
            </q-card-section>
            <q-card-section v-if="JSON.stringify(door.doorConfigIn) !== JSON.stringify(door.doorConfigOut)">
              <p class="text-subtitle2">Rein</p>
              <div v-for="(configpart, index) in door.doorConfigIn?.configParts"
                   style='padding: 0.2em; border-radius: 1em'>
                <div style='padding: 0.1em 0.5em; border-radius: 1em;'>
                  <div v-for="(credential, i) in configpart?.credentials">
                    <q-chip dense color="primary" text-color="white"> {{ credential.name }}</q-chip>
                    <b v-if="i !== configpart.credentials.length - 1">ODER</b>
                  </div>
                  <div style='margin-top: 0.5em; padding: 0.1em 0.5em; border-radius: 1em;'>
                    <b>mit</b>
                    <p style="line-height: 1; margin: 0.1em"
                       v-for="(attributeFilter, j) in configpart?.attributeFilter">
                      {{
                        attributeFilter.attribute.label + " " + attributeFilter.predicateType + " " + attributeFilter.value
                      }}
                      <b v-if="j !== configpart.attributeFilter.length - 1">ODER</b>
                    </p>
                  </div>
                </div>
                <div v-if="index !== door.doorConfigIn?.configParts.length - 1">
                  <b>UND</b>
                </div>
              </div>
              <p class="text-subtitle2">Raus</p>
              <div v-for="(configpart, index) in door.doorConfigOut?.configParts"
                   style='padding: 0.2em; border-radius: 1em'>
                <div style='padding: 0.1em 0.5em; border-radius: 1em;'>
                  <div v-for="(credential, i) in configpart?.credentials">
                    <q-chip dense color="primary" text-color="white"> {{ credential.name }}</q-chip>
                    <b v-if="i !== configpart.credentials.length - 1">ODER</b>
                  </div>
                  <div style='margin-top: 0.5em; padding: 0.1em 0.5em; border-radius: 1em;'>
                    <b>mit</b>
                    <p style="line-height: 1; margin: 0.1em"
                       v-for="(attributeFilter, j) in configpart?.attributeFilter">
                      {{
                        attributeFilter.attribute.label + " " + attributeFilter.predicateType + " " + attributeFilter.value
                      }}
                      <b v-if="j !== configpart.attributeFilter.length - 1">ODER</b>
                    </p>
                  </div>
                </div>
                <div v-if="index !== door.doorConfigOut?.configParts.length - 1">
                  <b>UND</b>
                </div>
              </div>
            </q-card-section>
          </q-card>

        </q-expansion-item>

      </q-list>
    </q-scroll-area>
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
  methods: {},
  setup(props) {
    const loading = ref(true)
    const config = ref();
    const activeConfigs = ref([]);

    // TODO get inactive configs
    const inactiveConfigs = ref([]);

    const getActiveConfig = (door) => {
      let activeConfig;
      if (door.doorConfigCmds.length > 1) {
        let baseConf;
        for (const doorConfig of door.doorConfigCmds) {
          if (doorConfig.baseConfig) {
            baseConf = doorConfig;
            break;
          }
          // TODO check time
        }

        return activeConfig ? activeConfig : baseConf;
      } else {
        activeConfig = door.doorConfigCmds[0]
      }
      return activeConfig;
    }

    api.room.getRoomDetails(props.room.id)
        .then(room => {
          config.value = room.data
          console.log(config.value)
          loading.value = false
          activeConfigs.value = room.data.doors.map(door => ({
            ...getActiveConfig(door), doorName: door.name
          }))
        })
        .catch(() => loading.value = false)
    return {config, activeConfigs, inactiveConfigs}
  }
}
</script>

<style scoped>

</style>