<template>
<q-page-container class="no-padding no-margin">
    <q-page style="padding-right: 1em; padding-top: 2em">
      <q-input
          :label="t('home.roomSearch')"
          v-model="search"
          @update:model-value="roomFilter"
          clearable
          outlined
          style="margin-bottom: 1em; min-width: 20em">
        <template v-slot:append>
          <q-icon name="search" />
        </template>
      </q-input>
        <q-list>
          <q-item  v-for="room in filteredRooms" style="padding-left: 0">
            <q-btn-dropdown
                split
                style="min-width: 16em"
                :label="room.name"
                color="primary"
                @click="toggleRoomCheckbox(room)"
            >
              <div class="col no-wrap">
                <div class="row" style="padding: 0.5em">
                  <q-list>
                    <q-item-label>Raumbezeichnung: {{room.name}}</q-item-label>
                    <q-item-label>Raumtyp: Meetingraum</q-item-label>
                    <q-item-label>Credentials: 1,2,3,4</q-item-label>
                  </q-list>
                </div>
                <div v-if="userStore.authenticated">
                  <q-separator spaced></q-separator>
                  <div class="row justify-end" style="padding: 0.5em">
                    <q-btn size="sm" label="Bearbeiten" color="blue" @click="func(room)" ></q-btn>
                      <q-dialog v-model="inception">
                          <q-card>
                              <q-card-section>
                                  <div class="text-h6">Raum Bearbeiten</div>
                              </q-card-section>
                              <q-card-section>
                                  <q-input filled v-model="room.name" label="Raumname" stack-label :dense="dense" />
                              </q-card-section>

                              <q-card-section>
                                  <q-list bordered class="rounded-borders" style="max-width: 600px">
                                      <q-item-label header>
                                          <div class="row items-center">
                                              <div class="q-mr-sm">Türe</div>
                                              <q-btn round size="sm" icon="add" @click="addDoorDialog = true" class="ml-auto" /> <!-- Modified class -->
                                          </div>
                                      </q-item-label>
                                      <template v-for="door in room.doors">
                                          <q-item>
                                              <q-item-section avatar top>
                                                  <q-icon name="crop_portrait" color="black" size="34px" />
                                              </q-item-section>

                                              <q-item-section >
                                                  <q-item-label lines="1">
                                                      <span class="text-weight-medium">{{ door.name }}</span>
                                                  </q-item-label>

                                              </q-item-section>

                                              <q-item-section top side>
                                                  <div class="text-grey-8 q-gutter-xs">
                                                      <q-btn class="gt-xs" size="12px" flat dense round icon="delete" @click = "deleteDoorDialog= true" />
                                                      <q-btn class="gt-xs" size="12px" flat dense round icon="done" @click = "save(room)" />
                                                      <q-btn size="12px" flat dense round icon="edit" @click="fourthDialog = true"/>
                                                  </div>
                                              </q-item-section>
                                          </q-item>
                                      </template>
                                  </q-list>
                              </q-card-section>
                              <q-card-actions align="right" class="text-primary">
                                  <q-btn flat label="speichern" @click="addDoor(room, newRoomName, newRoomCoord)"/>
                                  <q-btn flat label="abbrechen" v-close-popup />
                              </q-card-actions>
                          </q-card>
                      </q-dialog>
                      <div class="row justify-end" style="padding: 0.5em">
                      <q-dialog v-model="addDoorDialog" persistent transition-show="scale" transition-hide="scale">
                          <q-card>
                              <q-card-section>
                                  <div class="text-h6">Tür Hinzufuegen</div>
                              </q-card-section>
                              <q-card-section>

                                  <q-input filled v-model="newDoorName" label="Türname" stack-label :dense="dense" />
                                  <q-separator spaced />
                                  <q-input filled v-model="newDoorCoord" label="Tür Coordinaten" stack-label :dense="dense" />
                              </q-card-section>


                              <q-card-actions align="right" class="bg-white text-teal">
                                  <q-btn flat label="speichern" color = "primary" />
                                  <q-btn flat label="abbrechen"  color = "primary" v-close-popup />
                              </q-card-actions>
                          </q-card>
                      </q-dialog>
                      <q-dialog v-model="deleteDoorDialog" persistent transition-show="scale" transition-hide="scale">
                          <q-card class="bg-teal text-white" style="width: 300px">
                              <q-card-section>
                                  <div class="text-h6">Persistent</div>
                              </q-card-section>

                              <q-card-section class="q-pt-none">
                                  Bist du sicher dass du die Tür löschen möchtest?
                              </q-card-section>

                              <q-card-actions align="right" class="bg-white text-teal">
                                  <q-btn flat label="Ja" @click="deleteDoor(room, door)" v-close-popup />
                                  <q-btn flat label="Nein" v-close-popup />
                              </q-card-actions>
                          </q-card>
                      </q-dialog>
                          <q-dialog v-model="fourthDialog" persistent transition-show="scale" transition-hide="scale">
                              <q-card>
                                  <q-card-section>
                                      <div class="text-h6">Tür Bearbeiten</div>
                                  </q-card-section>
                                  <q-card-section>
                                      <q-input filled v-model="currentDoorName" label="Türname" stack-label :dense="dense" />
                                      <q-separator spaced />
                                      <q-input filled v-model="CurrentDoorCoord" label="Tür Coordinaten" stack-label :dense="dense" />
                                  </q-card-section>


                                  <q-card-actions align="right" class="bg-white text-teal">
                                      <q-btn flat label="speichern" />
                                      <q-btn flat label="abbrechen" v-close-popup />
                                  </q-card-actions>
                              </q-card>
                          </q-dialog>

                      </div>
                  </div>
                </div>
              </div>
            </q-btn-dropdown>
            <q-checkbox v-model="selectedRooms" :val="room" color="blue"></q-checkbox>
          </q-item>
        </q-list>
    </q-page>
