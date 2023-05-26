<template>
  <q-page class="items-center justify-center" style="display: flex">
    <div class="q-gutter-y-md column" style="max-width: 80%; min-width: 20em; display: flex">
      <div>
      <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">Credentialmapping  {{credentialStore.categories}}</h1>
      </div>
      <div style="width: 100em">
      <q-table
          flat bordered
          title="Credentialmapping"
          :rows="rows"
          :columns="columns"
          row-key="name"
          :separator="'cell'"
          :filter="filter"
      >
        <template v-slot:top-right>
          <div style="padding-right: 1em">
          <q-btn dense flat color="grey" label="New category"  :disable="loading" icon="add" rounded @click="prompt=true"/>
          </div>
          <q-input v-model="filter" placeholder="Search" dense>
            <template v-slot:append>
              <q-icon name="search" />
            </template>
          </q-input>
        </template>
        <template v-slot:body-cell-actions="props">
          <q-td :props="props">
            <q-btn dense round flat color="grey" @click="changeCategory = true" icon="edit"></q-btn>
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
        <div class="text-h6">Löschen der Kategorie</div>
      </q-card-section>

      <q-card-section class="q-pt-none">
        Möchten Sie wirklich die Kategorie löschen?
      </q-card-section>

      <q-card-actions align="evenly">
        <q-btn flat icon="cancel" color="primary" v-close-popup />
        <q-btn flat icon="check" color="primary" v-close-popup />
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-dialog v-model="prompt" persistent>
    <q-card style="max-width: 80%; padding-bottom: 2em">
      <q-card-section>
        <div class="text-h6">Neue Kategorie erstellen</div>
      </q-card-section>

      <q-card-section class="q-pt-none">
        <q-input dense label="Name der Kategorie" v-model="address" autofocus @keyup.enter="prompt = false" placeholder="Kategorie">
          <template v-slot:append>
            <q-icon name="edit" />
          </template>
        </q-input>
      </q-card-section>
      <q-card-section class="q-pt-none" style="padding-bottom: 10em ">
      <div style="float: right; width: 20em; padding-top: 2em ; padding-left: 2em">
        <q-select
            filled
            v-model="model"
            multiple
            label="Interne Credentials"
            emit-value
            :options="options"
        ></q-select>
      </div>
      <div style="float: left; width: 20em; padding-top: 2em">
        <q-select
            filled
            v-model="model2"
            multiple
            label="Externe Credentials"
            emit-value
            :options="options2"
        ></q-select>
        <q-btn dense flat color="grey" label="Delete External Credential" :disable="loading" icon="delete" rounded @click="alertExternal = true" no-caps/>
        <q-btn dense flat color="grey" label="Add External Credential" :disable="loading" icon="add" rounded @click="addExternal=true" no-caps/>
      </div>
      </q-card-section>
      <q-card-actions align="right" class="text-primary">
        <q-btn flat icon="cancel" v-close-popup />
        <q-btn flat icon="save" v-close-popup />
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-dialog v-model="alertExternal">
    <q-card>
      <q-card-section>
        <div class="text-h6">Löschen von ein Externen Credential</div>
      </q-card-section>

      <q-card-section class="q-pt-none">
        Möchten Sie wirklich ein externes Credential löschen?
        <q-select
            filled
            v-model="model2"
            multiple
            label="Externe Credentials"
            emit-value
            :options="options2"
        ></q-select>
      </q-card-section>

      <q-card-actions align="evenly">
        <q-btn flat icon="cancel" color="primary" v-close-popup />
        <q-btn flat icon="check" color="primary" v-close-popup />
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-dialog v-model="addExternal">
    <q-card>
      <q-card-section>
        <div class="text-h6">Hinzufügen eines externen Credentials</div>
      </q-card-section>

      <q-card-section class="q-pt-none">
        <q-card-section class="q-pt-none">
          <q-input dense label="Name des externen Credentials" v-model="address" autofocus @keyup.enter="prompt = false" />
          <q-input dense label="CredentialDefintionId" v-model="address" autofocus @keyup.enter="prompt = false" style="padding-top: 2em" />
        </q-card-section>
      </q-card-section>
      <q-card-actions align="evenly">
        <q-btn flat icon="cancel" color="primary" v-close-popup />
        <q-btn flat icon="save" color="primary" v-close-popup />
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-dialog v-model="changeCategory" persistent>
    <q-card style="max-width: 80%; padding-bottom: 2em">
      <q-card-section>
        <div class="text-h6">Kategorie ändern</div>
      </q-card-section>
      <q-card-section class="q-pt-none">
        <q-input dense label="Name der Kategorie" v-model="address" autofocus @keyup.enter="prompt = false" placeholder="Kategorie">
        <template v-slot:append>
          <q-icon name="edit" />
        </template>
        </q-input>
      </q-card-section>
      <q-card-section class="q-pt-none" style="padding-bottom: 10em ">
        <div style="float: right; width: 20em; padding-top: 2em ; padding-left: 2em">
          <q-select
              filled
              v-model="model"
              multiple
              label="Interne Credentials"
              emit-value
              :options="options"
          ></q-select>
        </div>
        <div style="float: left; width: 20em; padding-top: 2em">
          <q-select
              filled
              v-model="model2"
              multiple
              label="Externe Credentials"
              emit-value
              :options="options2"
          ></q-select>
          <q-btn dense flat color="grey" label="Delete External Credential" :disable="loading" icon="delete" rounded @click="alertExternal = true" no-caps/>
          <q-btn dense flat color="grey" label="Add External Credential" :disable="loading" icon="add" rounded @click="addExternal=true" no-caps/>
        </div>
      </q-card-section>
      <q-card-actions align="right" class="text-primary">
        <q-btn flat icon="cancel" v-close-popup />
        <q-btn flat icon="save" v-close-popup />
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
  { name: 'category', required: true, label: 'Kategorie', align: 'center', field: row => row.nameCategory, sortable: true },
  { name: 'credential', align: 'center', label: "Credential (intern)", field: row => row.internalCredentials,  format: (val, row) => val.join(', '), sortable: true},
  { name: 'externalCredential', align: 'center', label: 'Credential (extern)', field: row => row.externalCredentialsCmd, format: (val, row) => val.join(', '), sortable: true},
  { name: 'actions', align: 'center', label: 'Bearbeiten'}
]

const rows = ref([]);

rows.value = []

export default {
  name: "CredentialMappingList",
  setup() {
    const credentialStore = useCredentialStore()

    credentialStore.getCategory().then((categories) => {
        rows.value = categories
    })


    return {
      credentialStore,
      rows,
      columns,
      alert: ref(false),
      prompt: ref(false),
      filter: ref(''),
      alertExternal: ref(false),
      addExternal:ref(false),
      changeCategory:ref(false),
      options: [
        'DRLG', 'Johanniter'
      ],
      options2:['Telekom', 'DRK'],
    }
  }
}

</script>

<style scoped>

</style>