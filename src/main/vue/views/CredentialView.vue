<template xmlns:margin="http://www.w3.org/1999/xhtml">
  <q-page class="column justify-evenly" style="padding: 0.5em" >
    <div class="q-gutter-y-md column" style="width: 80%; display: flex; margin:0 auto">
      <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">{{t("credentialview.credentialview")}}</h1>
    <div class="q-pa-md">
      <q-table
          flat bordered
          :rows="rows"
          :columns="columns"
          :title="t('credentialview.credentialview')"
          :separator="'cell'"
          :filter="filter"
          row-key="name"
      >
        <template v-slot:top-right>
          <q-select
              :label="t('credentialview.location')"
              behavior="menu"
              v-model="model"
              multiple
              borderless
              dense
              options-dense
              emit-value
              map-options
              :options="pov"
              option-value="name"
              options-cover
              style="min-width: 13em; padding-right: 1em"
          />
          <q-input dense debounce="300" v-model="filter" :placeholder="t('credentialview.search')">
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
              <div class="text-left" style="padding: 1em">{{t('credentialview.credentialtext')}} <b>{{ props.row.issuer }}</b><br>
                {{t('credentialview.credentialtext2')}} <b>0.007</b> <br>
                {{t('credentialview.credentialtext3')}} <b>{{ props.row.qualification}}</b> <br>
                {{t('credentialview.credentialtext4')}} <b>{{props.row.category}} </b>
              </div>
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
import {useCredentialStore} from "@/main/vue/stores/credential";
import {useUserStore} from "@/main/vue/stores/users";

const filter=ref('')
const columns = [
  { name: 'category', required: true, label: "Kategorie", align: 'center', field: 'category', sortable: true },
  { name: 'availableCredential', align: 'center', label : 'Verfügbares Credential' , field: 'availableCredential', sortable: true },
  { name: 'qualification', align: 'center', label: 'Vergleichbare Qualifikation', field: 'qualification', sortable: true },
  { name: 'issuer', align: 'center', label: 'Herausgeber', field: 'issuer', sortable: true },
]

const rows = [
  {
    category: "Erste-Hilfe-Kurs",
    availableCredential: "1",
    qualification: "2",
    issuer: "Gerda Peters",
  },
  {
    category: "Sicherheitsunterweisung",
    availableCredential: "3",
    qualification: "4",
    issuer: "Gerda Peters",
  }
]

export default {
  setup () {
    const { t } = useI18n();
    const credentialStore = useCredentialStore()
    //credentialStore.getCategories().then(res => rows.value = res.data)
    return {
      columns,
      rows,
      filter,
      model: ref(null),
      pov: ref(["Berlin", "Bielefeld"]),
      t
    }
  }
}
</script>

<style scoped>

</style>