<template>
  <div>
    <div v-if="children.length === 0">
      <q-item clickable v-ripple :inset-level="level" @click="changeFloorPlan(floorPlan, id, rooms)"
              :active="floorPlanStore.selectedFloorId === id">
        <q-item-section>{{ title }}</q-item-section>
      </q-item>
    </div>
    <div v-else>
      <div v-if="children.length > 0">
        <!-- {{children}} -->
        <q-expansion-item
            expand-separator
            :icon="icon"
            :label="title"
            :header-inset-level="level"
            v-model="expandedRef"
            default-closed>
          <Node
              v-for="child in children"
              :title="child.title"
              v-bind="child"
              :id="child.id"
              :expanded="child.expanded"
              :rooms="child.rooms"
          >
          </Node>
        </q-expansion-item>
      </div>
      <div v-else>
        <q-item clickable v-ripple :inset-level="level">
          <q-item-section>{{ title }}</q-item-section>
        </q-item>
      </div>
    </div>
  </div>
</template>
<script>

import {useFloorPlanStore} from "@/main/vue/stores/floorPlan";
import {ref} from "vue";

export default {
  name: "Node",
  props: {
    id: {
      type: Number,
      required: true
    },
    expanded: {
      type: Boolean
    },
    title: {
      type: String,
      required: true
    },
    level: {
      type: Number,
    },
    children: {
      type: Array,
      default: []
    },
    icon: {
      type: String,
      default: ''
    },
    floorPlan: {
      type: String,
      default: ''
    },
    rooms: {
      type: Array
    }
  },
  setup(props) {
    const floorPlanStore = useFloorPlanStore()
    const changeFloorPlan = function (floorPlan, id, rooms) {
      floorPlanStore.selectedFloorId = id
      floorPlanStore.selectedFloorPlan = floorPlan
      floorPlanStore.rooms = rooms
    }

    const expandedRef = ref(props.expanded)
    return {changeFloorPlan, floorPlanStore, expandedRef}
  }
}
</script>