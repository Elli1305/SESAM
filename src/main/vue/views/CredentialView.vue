<template>
  <q-page class="column justify-evenly" style="padding: 0.5em" >
    <div class="q-gutter-y-md column" style="width: 80%; display: flex; margin:0 auto">
      <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">Credentialansicht</h1>
    <div class="q-pa-md">
      <q-table
          flat bordered
          :rows="rows"
          :columns="columns"
          title="Credentialansicht"
          :separator="'cell'"
          :filter="filter"
          row-key="name"
      >
        <template v-slot:top-right>
          <q-input borderless dense debounce="300" v-model="filter" placeholder="Suchen">
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
              <q-btn size="sm" color="info" round dense @click="props.expand = !props.expand" :icon="props.expand ? 'info' : 'info'" />
            </q-td>
            <q-td
                v-for="col in props.cols"
                :key="col.name"
                :props="props"
            >
              {{ col.value }}
            </q-td>
          </q-tr>
          <q-tr v-show="props.expand" :props="props">
            <q-td colspan="100%">
              <div class="text-left">Das Credential {{ props.row.name }} kann durch Gerda Peters herausgeben werden. Zum Erlangen wird ein Erste-Hilfe-Kurs benötigt. Als vergleichbare Qualifikation kann Credential 2 angebracht werden.</div>
            </q-td>
          </q-tr>
        </template>

      </q-table>
    </div>
    </div>
  </q-page>
  </template>


<script>
import { ref } from 'vue'

import {useI18n} from "vue-i18n";
import axios from "axios";

const columns = [
  { name: 'category', required: true, label: 'Kategorie', align: 'center', field: 'category', sortable: true },
  { name: 'availableCredential', align: 'center', label : 'Verfügbares Credential' , field: 'availableCredential', sortable: true },
  { name: 'qualification', align: 'center',label: 'Vergleichbare Qualifikation', field: 'qualification', sortable: true },
  { name: 'rooms', align: 'center',label: 'Räume', field: 'rooms', sortable: true},
  { name: 'issuer', align: 'center', label: 'Herausgeber', field: 'issuer', sortable: true }
]

const rows = [
  {
    category: "Erste-Hilfe-Kurs",
    availableCredential: "1",
    qualification: "2",
    rooms: "0.007",
    issuer: "Gerda Peters"
  },
  {
    category: "Sicherheitsunterweisung",
    availableCredential: "3",
    qualification: "4",
    rooms: "0.007, 0.113, 0.112, 0.114",
    issuer: "Gerda Peters"
  }
]

export default {
  setup () {
    return {
      columns,
      rows
    }
  }
}
</script>

<style scoped>

</style>