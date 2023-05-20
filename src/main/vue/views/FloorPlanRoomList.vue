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
                    <q-btn size="sm" label="Bearbeiten" color="blue"></q-btn>
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
    filteredRooms.value = rooms.value

    watch(rooms, () => {
      filteredRooms.value = rooms.value
    })

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
    return{floorPlanStore, selectedRooms, toggleRoomCheckbox, userStore, search, filteredRooms, roomFilter, t}
  }

}
</script>

<style scoped>

</style>