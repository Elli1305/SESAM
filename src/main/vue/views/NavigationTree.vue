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
      <Node
          v-for="node in locationTreeStructure"
          :title="node.title"
          v-bind="node"
          :id=node.id
          :expanded="node.expanded"
      >
      </Node>
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

</template>

<script>
import {useLocationStore} from "@/main/vue/stores/locations";
import Node from "@/main/vue/views/Node.vue";
import {ref} from "vue";
import {useFloorPlanStore} from "@/main/vue/stores/floorPlan";
import {useI18n} from "vue-i18n";


export default {
  name: "NavigationTree",
  components: {Node},
  setup() {
    const locationStore = useLocationStore()
    const floorPlanStore = useFloorPlanStore()
    let locationTreeStructure = ref([])
    let show = ref(true)
    const {t} = useI18n()

    function getParentIDs(locations, selectFloorId) {
      for (const location of locations) {
        for (const building of location.buildings) {
          if (building.floors.some(floor => floor.id === selectFloorId)) {
            return {locationId: location.id, buildingId: building.id}
          }
        }
      }
      return null;
    }


    locationStore.getLocations().then((locations) => {
          let locationId, buildingId;
          if (!floorPlanStore.selectedFloorPlan) {
            const initialFloor = locations[0].buildings[0].floors[0];
            locationId = locations[0].id
            buildingId = locations[0].buildings[0].id
            floorPlanStore.selectedFloorPlan = initialFloor.floorPlanPath;
            floorPlanStore.selectedFloorId = initialFloor.id;
            floorPlanStore.rooms = initialFloor.rooms;
          } else {
            ({locationId, buildingId} = getParentIDs(locations, floorPlanStore.selectedFloorId));
          }

          locationTreeStructure.value = locations.map((location) => ({
            id: location.id,
            title: location.name,
            level: 0,
            expanded: location.id === locationId,
            children: location.buildings.map((building) => ({
              id: building.id,
              title: building.name,
              level: 1,
              expanded: building.id === buildingId,
              children: building.floors.map(floor => ({
                id: floor.id,
                level: 2,
                floorPlan: floor.floorPlanPath,
                title: "Etage " + floor.floorLevel
              }))
            }))
          }))
        }
    )


    return {locationTreeStructure, show, t}
  }
}
</script>

<style scoped>

</style>