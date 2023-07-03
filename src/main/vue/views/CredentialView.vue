<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{ t("credentialview.credentialview") }}</p>
    <div class="row self-center">
      <q-splitter
          v-model="splitterModel"
          style="height: 90%"
          :limits="[5, Infinity]"
          horizontal
      >
        <template v-slot:before style="max-width: 20px">
          <q-tabs
              v-model="tab"
              vertical
              class="bg-white text-primary"
          >
            <div class="row">
            <q-tab name="credentials" label="Credentials" />
            <q-tab name="all" label="Alle Credentials" />
            </div>
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
              <q-tab-panel name="credentials">
      <q-table
          style="width: 80vw; height: 50vh"
          :rows-per-page-options="[0]"
          :rows="rows"
          :columns="columns"
          :title="t('credentialview.credentialview')"
          :separator="'cell'"
          :no-data-label="t('common.noData')"
          :no-results-label="t('common.noResults')"
          :pagination-label="getPaginationLabel"
          :filter="filter"
          row-key="name"
          visible-columns="['category', 'availableCredential', 'qualification', 'issuer']">
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
              style="min-width: 12em; padding-right: 2em"
              @update:model-value="updateCredentials"/>
          <q-input dense borderless debounce="250" v-model="filter" :placeholder="t('credentialview.search')">
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
                  {{ t("credentialview.room") }} {{ props.row.room[index] }}
                </q-tooltip>
              </q-icon>
            </div>
          </q-td>
        </template>
      </q-table>
              </q-tab-panel>
            <q-tab-panel name="all">
              <q-table
                  style="width: 80vw; height: 50vh"
                  :rows-per-page-options="[0]"
                  :rows="rows2"
                  :columns="columns"
                  :title="t('credentialview.credentialview')"
                  :separator="'cell'"
                  :no-data-label="t('common.noData')"
                  :no-results-label="t('common.noResults')"
                  :pagination-label="getPaginationLabel"
                  :filter="filter2"
                  row-key="name"
                  visible-columns="['category', 'availableCredential', 'qualification', 'issuer']">
                <template v-slot:top-right>
                  <q-input dense borderless debounce="250" v-model="filter" :placeholder="t('credentialview.search')">
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
                          {{ t("credentialview.room") }} {{ props.row.room[index] }}
                        </q-tooltip>
                      </q-icon>
                    </div>
                  </q-td>
                </template>
              </q-table>
            </q-tab-panel>
            </q-tab-panels>
        </template>
      </q-splitter>
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
    const route = useRoute()
    const queryParam = ref('')


    const filter = ref('')
    const columns = [
      {
        name: 'category',
        required: true,
        label: t('credentialmapping.category'),
        align: 'center',
        field: row => row.categoryName,
        format: val => `${val}`,
        sortable: true
      },
      {
        name: 'availableCredential',
        align: 'center',
        label: t('credentialview.availablecredentials'),
        field: row => row.credentialName,
        sortable: true
      },
      {
        name: 'qualification',
        align: 'center',
        label: t('credentialview.qualification'),
        field: row => row.externalCredential,
        format: (val) => val.join(', '),
        sortable: true
      },
      {
        name: 'issuer',
        align: 'center',
        label: t('credentialview.issuer'),
        field: row => row.issuerName,
        format: (val) => val.join(', '),
        sortable: true
      },
      {name: 'room', align: 'center', label: t('credentialview.room'), sortable: true}
    ]

    let rows = ref([])

    const rows2 = ref([])

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
      getAllCredentials()
    })


    async function updateCredentials() {
      credentialStore.getCredentialsByLocation(model.value).then((credentials) => {
        rows.value = credentials
      })
    }

    function getAllCredentials() {
      credentialStore.getCredentialsForView().then((credentials) => {
        rows2.value = credentials
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
      rows2,
      filter2:ref(''),
      tab: ref('credentials'),
      splitterModel: ref(20),
      updateCredentials,
      queryParam,
      getAllCredentials,
      getPaginationLabel
    }
  }
}
</script>

<style scoped>

</style>