<template>
  <NavigationTree></NavigationTree>
    <div class="q-gutter-x-md" style="display: flex">
        <FloorPlan class="fit"></FloorPlan>
        <FloorPlanRoomList></FloorPlanRoomList>
    </div>
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