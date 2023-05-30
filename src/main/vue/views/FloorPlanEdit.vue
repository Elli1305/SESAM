<template>
  <q-drawer
      show-if-above bordered
      v-model="show"
      content-class="bg-grey-1">
    <q-list>
      <q-item-label
          header
          class="text-grey-8">
        {{ t('floorplan.locations') }}
      </q-item-label>
      <q-expansion-item
          v-for="(location,i) in locationStore.allLocations"
          :header-inset-level="0"
          :default-opened="i===0"
          :default-closed="i!==1"
          class="show-location">
        <template v-slot:header>
          <q-item-section>
            {{ location.name }}
          </q-item-section>
          <q-item-section side>
            <q-btn round @click.stop icon="more_vert" flat class="edit-menu-location">
              <q-menu>
                <q-list style="min-width: 100px">
                  <q-item clickable @click.stop="editLocation(location)" v-close-popup>
                    <q-item-section>Standort bearbeiten</q-item-section>
                  </q-item>
                  <q-item clickable @click.stop="addBuilding(location)" v-close-popup>
                    <q-item-section>Gebäude hinzufügen</q-item-section>
                  </q-item>
                </q-list>
              </q-menu>
            </q-btn>
          </q-item-section>
        </template>
        <q-expansion-item
            v-for="(building,i) in location.buildings"
            :label="building.name"
            :header-inset-level="1"
            :default-opened="i===0"
            :default-closed="i!==1"
            default-closed
            class="show-building"
        >

          <template v-slot:header>
            <q-item-section>
              {{ building.name }}
            </q-item-section>
            <q-item-section side>
              <q-btn round @click.stop icon="more_vert" flat class="edit-menu-building">
                <q-menu class="show-building">
                  <q-list style="min-width: 100px">
                    <q-item clickable @click.stop="editBuilding(building)" v-close-popup>
                      <q-item-section>Gebäude bearbeiten</q-item-section>
                    </q-item>
                    <q-item clickable @click.stop="addFloor(building)" v-close-popup>
                      <q-item-section>Etage hinzufügen</q-item-section>
                    </q-item>
                  </q-list>
                </q-menu>
              </q-btn>
            </q-item-section>
          </template>
          <q-item clickable v-ripple v-for="(floor,i) in building.floors" :inset-level="2"
                  @click="changeFloorPlan(floor)" :active="floorPlanStore.selectedFloorId === floor.id" class="show">
            <q-item-section>{{ floor.floorLevel === 0 ? "Erdgeschoss" : "Etage " + floor.floorLevel }}</q-item-section>

            <q-item-section side>
              <q-btn round icon="more_vert" @click.stop flat class="edit-menu">
                <q-menu>
                  <q-list style="min-width: 100px">
                    <q-item clickable @click.stop="editFloor(floor)" v-close-popup>
                      <q-item-section>Etage bearbeiten</q-item-section>
                    </q-item>
                  </q-list>
                </q-menu>
              </q-btn>
            </q-item-section>
          </q-item>
        </q-expansion-item>
      </q-expansion-item>
      <q-item>
        <q-btn color="primary" icon="add" label="Standort hinzufügen" @click="addLocation" flat/>
      </q-item>
    </q-list>
    <div class="q-mini-drawer-hide absolute" style="top: 15px; right: -17px">
      <q-btn
          dense
          round
          unelevated
          color="primary"
          icon="chevron_left"
          @click="show = false"
      />
    </div>
  </q-drawer>
  <q-fab
      v-if="show === false"
      style="z-index: 10; margin-top: 15px; margin-left: -22px"
      @click="show = true"
      class="absolute"
      label="   "
      label-position="left"
      color="primary"
      icon="menu"
      direction="right"
  ></q-fab>
  <div class="q-gutter-x-md" style="display: flex">
    <FloorPlan :edit-view="true" class="fit"></FloorPlan>
    <FloorPlanRoomList></FloorPlanRoomList>
  </div>

</template>

<script lang="ts">

