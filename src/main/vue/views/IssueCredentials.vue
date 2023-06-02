<template>

  <div class="q-gutter-y-md column" style="align-items: center; min-width: 20em; display: flex">
    <q-table
        title="Credentials"
        no-data-label="No Credentials found"
        bordered
        :rows="rows"
        :columns="columns"
        row-key="name"
        :filter="filter"
        style="min-width: 60em; margin-top: 10em">

      <template v-slot:body-cell-actions="props">
        <q-td :props="props">
          <q-btn color="primary" label="Ausstellen" :to="'/credentials/' + props.row.id + '/issue'"/>
        </q-td>
      </template>
    </q-table>
  </div>
</template>

<script setup lang="ts">
import api from '@/main/vue/api';
import {Credential} from "@/main/vue/entity/credentialDefinition";
import {ref, Ref} from "vue";
import {useCredentialStore} from "@/main/vue/stores/credential";
import {useUserStore} from "@/main/vue/stores/users";

const credentialStore = useCredentialStore()
const userStore = useUserStore()
const credentials: Ref<Credential[]> = ref([]);
const filter = ref('')
const columns = [
  {
    name: 'name',
    required: true,
    align: 'center',
    field: 'name',
    sortable: true
  },
  { name: 'actions', align: 'center'}
]

const rows: Ref<Credential[]> = ref([])
credentialStore.getCredentialsByIssuer(userStore.user?.id).then((response) => {
  rows.value = response
})

api.credential.all().then((r) => credentials.value = r.data);
</script>

<style scoped>
</style>