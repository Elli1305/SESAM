<template>
  <NavigationTree></NavigationTree>
  <FloorPlan></FloorPlan>
  <FloorPlanRoomList></FloorPlanRoomList>
</template>

<script>
import NavigationTree from "@/main/vue/views/NavigationTree.vue";
import FloorPlan from "@/main/vue/views/FloorPlan.vue";
import FloorPlanRoomList from "@/main/vue/views/FloorPlanRoomList.vue";

export default {
  name: "Start",
  components: {FloorPlan, NavigationTree, FloorPlanRoomList}
}
</script>

<style scoped>

</style>