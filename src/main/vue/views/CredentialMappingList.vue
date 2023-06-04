<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{t("credentialmapping.credentialmapping")}}</p>
    <div class="row self-center">
      <q-table
          style="width: 75vw; height: 25em"
          :rows-per-page-options="[0]"
          :rows="rows"
          :columns="columns"
          :title="t('credentialmapping.credentialmapping')"
          :separator="'cell'"
          :no-data-label="t('common.noData')"
          :no-results-label="t('common.noResults')"
          :pagination-label="getPaginationLabel"
          :filter="filter"
          row-key="name"
          visible-columns="['category', 'credential', 'externalCredential', 'actions']"
          :row-key="row => row.id">
        <template v-slot:top-right>
          <q-btn class="q-mr-xs" flat color="grey" :label="t('credentialmapping.newcategory')" icon="add" rounded @click="prompt=true"/>
          <q-input class="q-ml-xs" borderless dense debounce="250" v-model="filter" :placeholder="t('credentialview.search')">
            <template v-slot:append>
              <q-icon name="search" />
            </template>
          </q-input>
        </template>
        <template v-slot:body-cell-actions="props">
          <q-td :props="props">
            <q-btn dense round flat color="grey" @click="changeCategory = true; openForm(props.row)" icon="edit"></q-btn>
            <q-btn dense round flat color="grey" icon="delete" @click="alert = true; openForm(props.row)" />
          </q-td>
        </template>
      </q-table>
    </div>
  </q-page>
  <q-dialog v-model="alert" persistent>
    <q-card>
      <q-card-section>
        <div class="text-h6"> {{ t("credentialmapping.categorydelete")}}</div>
      </q-card-section>
      <q-card-section class="row items-center">
        <span class="q-mx-sm">{{ t("credentialmapping.delete")}}</span>
      </q-card-section>

      <q-card-actions align="right" class="text-primary">
        <q-btn flat v-close-popup>  {{ t("credentialmapping.cancel")}}</q-btn>
        <q-btn flat v-close-popup @click="deleteCategory(editedRow.id)">  {{ t("credentialmapping.save")}} </q-btn>
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-dialog v-model="prompt" persistent>
    <q-card>
      <q-card-section>
        <div class="text-h6"> {{ t("credentialmapping.categorycreate")}}</div>
      </q-card-section>

      <q-card-section class="q-pt-none">
        <q-input dense :label="t('credentialmapping.name')" v-model="address" autofocus @keyup.enter="prompt = false" :placeholder="t('credentialmapping.category')">
          <template v-slot:append>
            <q-icon name="edit" />
          </template>
        </q-input>
      </q-card-section>
      <q-card-section class="column q-py-none no-wrap">
        <q-select
            class="q-my-sm"
            filled
            v-model="model"
            multiple
            :label="t('credentialmapping.internal')"
            emit-value
            :options="credentialStore.allCredentials"
            option-value="id"
            option-label="name"
            options-cover
            map-options/>
        <q-select
            class="q-my-sm"
            filled
            v-model="model2"
            multiple
            :label="t('credentialmapping.external')"
            emit-value
            :options="credentialStore.external"
            option-value="id"
            option-label="name"
            options-cover
            map-options/>
      </q-card-section>
      <q-card-actions align="right" class="text-primary">
        <q-btn flat v-close-popup> {{ t("credentialmapping.cancel")}}</q-btn>
        <q-btn flat v-close-popup @click="createCategory"> {{ t("credentialmapping.save")}}</q-btn>
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-dialog v-model="changeCategory" persistent>
    <q-card>
      <q-card-section>
        <div class="text-h6"> {{ t("credentialmapping.categorychange")}}</div>
      </q-card-section>
      <q-card-section class="q-pt-none">
        <q-input dense :label="t('credentialmapping.name')" v-model="catname" autofocus @keyup.enter="prompt = false" :placeholder="t('credentialmapping.category')">
        <template v-slot:append>
          <q-icon name="edit" />
        </template>
        </q-input>
      </q-card-section>
      <q-card-section class="column q-py-none">
          <q-select
              class="q-my-sm"
              filled
              multiple
              :label="t('credentialmapping.internal')"
              emit-value
              v-model="model3"
              :options="credentialStore.allCredentials"
              option-value="id"
              option-label="name"
              options-cover
              map-options/>
          <q-select
              class="q-my-sm"
              filled
              multiple
              :label="t('credentialmapping.external')"
              emit-value
              v-model="model4"
              :options="credentialStore.external"
              option-value="id"
              option-label="name"
              options-cover
              map-options/>
      </q-card-section>
      <q-card-actions align="right" class="text-primary">
        <q-btn flat v-close-popup> {{ t("credentialmapping.cancel")}}</q-btn>
        <q-btn flat v-close-popup @click="updateCategory(editedRow.id)"> {{ t("credentialmapping.save")}} </q-btn>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
import {ref} from 'vue'
import {useCredentialStore} from "@/main/vue/stores/credential";
import {useI18n} from "vue-i18n";

const columns = [
  { name: 'category', required: true, label: 'Kategorie', align: 'center', field: row => row.name, sortable: true },
  { name: 'credential', align: 'center', label: "Credential (intern)", field: row => row.credentials,  format: (val) => val.map(c => c.name).join(', '), sortable: true},
  { name: 'externalCredential', align: 'center', label: 'Credential (extern)', field: row => row.externalCredentials, format: (val) => val.map(e => e.name).join(', '), sortable: true},
  { name: 'actions', align: 'center', label: 'Bearbeiten'}
]

const rows = ref([]);

rows.value = []

export default {
  name: "CredentialMappingList",
  setup() {
    const model = ref([])
    const model2 = ref([])
    const model3 = ref([])
    const model4 = ref([])
    const address = ref('')
    const catname = ref('')
    const { t } = useI18n()

    const credentialStore = useCredentialStore()

    credentialStore.getCategory().then((categories) => {
      rows.value = categories

    })

    credentialStore.getExternalCredentials().then(() => {
    })

    credentialStore.getCredentials().then(() => {})

    function deleteCategory(id) {
      credentialStore.deleteCategory(id)
      this.timeout = setTimeout(() =>
          credentialStore.getCategory().then((categories) => {
            rows.value = categories
          }), 250)
    }

    function getPaginationLabel(firstRowIndex, endRowIndex, totalRowsNumber) {
      return firstRowIndex.toString() + "-" + endRowIndex.toString() + " / " + totalRowsNumber.toString()
    }

    function createCategory() {
      credentialStore.createCategory(address.value, model.value, model2.value);
      address.value =''
      model.value =[]
      model2.value =[]
      this.timeout = setTimeout(() =>
          credentialStore.getCategory().then((categories) => {
            rows.value = categories
          }), 250)
    }


    function updateCategory(id) {
      credentialStore.updateCredentials(id, catname.value, model3.value, model4.value);
      this.timeout = setTimeout(() =>
          credentialStore.getCategory().then((categories) => {
            rows.value = categories
          }), 250)
    }

    const editedRow = ref({})
    const openForm = (row) => {
      editedRow.value = {...row};
      catname.value = row.name
      model3.value = row.credentials.map(c =>c.id)
      model4.value = row.externalCredentials.map(e =>e.id)
    }

    return {
      catname,
      openForm,
      editedRow,
      credentialStore,
      rows,
      columns,
      model,
      model2,
      model3,
      model4,
      address,
      alert: ref(false),
      prompt: ref(false),
      filter: ref(''),
      changeCategory:ref(false),
      deleteCategory,
      createCategory,
      updateCategory,
      t,
      getPaginationLabel
    }
  }
}

</script>

<style scoped>

</style>