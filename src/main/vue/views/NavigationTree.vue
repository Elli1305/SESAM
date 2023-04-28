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
      >
      </Node>
    </q-list>
  </q-drawer>
</template>

<script>
import {useLocationStore} from "@/main/vue/stores/locations";
import Node from "@/main/vue/views/Node.vue";
import {ref} from "vue";


export default {
  name: "NavigationTree",
  components: {Node},

  setup() {
    const locationStore = useLocationStore()
    let locationTreeStructure = ref([])
    const buildTreeStructure = async () => {
      const locations = await locationStore.getLocations()

      return locations.map((location) => {
        return {
          id: location.id,
          title: location.name,
          level: 0,

          children: location.buildings.map((building) => {
            return {
              id: building.id,
              title: building.name,
              level: 1,
              children: building.floors.map(floor => {
                return {
                  id: floor.id,
                  level: 2,
                  title: "Etage " + floor.floorLevel
                }
              })
            }
          })
        }
      })
    }

    buildTreeStructure().then(res => locationTreeStructure.value = res)


    return {locationTreeStructure}
  }
}
</script>

<style scoped>

</style>