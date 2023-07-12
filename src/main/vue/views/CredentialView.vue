<template>
  <q-page class="column justify-evenly items-center" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{ t("credentialView.title") }}</p>
    <div class="column">
      <q-tabs
          v-model="tab"
          dense
          class="text-grey"
          active-color="primary"
          indicator-color="primary"
          align="justify">
        <q-tab name="all" :label="t('credentialView.all')"/>
        <q-tab name="internal" :label="t('credentialView.intern')"/>
        <q-tab name="external" :label="t('credentialView.extern')"/>
      </q-tabs>

      <q-separator/>

      <q-tab-panels
          style="width: 80vw; height: 50vh; background-color: var(--bg-color); color: var(--text-color)"
          v-model="tab"
          animated
          swipeable
          color="primary">
        <q-tab-panel name="all">
          <q-table
              class="full-width full-height"
              style="background-color: var(--bg-color); color: var(--text-color)"
              :rows-per-page-options="[0]"
              :rows="rows3"
              :columns="columns3"
              :separator="'cell'"
              :no-data-label="t('common.noData')"
              :no-results-label="t('common.noResults')"
              :pagination-label="getPaginationLabel"
              :filter="filter3"
              row-key="name"
              visible-columns="['category', 'type', 'availableCredential', 'qualification', 'issuer']">
            <template v-slot:top-left>
              <q-toggle v-model="value3" left-label :label="t('credentialView.showCredentials')"
                        @update:model-value="all(value3)" size="2.5em"/>
              <q-select
                  class="q-ml-md"
                  v-if="!value3"
                  :label="t('credentialView.location')"
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
            </template>
            <template v-slot:top-right>
              <q-input dense outlined rounded debounce="250" v-model="filter3" :placeholder="t('common.search')">
                <template v-slot:append>
                  <q-icon name="search"/>
                </template>
              </q-input>
            </template>
            <template v-slot:body-cell-issuer="props">
              <q-td class="row justify-evenly no-wrap" :props="props">
                <div class="row q-my-xs justify-between items-center no-wrap"
                     v-for="(elem, index) in props.row.issuerName">
                  <p class="no-margin" style="line-height: 1">{{ props.row.issuerName[index] }}</p>
                  <q-icon class="q-ml-sm q-mr-md" color="info" size="1em" name="info_outlined">
                    <q-tooltip class="bg-grey-8" anchor="top left" self="bottom left" :offset="[0, 8]">
                      {{ t("credentialView.room") }} {{ props.row.room[index] }}
                    </q-tooltip>
                  </q-icon>
                </div>
              </q-td>
            </template>
          </q-table>
        </q-tab-panel>
        <q-tab-panel name="internal">
          <q-table
              class="full-width full-height"
              style="background-color: var(--bg-color); color: var(--text-color)"
              :rows-per-page-options="[0]"
              :rows="rows"
              :columns="columns"
              :separator="'cell'"
              :no-data-label="t('common.noData')"
              :no-results-label="t('common.noResults')"
              :pagination-label="getPaginationLabel"
              :filter="filter"
              row-key="name"
              visible-columns="['category', 'availableCredential', 'qualification', 'issuer']">
            <template v-slot:top-left>
              <q-toggle v-model="value" left-label :label="t('credentialView.showCredentials')"
                        @update:model-value="internalCredential(value)" size="2.5em"/>
              <q-select
                  class="q-ml-md"
                  v-if="!value"
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
            </template>
            <template v-slot:top-right>
              <q-input dense outlined rounded debounce="250" v-model="filter" :placeholder="t('common.search')">
                <template v-slot:append>
                  <q-icon name="search"/>
                </template>
              </q-input>
            </template>
            <template v-slot:body-cell-issuer="props">
              <q-td class="row justify-evenly no-wrap" :props="props">
                <div class="row q-my-xs justify-between items-center no-wrap"
                     v-for="(elem, index) in props.row.issuerName">
                  <p class="no-margin" style="line-height: 1">{{ props.row.issuerName[index] }}</p>
                  <q-icon class="q-ml-sm q-mr-md" color="info" size="1em" name="info_outlined">
                    <q-tooltip class="bg-grey-8" anchor="top left" self="bottom left" :offset="[0, 8]">
                      {{ t("credentialView.room") }} {{ props.row.room[index] }}
                    </q-tooltip>
                  </q-icon>
                </div>
              </q-td>
              <br>
            </template>
          </q-table>
        </q-tab-panel>
        <q-tab-panel name="external">
          <q-table
              class="full-width full-height"
              style="background-color: var(--bg-color); color: var(--text-color)"
              :rows-per-page-options="[0]"
              :rows="rows2"
              :columns="columns2"
              :separator="'cell'"
              :no-data-label="t('common.noData')"
              :no-results-label="t('common.noResults')"
              :pagination-label="getPaginationLabel"
              :filter="filter2"
              row-key="name">
            <template v-slot:top-left>
              <q-toggle v-model="value2" left-label :label="t('credentialView.showCredentials')"
                        @update:model-value="externalCredential(value2)" size="2.5em"/>
              <q-select
                  class="q-ml-md"
                  v-if="!value2"
                  :label="t('credentialView.location')"
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
            </template>
            <template v-slot:top-right>
              <q-input dense outlined rounded debounce="250" v-model="filter2" :placeholder="t('common.search')">
                <template v-slot:append>
                  <q-icon name="search"/>
                </template>
              </q-input>
            </template>
          </q-table>
        </q-tab-panel>
      </q-tab-panels>
    </div>

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
      {
        name: 'availableCredential',
        align: 'center',
        label: t('common.credential'),
        field: row => row.credentialName,
        sortable: true
      },
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
        name: 'qualification',
        align: 'center',
        label: t('admin.credentialMapping.external'),
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
        sortable: true,
      },
      {name: 'room', align: 'center', label: t('common.room'), sortable: true}
    ]
    const columns2 = [
      {
        name: 'availableCredential',
        align: 'center',
        label: t('common.credential'),
        field: row => row.credentialName,
        sortable: true
      },
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
        name: 'qualification',
        align: 'center',
        label: t('admin.credentialMapping.internal'),
        field: row => row.internalCredential,
        format: (val) => val.join(', '),
        sortable: true
      },
    ]

    const columns3 = [
      {
        name: 'availableCredential',
        align: 'center',
        label: t('common.credentials'),
        field: row => row.credentialName,
        sortable: true
      },
      {
        name: 'category',
        required: true,
        label: t('credentialView.type'),
        align: 'center',
        field: row => row.type,
        sortable: true
      },
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
        name: 'issuer',
        align: 'center',
        label: t('profile.issuer'),
        field: row => row.issuerName,
        format: (val) => val.join(', '),
        sortable: true
      },
      {name: 'room', align: 'center', label: t('common.room'), sortable: true}
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
      all(true)
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
      credentialStore.getExternalByLocation(model2.value).then((credentials) => {
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
      credentialStore.getAllForView().then((credentials) => {
        rows3.value = credentials
      })
    }

    function updateAll() {
      credentialStore.getAllByLocation(model3.value).then((credentials) => {
        rows3.value = credentials
      })
    }

    function all(value) {
      if (value) {
        getAll()
      } else {
        updateAll()
      }
    }


    return {
      columns,
      columns2,
      columns3,
      rows,
      rows2,
      rows3,
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
      tab: ref('all'),
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