<template>
  <NavigationTree></NavigationTree>
  <q-page class="row no-wrap">
    <FloorPlan @roomClicked="openDetail" @door-created="refresh" @door-removed="closeDetail" class="full-width" ref="floorPlanRef"></FloorPlan>
    <q-separator vertical/>
    <FloorPlanRoomList @doorChanged="redrawRooms" @editRoom="redrawRooms" ref="roomList"></FloorPlanRoomList>
  </q-page>
</template>

<script>
import NavigationTree from "@/main/vue/views/NavigationTree.vue";
import FloorPlan from "@/main/vue/views/FloorPlan.vue";
import FloorPlanRoomList from "@/main/vue/views/FloorPlanRoomList.vue";
import {useFloorPlanStore} from "@/main/vue/stores/floorPlan";
import {useLocationStore} from "@/main/vue/stores/locations";

export default {
  name: "Start",
  components: {FloorPlan, NavigationTree, FloorPlanRoomList},
  methods: {
    redrawRooms() {
      this.$refs.floorPlanRef.removeLayer()
      this.$refs.floorPlanRef.drawRooms(this.floorPlanStore.rooms)
    },
    refresh() {
      this.$refs?.roomList?.refreshDetail();
    },
    openDetail(id) {
      this.$refs?.roomList?.roomClick(this.locationStore.getRoomById(id))
    },
    closeDetail(id) {
      console.log(id)
      console.log(this.$refs?.roomList?.room.id)
      if (this.$refs?.roomList?.room.id === id) {
        this.$refs?.roomList?.back()
      }
    }
  },
  setup() {
    const floorPlanStore = useFloorPlanStore()
    const locationStore = useLocationStore()
    return {floorPlanStore, locationStore}
  }
}
</script>

<style scoped>

</style>