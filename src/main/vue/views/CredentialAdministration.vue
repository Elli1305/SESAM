<template>
  <div class="q-pa-md">
    <q-table
        :columns="columns"
        :filter="filter"
        :rows="store.credentials"
        row-key="name"
        title="Credentials"
    >
      <template v-slot:top-right>
        <q-input v-model="filter" borderless debounce="300" dense placeholder="Search">
          <template v-slot:append>
            <q-icon name="search"/>
          </template>
        </q-input>
        <router-link class="q-ml-md" to="/add_credential">
          <q-icon color="primary" name="add" size="24px"/>
        </router-link>
      </template>
      <template v-slot:body-cell-name="props">
        <q-td :props="props">
          <router-link :to="`/credential_administration/${props.row.id}`">{{ props.value }}</router-link>
        </q-td>
      </template>
    </q-table>
  </div>
</template>

<script lang="ts" setup>
import {QTableColumn} from "quasar";
import {CredentialDefinition, useCredentialStore} from "@/main/vue/stores/credential";
import {ref} from "vue";

const columns: QTableColumn<CredentialDefinition>[] = [
  {
    name: 'name',
    required: true,
    label: 'Name',
    align: 'left',
    field: 'name',
    sortable: true,
  },
  {
    name: 'agent',
    required: true,
    label: 'Agent',
    align: 'left',
    field: 'agent',
    sortable: true,
  },
  {
    name: 'credentialDefinitionId',
    required: true,
    label: 'Credential Definition ID',
    align: 'left',
    field: 'credentialDefinitionId',
    sortable: true,
  },
]

const filter = ref('');
const store = useCredentialStore();

store.fetch();
</script>

<style scoped></style>