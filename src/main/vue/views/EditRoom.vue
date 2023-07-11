<template>
  <q-dialog ref="dialog" persistent>
    <q-card>
      <q-card-section>
        <div class="text-h6">{{ t("floorPlan.editRoom") }}</div>
        <div class="q-mt-md">
          <q-input filled v-model="currentRoomName"
                   :label="t( 'floorPlan.roomName')"
                   stack-label
                   style="width: 250px; padding-bottom: 1em"/>
        </div>
        <div class="q-my-xs">
          <q-list bordered class="rounded-borders"
                  style="max-width: 600px">
            <q-item-label header>
              <div class="row items-center">
                <div class="q-mr-sm">{{
                    t("floorPlan.doors")
                  }}
                </div>

              </div>
            </q-item-label>
            <template v-for="door in room.doors">
              <q-item class="q-mb-sm">
                <q-item-section avatar top>
                  <q-icon name="meeting_room"
                          color="black" size="34px"/>
                </q-item-section>

                <q-item-section>
                  <q-item-label lines="1">
                <span class="text-weight-medium">{{
                    door.name
                  }}</span>
                  </q-item-label>

                </q-item-section>

                <q-item-section top side>
                  <div class="text-grey-8 q-gutter-xs">
                    <q-btn class="gt-xs" size="12px"
                           flat dense round
                           icon="delete"
                           @click="deleteDoorDialog= true"/>
                    <q-btn size="12px" flat dense
                           round
                           icon="edit"
                           @click="openDialog(door)"/>
                  </div>
                </q-item-section>
              </q-item>
              <div class="row justify-end">

                <q-dialog v-model="deleteDoorDialog"
                          persistent
                          transition-show="scale"
                          transition-hide="scale">
                  <q-card>
                    <q-card-section class="q-pa-md">
                      <div class="text-h6">{{
                          t("floorPlan.confirmDeletion")
                        }}
                      </div>
                      <div class="q-mt-md">{{
                          t("floorPlan.confirmDeletionText")
                        }}
                      </div>
                    </q-card-section>

                    <q-card-actions align="right"
                                    class="bg-white text-teal">
                      <q-btn flat color="primary"
                             :label="t( 'common.cancel')"
                             v-close-popup/>
                      <q-btn flat color="primary"
                             :label="t('common.delete')"
                             @click="deleteDoor(room, door)"
                             v-close-popup/>
                    </q-card-actions>
                  </q-card>
                </q-dialog>

              </div>
            </template>
          </q-list>
        </div>
        <q-card-actions align="right" class="text-primary">
          <q-btn flat :label="t( 'common.cancel')"
                 color="primary" v-close-popup/>
          <q-btn flat :label="t( 'common.save')"
                 color="primary" @click="onOkClick(room)"
                 v-close-popup/>
        </q-card-actions>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
import {useI18n} from "vue-i18n";
import {ref} from "vue";
import api from "@/main/vue/api";
import CreateDoor from "@/main/vue/views/CreateDoor.vue";
import {useDoorStore} from "@/main/vue/stores/door";
import {useQuasar} from "quasar";
import {useRoomStore} from "@/main/vue/stores/room";

export default {
  name: "EditRoom",
  props: {
    room: {
      required: true
    }
  },
  emits: ['doorChanged'],
  methods: {
    deleteDoor(room, door) {
      const doors = room.doors;
      const index = doors.indexOf(door);
      doors.splice(index, 1);
      room.doors = doors;
      this.deleteDoorDialog = false
    },
    onOkClick(room) {
      room.name = this.currentRoomName;
      this.roomStore.save(room).then((room) =>
          this.$emit('ok', room)
      )
    },
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
    openDialog(door) {
      api.door.getById(door.id).then((door) => {
        this.$q.dialog({
          component: CreateDoor,
          componentProps: {
            door: door.data
          }
        }).onOk(({doorName, configuration}) => {
          console.log(doorName)
          door.data.name = doorName
          door.data.doorConfigCmds = configuration
          api.door.update(door.data).then((door) => {
            this.$emit('changedDoor', door.data.id)
            this.room.doors[this.room.doors.findIndex((d) => d.id === door.data.id)] = door.data
          })
        })
      })
    }
  },
  setup(props) {
    const {t} = useI18n()
    const currentRoomName = ref(props.room.name)
    const deleteDoorDialog = ref();
    const doorStore = useDoorStore();
    const roomStore = useRoomStore();
    const $q = useQuasar()

    return {t, currentRoomName, deleteDoorDialog, doorStore, roomStore}
  }
}
</script>

<style scoped>

</style>