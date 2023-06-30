<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{ t("credentialView.title") }}</p>
    <div class="row self-center">
      <q-table
          style="width: 80vw; height: 50vh"
          :rows-per-page-options="[0]"
          :rows="rows"
          :columns="columns"
          :title="t('credentialView.title')"
          :separator="'cell'"
          :no-data-label="t('common.noData')"
          :no-results-label="t('common.noResults')"
          :pagination-label="getPaginationLabel"
          :filter="filter"
          row-key="name"
          visible-columns="['category', 'availableCredential', 'qualification', 'issuer']">
        <template v-slot:top-right>
          <q-select
              :label="t('credentialView.location')"
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
              style="min-width: 12em; padding-right: 2em"
              @update:model-value="updateCredentials"/>
          <q-input dense borderless debounce="250" v-model="filter" :placeholder="t('common.search')">
            <template v-slot:append>
              <q-icon name="search"/>
            </template>
          </q-input>
        </template>
        <template v-slot:body-cell-issuer="props">
          <q-td style="" class="column no-wrap" :props="props">
            <div class="row q-my-xs justify-between items-center no-wrap" v-for="(elem, index) in props.row.issuerName">
              <p class="no-margin" style="line-height: 1">{{ props.row.issuerName[index] }}</p>
              <q-icon class="q-ml-md" color="info" size="1em" name="info_outlined">
                <q-tooltip class="bg-grey-8" anchor="top left" self="bottom left" :offset="[0, 8]">
                  {{ t("credentialView.room") }} {{ props.row.room[index] }}
                </q-tooltip>
              </q-icon>
            </div>
          </q-td>
        </template>
      </q-table>
    </div>
  </q-page>
</template>


<script>
import {ref, onMounted} from 'vue'
import {useRoute} from 'vue-router'

import {useI18n} from "vue-i18n";
import {useCredentialStore} from "@/main/vue/stores/credential";
import {useLocationStore} from "@/main/vue/stores/locations";

export default {
  setup() {

    let locationNames = ref([])
    const {t} = useI18n();
    const credentialStore = useCredentialStore()
    const locationStore = useLocationStore()
    const model = ref()
    const route = useRoute()
    const queryParam = ref('')


    const filter = ref('')
    const columns = [
      {
        name: 'category',
        required: true,
        label: t('common.category'),
        align: 'center',
        field: row => row.categoryName,
        format: val => `${val}`,
        sortable: true
      },
      {
        name: 'availableCredential',
        align: 'center',
        label: t('credentialView.availableCredentials'),
        field: row => row.credentialName,
        sortable: true
      },
      {
        name: 'qualification',
        align: 'center',
        label: t('credentialView.qualification'),
        field: row => row.externalCredential,
        format: (val) => val.join(', '),
        sortable: true
      },
      {
        name: 'issuer',
        align: 'center',
        label: t('profile.issuer'),
        field: row => row.issuerName,
        format: (val) => val.join(', '),
        sortable: true
      },
      {name: 'room', align: 'center', label: t('credentialView.room'), sortable: true}
    ]

    let rows = ref([])

    function setLocation(id) {
      locationStore.getLocations().then((locations) => {
        model.value = locations[0].id;
        credentialStore.getCredentialsByLocation(locations[0].id).then((credentials) => {
          rows.value = credentials
        })
        for (const location of locations) {
          for (const building of location.buildings ?? []) {
            for (const floor of building.floors ?? []) {
              for (const room of floor.rooms ?? []) {
                if (Number(room.id) === Number(id)) {
                  model.value = location;
                }
              }
            }
          }
        }
      });
    }


    function getPaginationLabel(firstRowIndex, endRowIndex, totalRowsNumber) {
      return firstRowIndex.toString() + "-" + endRowIndex.toString() + " / " + totalRowsNumber.toString()
    }

    onMounted(() => {
      queryParam.value = route.query.q
      setLocation(queryParam.value)
      updateCredentials()
    })


    async function updateCredentials() {
      credentialStore.getCredentialsByLocation(model.value).then((credentials) => {
        rows.value = credentials
      })
    }


    return {
      columns,
      rows,
      filter,
      locationNames,
      locationStore,
      credentialStore,
      model,
      t,
      updateCredentials,
      queryParam,
      getPaginationLabel
    }
  }
}
</script>

<style scoped>

</style>