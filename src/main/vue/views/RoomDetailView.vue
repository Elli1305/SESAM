<template>
  <div class="row justify-between">
    <q-btn
        size="1em" flat round
        icon="arrow_back"
        @click="$emit('backClicked')"/>
    <p class="text-h6 self-center no-margin">{{ room?.name }}</p>
    <q-btn
        size="1em" flat round
        icon="edit"
        style="color: var(--light)"
        v-show="userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted)"
        @click="openDialog(room)"/>
  </div>
  <q-scroll-area style="width: 100%; height: 33.25em">
    <p class="text-subtitle2 q-mt-md">Aktive Tür-Konfigurationen:</p>
    <q-list flat bordered v-for="door in activeConfigs">
      <q-expansion-item expanded :label="door?.doorName"
                        :caption="door.baseConfig? 'Basiskonfiguration' : (door.startTime && door.endTime) ? door.startTime + '-' + door.endTime : ''">
        <q-card style="background-color: var(--bg-color); color: var(--text-color)">
          <q-separator/>
          <q-card-section v-if="JSON.stringify(door.doorConfigIn) === JSON.stringify(door.doorConfigOut)">
            <div v-for="(configpart, index) in door.doorConfigIn?.configParts"
                 style='padding: 0.2em; border-radius: 1em'>
              <div class="q-mb-sm" style='padding: 0.1em 0.5em; border-radius: 1em;'>
                <div v-for="(credential, i) in configpart?.credentials">
                  <q-chip color="primary" text-color="accent"> {{ credential.name }}</q-chip>
                  <b v-if="i !== configpart.credentials.length - 1">ODER</b>
                </div>
                <div style='margin-top: 0.5em; padding: 0.1em 0.5em; border-radius: 1em;'>
                  <b>mit</b>
                  <p style="line-height: 1; margin: 0.1em"
                     v-for="(attributeFilter, j) in configpart?.attributeFilter">
                    {{ attributeFilter.attribute.label + " " + attributeFilter.predicateType + " " + attributeFilter.value }}
                    <b v-if="j !== configpart.attributeFilter.length - 1">ODER</b>
                  </p>
                </div>
              </div>
                <b v-if="index !== door.doorConfigIn?.configParts.length - 1">UND</b>
            </div>
          </q-card-section>
          <q-card-section v-if="JSON.stringify(door.doorConfigIn) !== JSON.stringify(door.doorConfigOut)">
            <p class="text-subtitle2">Rein</p>
            <div v-for="(configpart, index) in door.doorConfigIn?.configParts"
                 style='padding: 0.2em; border-radius: 1em'>
              <div style='padding: 0.1em 0.5em; border-radius: 1em;'>
                <div v-for="(credential, i) in configpart?.credentials">
                  <q-chip dense color="primary" text-color="accent"> {{ credential.name }}</q-chip>
                  <b v-if="i !== configpart.credentials.length - 1">ODER</b>
                </div>
                <div style='margin-top: 0.5em; padding: 0.1em 0.5em; border-radius: 1em;'>
                  <b>mit</b>
                  <p style="line-height: 1; margin: 0.1em"
                     v-for="(attributeFilter, j) in configpart?.attributeFilter">
                    {{ attributeFilter.attribute.label + " " + attributeFilter.predicateType + " " + attributeFilter.value }}
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
                  <q-chip dense color="primary" text-color="accent"> {{ credential.name }}</q-chip>
                  <b v-if="i !== configpart.credentials.length - 1">ODER</b>
                </div>
                <div style='margin-top: 0.5em; padding: 0.1em 0.5em; border-radius: 1em;'>
                  <b>mit</b>
                  <p style="line-height: 1; margin: 0.1em"
                     v-for="(attributeFilter, j) in configpart?.attributeFilter">
                    {{ attributeFilter.attribute.label + " " + attributeFilter.predicateType + " " + attributeFilter.value }}
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
    <p class="text-subtitle2 q-mt-md" v-if="inactiveConfigs.length > 0">Inaktive Tür-Konfigurationen:</p>
    <q-list flat bordered v-for="door in inactiveConfigs">
      <q-expansion-item expanded :label="door.doorName"
                        :caption="door.baseConfig? 'Basiskonfiguration' : door.startTime + '-' + door.endTime">
        <q-card style="background-color: var(--bg-color); color: var(--text-color)">
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
</template>

