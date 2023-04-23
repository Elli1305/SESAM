<template>
  <NavigationTree></NavigationTree>
  <div><p>Hier kommt der Floorplan rein</p></div>
</template>

<script>
import {ref} from "vue";
import router from "@/main/vue/router";
import NavigationTree from "@/main/vue/views/NavigationTree.vue";

export default {
  name: "Start",
  components: {NavigationTree}
}
</script>

<style scoped>

</style>