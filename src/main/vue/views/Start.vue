<template>
  <NavigationTree></NavigationTree>
 <Map></Map>
  <div><p>Hier kommt der Floorplan rein</p></div>
</template>

<script>
import Map from "@/main/vue/views/Map.vue";

import {ref} from "vue";
import router from "@/main/vue/router";
import NavigationTree from "@/main/vue/views/NavigationTree.vue";

export default {
  name: "Start",
  components: {Map}
  components: {NavigationTree}
}
</script>

<style scoped>

</style>