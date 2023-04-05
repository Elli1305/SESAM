<script setup>
import {RouterView} from 'vue-router'



</script>
<template>
  <q-layout view="hHh lpR fFf">

    <q-header elevated class="bg-primary text-white" height-hint="98">
      <q-toolbar>
        <q-toolbar-title>
          GPSE 2023 Template Project
        </q-toolbar-title>
      </q-toolbar>

      <q-tabs align="left">
        <q-route-tab to="/page1" label="Page One"/>
        <q-route-tab to="/page2" label="Page Two"/>
        <q-route-tab to="/page3" label="Page Three"/>
      </q-tabs>
    </q-header>

    <q-page-container>
      <router-view/>
    </q-page-container>

    <q-footer elevated class="bg-grey-8 text-white">
      <q-toolbar>
        <q-toolbar-title>
          <div>This is a footer. Hi!</div>
        </q-toolbar-title>
      </q-toolbar>
    </q-footer>

  </q-layout>


</template>

<style scoped>
</style>