<script>

import {ref, watch} from "vue";
import api from "@/main/vue/api";
import DoorConfig from "@/main/vue/views/DoorConfig.vue";
import {Direction} from "@/main/vue/entity/doorConfiguration";
import {useQuasar} from "quasar";
import EditRoom from "@/main/vue/views/EditRoom.vue";
import {useUserStore} from "@/main/vue/stores/users";

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
    'backClicked', 'doorChanged'
  ],
  methods: {
    load() {
      api.room.getRoomDetails(this.$props.room.id)
          .then(room => {
            console.log("load")
            this.config = room.data
            this.loading = false
            this.activeConfigs = room.data.doors.map(door => ({
              ...this.getActiveConfig(door), doorName: door.name
            }))
            this.inactiveConfigs = room.data.doors.flatMap(d => this.getInactiveConfig(d))
          })
          .catch(() => this.loading = false)
    }, openDialog(room) {
      let dialog = this.$q.dialog({
        component: EditRoom,
        componentProps: {
          room: room
        }
      });
      dialog.onOk(() => {
        this.load()
        this.$emit('doorChanged')
      })
      dialog.onCancel(() => {
        this.load()
        this.$emit('doorChanged')
      })
    }
  },
  setup(props) {
    const $q = useQuasar();
    const loading = ref(true)
    const config = ref();
    const userStore = useUserStore()
    const activeConfigs = ref([]);

    const inactiveConfigs = ref([]);

    watch(() => props.room, () => {
      api.room.getRoomDetails(props.room.id)
          .then(room => {
            config.value = room.data
            loading.value = false
            activeConfigs.value = room.data.doors.map(door => ({
              ...getActiveConfig(door), doorName: door.name
            }))
            inactiveConfigs.value = room.data.doors.flatMap(d => getInactiveConfig(d))
          })
          .catch(() => loading.value = false)
    })
    const getActiveConfig = (door) => {
      if (door.doorConfigCmds.length > 1) {
        const currentDate = new Date();

        for (const doorConfig of door.doorConfigCmds) {
          if (!doorConfig?.baseConfig) {

            const startTime = new Date();
            startTime.setHours(doorConfig?.startTime?.split(":")[0], doorConfig?.startTime?.split(":")[1])
            const endTime = new Date();
            endTime.setHours(doorConfig?.endTime?.split(":")[0], doorConfig?.startTime?.split(":")[1])
            if (startTime < currentDate && endTime > currentDate) {
              return doorConfig
            }
          }
        }

        for (const doorConfig of door.doorConfigCmds) {
          if (doorConfig.baseConfig) {
            return doorConfig;
          }
        }
      } else {
        return door.doorConfigCmds[0]
      }
    }

    function getInactiveConfig(door) {
      const activeConfig = getActiveConfig(door)
      if (activeConfig) {
        return door.doorConfigCmds.filter((config) => config.id !== activeConfig.id).map(config => ({
          ...config,
          doorName: door.name
        }))
      } else {
        return []
      }
    }

    api.room.getRoomDetails(props.room.id)
        .then(room => {
          config.value = room.data
          loading.value = false
          activeConfigs.value = room.data.doors.map(door => ({
            ...getActiveConfig(door), doorName: door.name
          }))
          inactiveConfigs.value = room.data.doors.flatMap(d => getInactiveConfig(d))
        })
        .catch(() => loading.value = false)
    return {config, activeConfigs, inactiveConfigs, userStore, getActiveConfig, getInactiveConfig}
  }
}
</script>

<style scoped>

</style>