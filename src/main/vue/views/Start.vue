<template>
  <NavigationTree></NavigationTree>
  <Map></Map>
</template>

<script>
import Map from "@/main/vue/views/Map.vue";

import {ref} from "vue";
import router from "@/main/vue/router";
import NavigationTree from "@/main/vue/views/NavigationTree.vue";

export default {
  name: "Start",
  components: {Map, NavigationTree}
}
</script>

<style scoped>

</style>