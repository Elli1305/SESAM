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
          <div style="padding-right: 1em">
          <q-btn dense flat color="grey" label="New categoty"  :disable="loading" icon="add" rounded @click="addRow"/>
          </div>
          <q-input v-model="filter" placeholder="Search" dense>
            <template v-slot:append>
              <q-icon name="search" />
            </template>
          </q-input>
        </template>
        <template v-slot:body-cell-actions="props">
          <q-td :props="props">
            <q-btn dense round flat color="grey" to="/credentialmapping/edit"
                   test="props.value" icon="edit"></q-btn>
            <q-btn dense round flat color="grey" icon="delete" :disable="loading" @click="removeRow" />
          </q-td>
        </template>
      </q-table>
    </div>
    </div>
  </q-page>
  <q-dialog
      v-model="deleteAlert"
  >
    <q-card style="width: 400px">
      <q-card-section>
        <div class="text-h6">Warning</div>
      </q-card-section>

      <q-card-section class="q-pt-none">
        "Möchten Sie die Kategorie löschen?"
      </q-card-section>

      <q-card-actions align="right" class="bg-white text-teal">
        <q-btn flat :label="Nein" v-close-popup/>
        <q-btn flat :label="Ja" @click="deleteCategory()"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
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
  {name: 'actions', align: 'center', label: 'Bearbeiten'}
]

const rows = ref([
  {
    category: 'Erste-Hilfe-Kurs', credential: 'Erste-Hilfe-Kurs DRK', externalCredential: 'Erste-Hilfe-Kurs DRLG'
  }
]);

export default {
  name: "CredentialMappingList",
  setup() {
    function deleteCategory() {
    }return {
      rows,
      columns,
      deleteAlert: ref(false),
      filter: ref(''),
      // emulate fetching data from server
      addRow () {
        loading.value = true
        setTimeout(() => {
          const
              index = Math.floor(Math.random() * (rows.value.length + 1)),
              row = originalRows[ Math.floor(Math.random() * originalRows.length) ]

          if (rows.value.length === 0) {
            rowCount.value = 0
          }

          row.id = ++rowCount.value
          const newRow = { ...row } // extend({}, row, { name: `${row.name} (${row.__count})` })
          rows.value = [ ...rows.value.slice(0, index), newRow, ...rows.value.slice(index) ]
          loading.value = false
        }, 500)
      },

      removeRow () {
        loading.value = true
        setTimeout(() => {
          const index = Math.floor(Math.random() * rows.value.length)
          rows.value = [ ...rows.value.slice(0, index), ...rows.value.slice(index + 1) ]
          loading.value = false
        }, 500)
      }
    }
  }
}

</script>

<style scoped>

</style>