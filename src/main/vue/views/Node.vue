<template>
  <div>
    <div v-if="children.length === 0">
      <q-item clickable v-ripple :inset-level="level">
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
            default-closed>
          <Node
              v-for="child in children"
              :title="child.title"
              v-bind="child">
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

export default {
  name: "Node",
  props: {
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
  },
  setup() {
    const active = true

    const routerPush = (event) => {
      console.log(event)
    }
    return {active, routerPush}
  }
}
</script>