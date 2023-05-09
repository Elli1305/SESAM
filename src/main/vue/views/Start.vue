<template>
  <NavigationTree></NavigationTree>
  <FloorPlan></FloorPlan>
</template>

<script>
import NavigationTree from "@/main/vue/views/NavigationTree.vue";
import FloorPlan from "@/main/vue/views/FloorPlan.vue";

export default {
  name: "Start",
  components: {FloorPlan, NavigationTree}
}
</script>

<style scoped>

</style>