import FloorPlan from "@/main/vue/views/FloorPlan.vue";
import {useLocationStore} from "@/main/vue/stores/locations";
import {ref} from "vue";
import {useI18n} from "vue-i18n";
import {Building, Floor, Location} from "@/main/vue/entity/location";
import {useFloorPlanStore} from "@/main/vue/stores/floorPlan";
import {useQuasar} from "quasar";
import EditLocation from "@/main/vue/views/EditLocation.vue";
import EditBuilding from "@/main/vue/views/EditBuilding.vue";
import EditFloor from "@/main/vue/views/EditFloor.vue";
import FloorPlanRoomList from "@/main/vue/views/FloorPlanRoomList.vue";
import {useBuildingStore} from "@/main/vue/stores/buildings";


export default {
  name: 'FloorPlanEdit',
  components: {FloorPlanRoomList, FloorPlan},
  methods: {},
  setup() {
    const locationStore = useLocationStore()
    const floorPlanStore = useFloorPlanStore()
    const buildingStore = useBuildingStore()
    let show = ref(true)
    const $q = useQuasar()
    const {t} = useI18n()

    const changeFloorPlan = function (floor: Floor) {
      floorPlanStore.selectedFloorId = floor.id
      floorPlanStore.selectedFloorPlan = floor.floorPlanPath
      floorPlanStore.rooms = floor.rooms
    }
    const editLocation = function (location: Location) {
      $q.dialog({
        component: EditLocation,
        componentProps: {
          location: location
        }
      }).onOk(() => {
        $q.notify({
          type: 'positive',
          message: 'Standort wurde erfolgreich gespeichert',
          position: 'bottom',
          timeout: 3000,
        });
      })
    }

    const addLocation = function () {
      $q.dialog({
        component: EditLocation,
        componentProps: {
          location: {}
        }
      }).onOk(() => {
        $q.notify({
          type: 'positive',
          message: 'Standort wurde erfolgreich hizugefügt',
          position: 'bottom',
          timeout: 3000,
        });
      })
    }

    const editBuilding = function (building: Building) {
      $q.dialog({
        component: EditBuilding,
        componentProps: {
          building: building
        }
      }).onOk(() => {
        $q.notify({
          type: 'positive',
          message: 'Gebäude wurde erfolgreich hizugefügt',
          position: 'bottom',
          timeout: 3000,
        });
      })
    }

    const addBuilding = function (location: Location) {
      $q.dialog({
        component: EditBuilding,
        componentProps: {
          building: {}
        }
      }).onOk((building) => {
        location.buildings.push(building)
        locationStore.save(location).then(() => {
          $q.notify({
            type: 'positive',
            message: 'Gebäude wurde erfolgreich gespeichert',
            position: 'bottom',
            timeout: 3000,
          });
        })
      })
    }

    const editFloor = function (floor: Floor) {
      $q.dialog({
        component: EditFloor,
        componentProps: {
          floor: floor
        }
      }).onOk(() => {
        $q.notify({
          type: 'positive',
          message: 'Etage wurde erfolgreich gespeichert',
          position: 'bottom',
          timeout: 3000,
        });
      })
    }

    const addFloor = function (building: Building) {
      $q.dialog({
        component: EditFloor,
        componentProps: {
          floor: {}
        }
      }).onOk((floor) => {
        building.floors.push(floor)
        buildingStore.save(building).then(_ => {
          $q.notify({
            type: 'positive',
            message: 'Etage wurde erfolgreich hizugefügt',
            position: 'bottom',
            timeout: 3000,
          });
        })
      })
    }

    locationStore.getLocations().then((res: Location[]) => {
      changeFloorPlan(res[0].buildings[0].floors[0])
    })


    return {
      show,
      t,
      locationStore,
      changeFloorPlan,
      editLocation,
      editBuilding,
      editFloor,
      floorPlanStore,
      addFloor,
      addBuilding,
      addLocation
    }
  }
}
</script>
<style>
.show .edit-menu {
  visibility: hidden;
  opacity: 0;
  transition: .3s;
}

.show:hover .edit-menu {
  visibility: visible;
  opacity: 1;
  transition: .3s;
}

.show-building .edit-menu-building {
  visibility: hidden;
  opacity: 0;
  transition: .3s;
}

.show-building:hover .edit-menu-building {
  visibility: visible;
  opacity: 1;
  transition: .3s;
}

.show-location .edit-menu-location {
  visibility: hidden;
  opacity: 0;
  transition: .3s;
}

.show-location:hover .edit-menu-location {
  visibility: visible;
  opacity: 1;
  transition: .3s;
}
</style>