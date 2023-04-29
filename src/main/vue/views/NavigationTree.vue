<template>
  <q-drawer
      show-if-above bordered
      content-class="bg-grey-1">
    <q-list>
      <q-item-label
          header
          class="text-grey-8">
        Locations
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
  </q-drawer>
</template>

<script>
import {useLocationStore} from "@/main/vue/stores/locations";
import Node from "@/main/vue/views/Node.vue";
import {ref} from "vue";
import {useFloorPlanStore} from "@/main/vue/stores/floorPlan";


export default {
  name: "NavigationTree",
  components: {Node},
  setup() {
    const locationStore = useLocationStore()
    const floorPlanStore = useFloorPlanStore()
    let locationTreeStructure = ref([])

    locationStore.getLocations().then((locations) => {
          const initialFloor = locations[0].buildings[0].floors[0];
          floorPlanStore.selectedFloorPlan = initialFloor.floorPlanPath;
          floorPlanStore.selectedFloorId = initialFloor.id;

          locationTreeStructure.value = locations.map((location, index) => ({
            id: location.id,
            title: location.name,
            level: 0,
            expanded: index === 0,
            children: location.buildings.map((building, index) => ({
              id: building.id,
              title: building.name,
              level: 1,
              expanded: index === 0,
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


    return {locationTreeStructure}
  }
}
</script>

<style scoped>

</style>