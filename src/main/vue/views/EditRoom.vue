<template>
  <q-dialog ref="dialog" persistent>
    <q-card style="background-color: var(--bg-color); color: var(--text-color)">
      <q-card-section>
        <div class="text-h6">{{ t("floorPlan.editRoom") }}</div>
      </q-card-section>
      <q-card-section class="q-py-none">
        <q-input filled v-model="currentRoomName"
                 :label="t( 'floorPlan.roomName')"
                 stack-label/>
      </q-card-section>
      <q-card-section>
        <q-list bordered class="rounded-borders"
                style="width: 20em">
          <q-item-label header>
            <div class="row items-center">
              <div class="q-mr-sm">{{
                  t("floorPlan.doors")
                }}
              </div>

            </div>
          </q-item-label>
          <q-item v-for="door in room.doors" class="q-mb-sm">
            <q-item-section avatar>
              <q-icon name="meeting_room" size="2em"/>
            </q-item-section>

            <q-item-section>
              <q-item-label lines="1">
                {{ door.name }}
              </q-item-label>
            </q-item-section>

            <q-item-section top side>
              <div class="row">
                <q-btn
                    size="1em"
                    flat dense round
                    icon="edit"
                    style="color: var(--light)"
                    @click="openDialog(door)"/>
                <q-btn
                    size="1em"
                    flat dense round
                    icon="delete"
                    style="color: var(--light)"
                    @click="deleteDoorDialog= true"/>
              </div>
            </q-item-section>

            <q-dialog v-model="deleteDoorDialog" persistent>
              <q-card style="background-color: var(--bg-color); color: var(--text-color)">
                <q-card-section>
                  <div class="text-h6">{{ t("floorPlan.confirmDeletion") }}</div>
                </q-card-section>

                <q-card-section>
                  <div>{{ t("floorPlan.confirmDeletionText") }}</div>
                </q-card-section>

                <q-card-actions align="right">
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
          </q-item>
        </q-list>
      </q-card-section>
      <q-card-actions align="right" class="text-primary">
        <q-btn flat :label="t( 'common.cancel')"
               color="primary" v-close-popup/>
        <q-btn flat :label="t( 'common.save')"
               color="primary" @click="onOkClick(room)"
               v-close-popup/>
      </q-card-actions>
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

    return {t, currentRoomName, deleteDoorDialog, doorStore, roomStore}
  }
}
</script>

<style scoped>

</style>