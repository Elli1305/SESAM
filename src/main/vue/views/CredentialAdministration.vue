<template>
  <div class="q-pa-md">
    <div class="q-mb-xl">
      <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">
        Credentials</h1>
    </div>
    <q-table
        :columns="columns"
        :filter="filter"
        :rows="store.credentials"
        row-key="name"
    >
      <template v-slot:top-right>
        <q-btn to="/add_credential" color="grey-6" borderless flat icon="add" label="New Credential" />
        <q-input class="q-ml-md" v-model="filter" borderless debounce="300" dense placeholder="Search">
          <template v-slot:append>
            <q-icon name="search"/>
          </template>
        </q-input>
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
import {useCredentialsStore} from "@/main/vue/stores/credential";
import {ref} from "vue";
import {Credential} from "@/main/vue/entity/credentialDefinition";

const columns: QTableColumn<Credential>[] = [
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
const store = useCredentialsStore();

store.fetch();
</script>

<style scoped></style>