</q-page-container>
</template>

<script>
import {useFloorPlanStore} from "@/main/vue/stores/floorPlan";
import {useUserStore} from "@/main/vue/stores/users";
import {ref, watch} from "vue";
import { useI18n } from 'vue-i18n';
import {storeToRefs} from "pinia";

export default {
  name: "FloorPlanRoomList",

  setup() {
    const { t } = useI18n();
    const floorPlanStore = useFloorPlanStore()
    const { rooms } = storeToRefs(floorPlanStore)
    const selectedRooms = floorPlanStore.selectedRooms
    const userStore = useUserStore()
    const filteredRooms = ref([])
    const search = ref()
    const inception = ref();
    const addDoorDialog = ref();
    const deleteDoorDialog = ref();
      const fourthDialog = ref();
      const text = ref();
      const newDoorName = ref();
      const newDoorCoord = ref();
      const currentDoorname = ref();
      const currentDoorCoord = ref();
    filteredRooms.value = rooms.value

    watch(rooms, () => {
      filteredRooms.value = rooms.value
    })

    function addAllRooms() {
        floorPlanStore.rooms.forEach((element) => {
            selectedRooms.push(element)
        })
    }

    // to do function func(room)
    //to do function  deleteDoor(room, door)

    //to do function addDoor(room, name, coordinates)

    //to do define save(room)

    function deleteAllRooms() {
      floorPlanStore.selectedRooms.length = 0
    }

    function addRoom(element) {
      selectedRooms.push(element)
    }

    function deleteRoom(element) {
      selectedRooms.forEach( (item, index) => {
        if(item === element) selectedRooms.splice(index,1);
      });
    }

    function toggleRoomCheckbox(element) {
      if(selectedRooms.includes(element)) {
        deleteRoom(element)
      } else if(!selectedRooms.includes(element)) {
        addRoom(element)
      }
    }

    async function roomFilter() {
      if (!search.value || search.value.trim() === '') {
        filteredRooms.value = floorPlanStore.rooms
      } else if(search.value) {
        const request = search.value.toLowerCase().trim()
        filteredRooms.value = floorPlanStore.rooms.filter(room => {
          return room.name.toLowerCase().includes(request)
        })
      }
    }
    return{floorPlanStore, selectedRooms, toggleRoomCheckbox, userStore, search, filteredRooms, roomFilter, t, inception, addDoorDialog, deleteDoorDialog, func, text, deleteDoor, addDoor, newDoorName, newDoorCoord, currentDoorname, currentDoorCoord, fourthDialog}
  }

}
</script>

<style scoped>

</style>