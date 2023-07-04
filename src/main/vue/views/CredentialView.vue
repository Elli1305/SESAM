<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{ t("credentialview.credentialview") }}</p>
      <q-splitter
          class="row self-center"
          v-model="splitterModel"
          style="width: 80vw; height: 50vh"
          disable
          :limits="[5, Infinity]"
      >
        <template v-slot:before style="width: fit-content">
          <q-tabs
              v-model="tab"
              vertical
              class="bg-white text-primary"
              style="max-width: 10vw"
          >
            <q-tab name="all" :label="t('credentialview.all')"/>
            <q-tab name="internal" :label="t('credentialview.internal')"/>
            <q-tab name="external" :label="t('credentialview.external')"/>
          </q-tabs>
        </template>

        <template v-slot:after>
          <q-tab-panels
              v-model="tab"
              animated
              swipeable
              vertical
              transition-prev="jump-up"
              transition-next="jump-up"
              color="primary"
          >
            <q-tab-panel name="internal">
              <q-table
                  style="height: 45vh"
                  :rows-per-page-options="[0]"
                  :rows="rows"
                  :columns="columns"
                  :title="t('credentialview.intern')"
                  :separator="'cell'"
                  :no-data-label="t('common.noData')"
                  :no-results-label="t('common.noResults')"
                  :pagination-label="getPaginationLabel"
                  :filter="filter"
                  row-key="name"
                  visible-columns="['category', 'availableCredential', 'qualification', 'issuer']">
                <template v-slot:top-right>
                  <q-toggle v-model="value" left-label :label="t('credentialview.showcredentials')" @update:model-value="internalCredential(value)"/>
                  <div v-if="!value">
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
                      style="min-width: 12em; padding-right: 2em"
                      @update:model-value="updateCredentials"/>
                  </div>
                  <q-input dense borderless debounce="250" v-model="filter" :placeholder="t('credentialview.search')">
                    <template v-slot:append>
                      <q-icon name="search"/>
                    </template>
                  </q-input>
                </template>
                <template v-slot:body-cell-issuer="props">
                  <q-td style="" class="column no-wrap" :props="props">
                    <div class="row q-my-xs justify-between items-center no-wrap"
                         v-for="(elem, index) in props.row.issuerName">
                      <p class="no-margin" style="line-height: 1">{{ props.row.issuerName[index] }}</p>
                      <q-icon class="q-ml-md" color="info" size="1em" name="info_outlined">
                        <q-tooltip class="bg-grey-8" anchor="top left" self="bottom left" :offset="[0, 8]">
                          {{ t("credentialview.room") }} {{ props.row.room[index] }}
                        </q-tooltip>
                      </q-icon>
                    </div>
                  </q-td>
                </template>
              </q-table>
            </q-tab-panel>
            <q-tab-panel name="external">
              <q-table
                  style="height: 45vh"
                  :rows-per-page-options="[0]"
                  :rows="rows2"
                  :columns="columns2"
                  :title="t('credentialview.extern')"
                  :separator="'cell'"
                  :no-data-label="t('common.noData')"
                  :no-results-label="t('common.noResults')"
                  :pagination-label="getPaginationLabel"
                  :filter="filter2"
                  row-key="name">
                <template v-slot:top-right>
                  <q-toggle v-model="value2" left-label :label="t('credentialview.showcredentials')" @update:model-value="externalCredential(value2)"/>
                  <div v-if="!value2">
                    <q-select
                        :label="t('credentialview.location')"
                        behavior="menu"
                        v-model="model2"
                        borderless
                        dense
                        options-dense
                        emit-value
                        map-options
                        :options="locationStore.allLocations"
                        option-value="id"
                        option-label="name"
                        style="min-width: 12em; padding-right: 2em"
                        @update:model-value="updateExternalCredentials"/>
                  </div>
                  <q-input dense borderless debounce="250" v-model="filter2" :placeholder="t('credentialview.search')">
                    <template v-slot:append>
                      <q-icon name="search"/>
                    </template>
                  </q-input>
                </template>
              </q-table>
            </q-tab-panel>
            <q-tab-panel name="all">
              <q-table
                  style="height: 45vh"
                  :rows-per-page-options="[0]"
                  :rows="rows3"
                  :columns="columns"
                  :title="t('credentialview.extern')"
                  :separator="'cell'"
                  :no-data-label="t('common.noData')"
                  :no-results-label="t('common.noResults')"
                  :pagination-label="getPaginationLabel"
                  :filter="filter3"
                  row-key="name">
                <template v-slot:top-right>
                  <q-toggle v-model="value3" left-label :label="t('credentialview.showcredentials')" @update:model-value="all(value3)"/>
                  <div v-if="!value3">
                    <q-select
                        :label="t('credentialview.location')"
                        behavior="menu"
                        v-model="model3"
                        borderless
                        dense
                        options-dense
                        emit-value
                        map-options
                        :options="locationStore.allLocations"
                        option-value="id"
                        option-label="name"
                        style="min-width: 12em; padding-right: 2em"
                        @update:model-value="updateAll"/>
                  </div>
                  <q-input dense borderless debounce="250" v-model="filter3" :placeholder="t('credentialview.search')">
                    <template v-slot:append>
                      <q-icon name="search"/>
                    </template>
                  </q-input>
                </template>
              </q-table>
            </q-tab-panel>
          </q-tab-panels>
        </template>
      </q-splitter>

  </q-page>
