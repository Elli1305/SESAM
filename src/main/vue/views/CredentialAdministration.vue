<template>
  <div class="q-pa-md">
    <div class="q-mb-xl">
      <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">
        Credentialverwaltung</h1>
    </div>
    <q-table
        :columns="columns"
        :filter="search"
        :rows="rows"
        row-key="name"
    >
      <template v-slot:top-left>
        <q-btn borderless color="grey-6" flat icon="add" label="Neues Credential" to="/add_credential"/>
      </template>
      <template v-slot:top-right>
        <q-select v-model="selectedTypes" :options="options" borderless emit-value label="Art" map-options multiple/>
        <q-input v-model="filter" borderless class="q-ml-lg" debounce="300" dense placeholder="Suche">
          <template v-slot:append>
            <q-icon name="search"/>
          </template>
        </q-input>
      </template>
      <template v-slot:body-cell-name="props">
        <q-td :props="props">
          <router-link :to="`/credential_administration/${'issuer' in props.row ? 'internal' : 'external'}/${props.row.id}`">{{ props.value }}</router-link>
        </q-td>
      </template>
      <template v-slot:body-cell-type="props">
        <q-td :props="props">
          <q-chip v-if="props.value" color="primary" label="Internal" text-color="white"/>
          <q-chip v-else color="secondary" label="External" text-color="white"/>
        </q-td>
      </template>
    </q-table>
  </div>
</template>

<script lang="ts" setup>
import {QSelectOption, QTableColumn} from "quasar";
import {useCredentialsStore} from "@/main/vue/stores/credential";
import {computed, Ref, ref} from "vue";
import {Credential, ExternalCredential} from "@/main/vue/entity/credentialDefinition";

const columns: QTableColumn<Credential | ExternalCredential>[] = [
  {
    name: 'name',
    required: true,
    label: 'Name',
    align: 'left',
    field: 'name',
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
  {
    name: 'agent',
    required: false,
    label: 'Agent',
    align: 'left',
    field: (row) => "agent" in row ? row.agent : null,
    format: (val) => val ?? '–',
    sortable: true,
  },
  {
    name: 'type',
    required: true,
    label: 'Typ',
    align: 'center',
    field: (row) => "issuer" in row,
    sortable: true,
  },
];

const options: QSelectOption[] = [
  {
    label: 'Internal',
    value: 'internal'
  },
  {
    label: 'External',
    value: 'external'
  },
];

const selectedTypes: Ref<string[]> = ref(['internal', 'external']);

const search = ref('');
const store = useCredentialsStore();

store.fetch();

const rows = computed(
    () => [...store.credentials, ...store.externalCredentials]
        .filter((value) => (selectedTypes.value.includes('internal') && 'issuer' in value) || (selectedTypes.value.includes('external') && !('issuer' in value)))
);
</script>

<style scoped></style>