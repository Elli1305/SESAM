<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">Credentials</p>
    <div class="column content-center self-center justify-around" style="width: 80vw; height: 25em">
      <router-link v-for="credential in credentials" :to="'/credentials/' + credential.id + '/issue'">
        <q-btn size="1.25em" color="primary" flat>{{ credential.name }}</q-btn>
      </router-link>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import api from '@/main/vue/api';
import {Credential} from "@/main/vue/entity/credentialDefinition";
import {ref, Ref} from "vue";

const credentials: Ref<Credential[]> = ref([]);

api.credential.all().then((r) => credentials.value = r.data);
</script>

<style scoped>
</style>