</template>


<script>
import {ref, onMounted} from 'vue'
import {useRoute} from 'vue-router'

import {useI18n} from "vue-i18n";
import {useCredentialStore} from "@/main/vue/stores/credential";
import {useLocationStore} from "@/main/vue/stores/locations";
import credential from "@/main/vue/api/credential";

export default {
  setup() {

    let locationNames = ref([])
    const {t} = useI18n();
    const credentialStore = useCredentialStore()
    const locationStore = useLocationStore()
    const model = ref(1)
    const model2 = ref(1)
    const model3 = ref(1)
    const route = useRoute()
    const queryParam = ref('')


    const filter = ref('')
    const columns = [
      { name: 'availableCredential', align: 'center', label: t('credentialview.availablecredentials'), field: row => row.credentialName, sortable: true},
      { name: 'category', required: true, label: t('credentialmapping.category'), align: 'center', field: row => row.categoryName, format: val => `${val}`, sortable: true },
      { name: 'qualification', align: 'center', label: t('credentialview.extern'), field: row => row.externalCredential, format: (val) => val.join(', '), sortable: true },
      { name: 'issuer', align: 'center', label: t('credentialview.issuer'), field: row => row.issuerName, format: (val) => val.join(', '), sortable: true },
      { name: 'room', align: 'center', label: t('credentialview.room'), sortable: true}
    ]
    const columns2 = [
      { name: 'availableCredential', align: 'center', label: t('credentialview.availablecredentials'), field: row => row.credentialName, sortable: true},
      { name: 'category', required: true, label: t('credentialmapping.category'), align: 'center', field: row => row.categoryName, format: val => `${val}`, sortable: true },
      { name: 'qualification', align: 'center', label: t('credentialview.intern'), field: row => row.internalCredential, format: (val) => val.join(', '), sortable: true },
    ]

    let rows = ref([])

    const rows2 = ref([])
    const rows3 = ref([])

    function setLocation(id) {
      locationStore.getLocations().then((locations) => {
        model.value = locations[0].id;
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
      internalCredential(true)
      queryParam.value = route.query.q
      setLocation(queryParam.value)
      externalCredential(true)
    })

    function internalCredential(value) {
      if (value) {
        getAllCredentials()
      } else {
        updateCredentials()
      }
    }

    async function updateCredentials() {
      credentialStore.getCredentialsByLocation(model.value).then((credentials) => {
        rows.value = credentials
      })
    }

    function getAllCredentials() {
      credentialStore.getCredentialsForView().then((credentials) => {
        rows.value = credentials
      })
    }

    function getAllExternalCredentials() {
      credentialStore.getExternalsForView().then((credentials) => {
        rows2.value = credentials
      })
    }

    function updateExternalCredentials() {
      credentialStore.getCredentialsByLocation(model2.value).then((credentials) => {
        rows2.value = credentials
      })
    }

    function externalCredential(value) {
      if (value) {
        getAllExternalCredentials()
      } else {
        updateExternalCredentials()
      }
    }

    function getAll() {
      credentialStore.getExternalsForView().then((credentials) => {
        rows3.value = credentials
      })
    }

    function updateAll() {
      credentialStore.getCredentialsByLocation(model3.value).then((credentials) => {
        rows3.value = credentials
      })
    }

    function all(value) {
      if (value) {
        getAll()
      } else {
        updateExternalCredentials()
      }
    }


    return {
      columns,
      columns2,
      rows,
      rows2,
      filter,
      filter2: ref(''),
      filter3: ref(''),
      locationNames,
      locationStore,
      credentialStore,
      model,
      model2,
      model3,
      t,
      tab: ref('credentials'),
      splitterModel: ref(20),
      updateCredentials,
      queryParam,
      getAllCredentials,
      getPaginationLabel,
      value: ref(true),
      value2: ref(true),
      value3: ref(true),
      internalCredential,
      externalCredential,
      updateExternalCredentials,
      getAllExternalCredentials,
      all,
      getAll,
      updateAll,
    }
  }
}
</script>

<style scoped>

</style>