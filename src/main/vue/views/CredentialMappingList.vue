<template>
  <q-page class="items-center justify-center" style="display: flex">
    <div class="q-gutter-y-md column" style="max-width: 80%; min-width: 20em; display: flex">
      <div>
      <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">Credentialmapping</h1>
      </div>
      <div style="width: 100em">
      <q-table
          flat bordered
          ref="tableRef"
          title="Credentialmapping"
          :rows="rows"
          :columns="columns"
          row-key="id"
          :separator="'cell'"
          v-model:pagination="pagination"
          :loading="loading"
          :filter="filter"
          binary-state-sort
          @request="onRequest"
      >
        <template v-slot:top-right>
          <q-input borderless dense debounce="300" v-model="filter" placeholder="Search">
            <template v-slot:append>
              <q-icon name="search" />
            </template>
          </q-input>
        </template>
        <template v-slot:header="props">
          <q-tr :props="props">
            <q-th auto-width />
            <q-th
                v-for="col in props.cols"
                :key="col.name"
                :props="props"
            >
              {{ col.label }}
            </q-th>
          </q-tr>
        </template>

        <template v-slot:body="props">
          <q-tr :props="props">
            <q-td auto-width>
              <q-btn size="sm" color="accent" round dense icon="create" />
            </q-td>
            <q-td
                v-for="col in props.cols"
                :key="col.name"
                :props="props"
            >
              {{ col.value }}
            </q-td>
          </q-tr>
        </template>
      </q-table>
    </div>
    </div>
  </q-page>
</template>

<script>
import {ref} from 'vue'

import {useI18n} from "vue-i18n";
import {useRouter} from 'vue-router'
const columns = [
  {
    name: 'category',
    required: true,
    label: 'Kategorie',
    align: 'center',
    field: 'category',
    sortable: true
  },
  {name: 'credential', align: 'center', label: "Credential (intern)", field: 'credential', sortable: true},
  {
    name: 'externalCredential',
    align: 'center',
    label: 'Credential (extern)',
    field: 'externalCredential',
    sortable: true
  },
]

const rows = ref([
  {
    category: 'Erste-Hilfe-Kurs', credential: 'Erste-Hilfe-Kurs DRK', externalCredential: 'Erste-Hilfe-Kurs DRLG'
  }
]);

export default {
  name: "CredentialMappingList",
  setup() {
    return {
      rows,
      columns,
      filter: ref('')
    }
  }
}

</script>

<style scoped>

</style>