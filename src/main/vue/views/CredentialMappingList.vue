<template>
  <q-page class="items-center justify-center" style="display: flex">
    <div class="q-gutter-y-md column" style="max-width: 80%; min-width: 20em; display: flex">
      <div>
      <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">{{ t("credentialmapping.credentialmapping")}}</h1>
      </div>
      <div style="width: 100em">
      <q-table
          flat bordered
          :title="t('credentialmapping.credentialmapping')"
          :rows="rows"
          :columns="columns"
          row-key="name"
          :separator="'cell'"
          :filter="filter"
          visible-columns="['category', 'credential', 'externalCredential', 'actions']"
          :row-key="row => row.id"
      >
        <template v-slot:top-right>
          <div style="padding-right: 1em">
          <q-btn dense flat color="grey" :label="t('credentialmapping.newcategory')"  :disable="loading" icon="add" rounded @click="prompt=true"/>
          </div>
          <q-input v-model="filter" :placeholder="t('credentialview.search')" dense>
            <template v-slot:append>
              <q-icon name="search" />
            </template>
          </q-input>
        </template>
        <template v-slot:body-cell-actions="props">
          <q-td :props="props">
            <q-btn dense round flat color="grey" @click="changeCategory = true; openForm(props.row)" icon="edit"></q-btn>
            <q-btn dense round flat color="grey" icon="delete" :disable="loading" @click="alert = true" />
          </q-td>
        </template>
      </q-table>
    </div>
    </div>
  </q-page>
  <q-dialog v-model="alert">
    <q-card>
      <q-card-section>
        <div class="text-h6"> {{ t("credentialmapping.categorydelete")}}</div>
      </q-card-section>

      <q-card-section class="q-pt-none">
        {{ t("credentialmapping.delete")}}
      </q-card-section>

      <q-card-actions align="evenly" class="text-primary">
        <q-btn flat v-close-popup>  {{ t("credentialmapping.cancel")}}</q-btn>
        <q-btn flat v-close-popup @click="deleteCategory(editedRow.id)">  {{ t("credentialmapping.save")}} </q-btn>
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-dialog v-model="prompt" persistent>
    <q-card style="max-width: 80%; padding-bottom: 2em">
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
      <q-card-section class="q-pt-none" style="padding-bottom: 10em ">
      <div style="float: left; width: 20em; padding-top: 2em">
        <q-select
            filled
            v-model="model"
            multiple
            :label="t('credentialmapping.internal')"
            emit-value
            :options="credentialStore.allCredentials"
            option-value="id"
            option-label="name"
            options-cover
            map-options
        ></q-select>
      </div>
      <div style="float: right; width: 20em; padding-top: 2em; padding-left: 2em">
        <q-select
            filled
            v-model="model2"
            multiple
            :label="t('credentialmapping.external')"
            emit-value
            :options="credentialStore.external"
            option-value="id"
            option-label="name"
            options-cover
            map-options
        ></q-select>
      </div>
      </q-card-section>
      <q-card-actions align="right" class="text-primary">
        <q-btn flat v-close-popup> {{ t("credentialmapping.cancel")}}</q-btn>
        <q-btn flat v-close-popup @click="createCategory"> {{ t("credentialmapping.save")}}</q-btn>
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-dialog v-model="changeCategory" persistent>
    <q-card style="max-width: 80%; padding-bottom: 2em">
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
      <q-card-section class="q-pt-none" style="padding-bottom: 10em ">
        <div style="float: left; width: 20em; padding-top: 2em ">
          <q-select
              filled
              multiple
              :label="t('credentialmapping.internal')"
              emit-value
              v-model="model3"
              :options="credentialStore.allCredentials"
              option-value="id"
              option-label="name"
              options-cover
              map-options
          ></q-select>
        </div>
        <div style="float: right; width: 20em; padding-top: 2em; padding-left: 2em">
          <q-select
              filled
              multiple
              :label="t('credentialmapping.external')"
              emit-value
              v-model="model4"
              :options="credentialStore.external"
              option-value="id"
              option-label="name"
              options-cover
              map-options
          ></q-select>
        </div>
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
import {useRouter} from 'vue-router'

const columns = [
  { name: 'category', required: true, label: 'Kategorie', align: 'center', field: row => row.name, sortable: true },
  { name: 'credential', align: 'center', label: "Credential (intern)", field: row => row.credentials,  format: (val, row) => val.map(c => c.name).join(', '), sortable: true},
  { name: 'externalCredential', align: 'center', label: 'Credential (extern)', field: row => row.externalCredentials, format: (val, row) => val.map(e => e.name).join(', '), sortable: true},
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

    credentialStore.getExternalCredentials().then((external) => {
    })

    credentialStore.getCredentials().then((external) => {})

    function deleteCategory(id) {
      credentialStore.deleteCategory(id)
      this.timeout = setTimeout(() =>
          credentialStore.getCategory().then((categories) => {
            rows.value = categories
          }), 250)
    }

    function reload () {

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
    }
  }
}

</script>

<style scoped>

</style>