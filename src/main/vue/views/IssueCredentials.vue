<template>
  <div v-for="credential in credentials">
    <router-link :to="'/credentials/' + credential.id + '/issue'">{{ credential.name }}</router-link>
  </div>
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