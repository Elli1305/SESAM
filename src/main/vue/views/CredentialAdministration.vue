<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{ t('admin.credentialAdministration.title') }}</p>
    <div class="row self-center">
      <q-table
          :columns="columns"
          :filter="search"
          :rows="rows"
          :rows-per-page-options="[0]"
          :separator="'cell'"
          row-key="name"
          style="width: 80vw; height: 50vh; background-color: var(--bg-color); color: var(--text-color)">
        <template v-slot:top-left>
          <q-select v-model="selectedTypes" :label="t('admin.credentialAdministration.type')" :options="options" borderless dense emit-value
                    map-options multiple style="width: 12em"/>
        </template>
        <template v-slot:top-right>
          <q-btn-dropdown  color="grey-6" label="Import / Export Credentials" rounded flat icon="download">
            <q-list>
              <q-item v-close-popup clickable @click="importExport">
                <q-item-section>
                  <q-item-label>Alle Credentials exportieren</q-item-label>
                </q-item-section>
              </q-item>
              <q-item v-close-popup clickable to="/import_credentials">
                <q-item-section>
                  <q-item-label>Credentials importieren</q-item-label>
                </q-item-section>
              </q-item>
            </q-list>
          </q-btn-dropdown>
          <q-btn :label="t('admin.credentialAdministration.new')" color="grey-6" flat icon="add" rounded
                 to="/add_credential"/>
          <q-input v-model="search" :placeholder="t('common.search')" class="q-ml-lg" debounce="300" dense outlined
                   rounded>
            <template v-slot:append>
              <q-icon name="search"/>
            </template>
          </q-input>
        </template>
        <template v-slot:body-cell-name="props">
          <q-td :props="props">
            {{ props.value }}
          </q-td>
        </template>
        <template v-slot:body-cell-type="props">
          <q-td :props="props">
            <q-chip v-if="props.value" color="primary" style="font-size: 1em" text-color="secondary">
              <q-icon left name="business"/>
              Internal
            </q-chip>
            <q-chip v-else color="secondary" style="font-size: 1em" text-color="primary">
              <q-icon left name="public"/>
              External
            </q-chip>
          </q-td>
        </template>
        <template v-slot:body-cell-actions="props">
          <q-td :props="props">
            <q-btn :to="`/credential_administration/${'issuer' in props.row ? 'internal' : 'external'}/${props.row.id}`"
                   dense flat icon="edit" round style="color: var(--light)"></q-btn>
          </q-td>
        </template>
      </q-table>
    </div>
  </q-page>
</template>

<script lang="ts" setup>
import {exportFile, QSelectOption, QTableColumn, useQuasar} from "quasar";
import {useCredentialsStore} from "@/main/vue/stores/credential";
import {computed, Ref, ref} from "vue";
import {ExternalCredential, InternalCredential} from "@/main/vue/entity/credentialDefinition";
import {useI18n} from "vue-i18n";
import api from "@/main/vue/api";

const {t} = useI18n();
const $q = useQuasar();

const columns: QTableColumn<InternalCredential | ExternalCredential>[] = [
  {
    name: 'name',
    required: true,
    label: 'Name',
    align: 'left',
    field: 'name',
    sortable: true,
  },
  {
    name: 'credentialDefinitionId',
    required: true,
    label: 'Credential Definition ID',
    align: 'left',
    field: 'credentialDefinitionId',
    sortable: true,
  },
  {
    name: 'agent',
    required: false,
    label: 'Agent',
    align: 'left',
    field: (row) => "agent" in row ? row.agent : null,
    format: (val) => val ?? '–',
    sortable: true,
  },
  {
    name: 'type',
    required: true,
    label: 'Typ',
    align: 'center',
    field: (row) => "issuer" in row,
    sortable: true,
  },
  {
    name: 'actions',
    align: 'center',
    label: t('common.edit'),
    field: row => ''
  }

];

const options: QSelectOption[] = [
  {
    label: 'Internal',
    value: 'internal'
  },
  {
    label: 'External',
    value: 'external'
  },
];

const selectedTypes: Ref<string[]> = ref(['internal', 'external']);

const search = ref('');
const store = useCredentialsStore();

store.fetch();

const rows = computed(
    () => [...store.credentials, ...store.externalCredentials]
        .filter((value) => (selectedTypes.value.includes('internal') && 'issuer' in value) || (selectedTypes.value.includes('external') && !('issuer' in value)))
);

const importExport = async () => {
  const credentials = await api.credential.exportCredentials();

  const error = exportFile(
      `export-${new Date().getTime()}.json`,
      JSON.stringify(credentials.data),
      {
        mimeType: 'application/json'
      }
  )

  if (error !== true) {
    console.error(error);
  }
}

</script>

<style scoped></style>