<template>
<q-page-container class="no-padding no-margin">
    <q-page style="padding-right: 1em; padding-top: 2em">
        <q-list>
          <q-item  v-for="room in floorPlanStore.rooms" style="padding-left: 0">
            <q-checkbox v-model="selectedRooms" :val="room" color="blue"></q-checkbox>
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
                    <q-btn size="sm" label="Bearbeiten" color="blue"></q-btn>
                  </div>
                </div>
              </div>
            </q-btn-dropdown>
          </q-item>
        </q-list>
    </q-page>
</q-page-container>
</template>

<script>
import {useFloorPlanStore} from "@/main/vue/stores/floorPlan";
import {useUserStore} from "@/main/vue/stores/users";

export default {
  name: "FloorPlanRoomList",

  setup() {
    const floorPlanStore = useFloorPlanStore()
    const selectedRooms = floorPlanStore.selectedRooms
    const userStore = useUserStore()

    function addAllRooms() {
        floorPlanStore.rooms.forEach((element) => {
            selectedRooms.push(element)
        })
    }

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
      console.log(selectedRooms.includes(element))
      if(selectedRooms.includes(element)) {
        deleteRoom(element)
      } else if(!selectedRooms.includes(element)) {
        addRoom(element)
      }
    }

    return{floorPlanStore, selectedRooms, toggleRoomCheckbox, userStore}
  }

}
</script>

<style scoped>

</style>