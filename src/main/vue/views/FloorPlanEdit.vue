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
          :default-closed="i!==1">
        <template v-slot:header>
          <q-item-section>
            {{ location.name }}
          </q-item-section>
          <q-item-section side>
            <q-icon name="edit" @click.stop="editLocation(location)" v-ripple
                    style="border-radius: 50%; display: flex; width: 33px; height: 33px"/>
          </q-item-section>
        </template>
        <q-expansion-item
            v-for="(building,i) in location.buildings"
            :label="building.name"
            :header-inset-level="1"
            :default-opened="i===0"
            :default-closed="i!==1"
            default-closed>
          <template v-slot:header>
            <q-item-section>
              {{ building.name }}
            </q-item-section>
            <q-item-section side>
              <q-icon name="edit" @click.stop="editBuilding(building)" v-ripple
                      style="border-radius: 50%; display: flex; width: 33px; height: 33px"/>
            </q-item-section>
          </template>
          <q-item clickable v-ripple v-for="(floor,i) in building.floors" :inset-level="2"
                  @click="changeFloorPlan(floor)" :active="i===0">
            <q-item-section>{{ floor.floorLevel === 0 ? "Erdgeschoss" : "Etage " + floor.floorLevel }}</q-item-section>
            <q-item-section side>
              <q-icon name="edit" @click.stop="editFloor(floor)" v-ripple
                      style="border-radius: 50%; display: flex; width: 33px; height: 33px"/>
            </q-item-section>
          </q-item>
        </q-expansion-item>
      </q-expansion-item>
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
  <floor-plan :edit-view="true">
  </floor-plan>

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


export default {
  name: 'FloorPlanEdit',
  components: {FloorPlan},
  methods: {},
  setup() {
    const locationStore = useLocationStore()
    const floorPlanStore = useFloorPlanStore()
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

    const editBuilding = function (building: Building) {
      $q.dialog({
        component: EditBuilding,
        componentProps: {
          building: building
        }
      }).onOk(() => {
        $q.notify({
          type: 'positive',
          message: 'Gebäude wurde erfolgreich gespeichert',
          position: 'bottom',
          timeout: 3000,
        });
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

    locationStore.getLocations().then((res: Location[]) => {
      changeFloorPlan(res[0].buildings[0].floors[0])
    })


    return {show, t, locationStore, changeFloorPlan, editLocation, editBuilding, editFloor}
  }
}
</script>