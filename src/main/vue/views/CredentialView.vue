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
              borderless
              dense
              options-dense
              emit-value
              map-options
              :options="locationStore.allLocations"
              option-value="id"
              option-label="name"
              options-cover
              style="min-width: 13em; padding-right: 1em"
          />
          <q-input dense debounce="300" v-model="filter" :placeholder="t('credentialview.search')">
            <template v-slot:append>
              <q-icon name="search" />
            </template>
          </q-input>
        </template>
        <template v-slot:body-cell-issuer="props">
          <q-td :props="props">
            {{props.row.issuer}}
            <q-icon class="q-mr-xs" color="grey" size="20px" name="info" />
            <q-tooltip class="bg-grey-8" anchor="top left" self="bottom left"
                       :offset="[0, 8]">
              {{props.row.issuer}} ist in Raum: 000.7
            </q-tooltip>
          </q-td>
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
import {useLocationStore} from "@/main/vue/stores/locations";

const filter=ref('')
const columns = [
  { name: 'category', required: true, label: "Kategorie", align: 'center', field: 'category', sortable: true },
  { name: 'availableCredential', align: 'center', label : 'Verfügbares Credential' , field: 'availableCredential', sortable: true },
  { name: 'qualification', align: 'center', label: 'Vergleichbare Credentials', field: 'qualification', sortable: true },
  { name: 'issuer', align: 'center', label: 'Herausgeber', field: 'issuer', sortable: true },
]

//const rows = ref([''])
const rows = ref([{category: "Erste-Hilfe", availableCredential: "Telekom", qualification: "DRK", issuer: "2"}])

export default {
  setup () {

    let locationNames = ref([])
    const { t } = useI18n();
    const credentialStore = useCredentialStore()
    const locationStore = useLocationStore()
    const model = ref()
    locationStore.getLocations().then((locations) => {
      model.value = locations[0]
    })


    return {
      columns,
      rows,
      filter,
      locationNames,
      locationStore,
      model,
      t
    }
  }
}
</script>

<style scoped>

</style>