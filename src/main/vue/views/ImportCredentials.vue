<script lang="ts" setup>
import {QForm, QStepper, QTableColumn, useQuasar} from "quasar";
import {Ref, ref} from "vue";
import axios from "axios";
import {CredentialExport, ExternalCredentialExport} from "@/main/vue/entity/credentialDefinition";
import api from "@/main/vue/api";
import router from "@/main/vue/router";
import {useI18n} from "vue-i18n";

const $q = useQuasar();
const {t} = useI18n();

type Row = ExternalCredentialExport & { 'type': string };

const columns: QTableColumn<Row>[] = [
  {
    name: 'name',
    required: true,
    label: 'Name',
    align: 'left',
    field: row => row.name,
    format: val => `${val}`,
    sortable: true
  },
  {name: 'version', label: 'Version', field: 'version', sortable: true},
  {
    name: 'credentialDefinitionId',
    align: 'center',
    label: 'Credential Definition ID',
    field: 'credentialDefinitionId',
    sortable: true
  },
  {
    name: 'type',
    label: t('admin.credentialAdministration.type'),
    align: 'center',
    field: (row) => row.type,
    sortable: true,
  },
]

const rows: Ref<Row[]> = ref([]);

const selected: Ref<Row[]> = ref([]);
const host: Ref<string | null> = ref(null);
const file: Ref<File | null> = ref(null);
const step = ref(1);
const form: Ref<QForm | null> = ref(null);

const next = async (refs: any) => {
  const stepper = (refs.stepper as QStepper);

  if (step.value === 1) {
    if (!form.value || !(await form.value.validate())) {
      return;
    }

    if (host.value !== null && host.value.length > 0) {
      try {
        const url = new URL("/api/export_credentials", host.value);

        const response = await axios.get<CredentialExport>(url.toString());

        rows.value = [
          ...response.data.internalCredentials.map(i => ({...i, type: 'internal'})),
          ...response.data.externalCredentials.map(i => ({...i, type: 'external'}))
        ];

        stepper.next();
      } catch (e) {
        if (e instanceof TypeError) {
          $q.notify({
            type: 'negative',
            position: 'bottom',
            timeout: 6000,
            message: t('admin.importCredentials.invalidURL'),
            color: 'negative',
            textColor: 'positive',
          })

          return;
        }

        $q.notify({
          type: 'negative',
          position: 'bottom',
          timeout: 6000,
          message: t('admin.importCredentials.unknownError'),
          color: 'negative',
          textColor: 'positive',
        })
      }
    } else if (file.value !== null) {
      try {
        const credentialExport: CredentialExport = JSON.parse(await file.value.text());

        rows.value = [
          ...credentialExport.internalCredentials.map(i => ({...i, type: 'internal'})),
          ...credentialExport.externalCredentials.map(i => ({...i, type: 'external'}))
        ];

        stepper.next();
      } catch (e) {
        $q.notify({
          type: 'negative',
          position: 'bottom',
          timeout: 6000,
          message: t('admin.importCredentials.unableToOpenFile'),
          color: 'negative',
          textColor: 'positive',
        })
      }
    }
  } else if (step.value === 2) {
    if (selected.value.length <= 0) {
      $q.notify({
        type: 'negative',
        position: 'bottom',
        timeout: 6000,
        message: t('admin.importCredentials.noCredentialsSelected'),
        color: 'negative',
        textColor: 'positive',
      });

      return;
    }

    try {
      await api.credential.importCredentials(
          {
            internalCredentials: selected.value.filter(value => value.type === 'internal') as any,
            externalCredentials: selected.value.filter(value => value.type === 'external')
          }
      );
    } catch (e) {
      $q.notify({
        type: 'negative',
        position: 'bottom',
        timeout: 6000,
        message: t('admin.importCredentials.conflict'),
        color: 'negative',
        textColor: 'positive',
      });

      return;
    }

    await router.push('/credential_administration');
  }
}
</script>

<template>
  <q-page class="column justify-evenly items-center" style="padding: 2em 5em">
    <q-stepper
        ref="stepper"
        color="primary"
        v-model="step"
        animated
        style="width: 80vw; background-color: var(--bg-color); color: var(--text-color)"
        flat>
      <q-step
          :done="step > 1"
          :name="1"
          icon="file_import"
          :title="t('admin.importCredentials.selectSource')">
        <div class="row justify-evenly" style="height: 45vh">
          <div class="column q-pa-lg" style="width: 40%; font-size: 1.5em">
            <p>{{ t('admin.importCredentials.steps.step1') }}</p>

            <p>{{ t('admin.importCredentials.steps.step2') }}</p>

            <p>{{ t('admin.importCredentials.steps.step3') }}</p>
          </div>
          <q-form ref="form" class="column justify-evenly" @submit.prevent style="width: 40%">
            <q-input v-model="host" :disable="file != null" class="q-mb-md" type="url"
                     :label="t('admin.importCredentials.instanceURL')"/>
            <q-file v-model="file" :disable="host != null && host.length > 0" accept="application/json"
                    :label="t('admin.importCredentials.uploadFile')"/>
          </q-form>
        </div>
      </q-step>

      <q-step
          :done="step > 2"
          :name="2"
          icon="checklist"
          :title="t('admin.importCredentials.pickCredentials')"
      >
        <div class="column no-wrap" style="height: 45vh">
          <div class="q-pa-lg" style="font-size: 1.25em">
            <p>{{ t('admin.importCredentials.steps.step4') }}</p>

            <p>{{ t('admin.importCredentials.steps.step5') }}</p>
          </div>
          <q-table
              class="fit"
              style="background-color: var(--bg-color); color: var(--text-color)"
              v-model:selected="selected"
              :columns="columns"
              :rows="rows"
              flat
              row-key="name"
              selection="multiple">
            <template v-slot:body-cell-type="props">
              <q-td :props="props">
                <q-chip v-if="props.value === 'internal'" color="primary" style="font-size: 1em" text-color="secondary">
                  <q-icon left name="business"/>
                  Internal
                </q-chip>
                <q-chip v-else color="secondary" style="font-size: 1em" text-color="primary">
                  <q-icon left name="public"/>
                  External
                </q-chip>
              </q-td>
            </template>
          </q-table>
        </div>
      </q-step>

      <template v-slot:navigation>
        <q-stepper-navigation class="row q-mt-md justify-end">
          <q-btn v-if="step > 1" :label="t('admin.importCredentials.back')" class="q-ml-sm" color="primary" flat rounded
                 @click="$refs.stepper.previous()"/>
          <q-btn :disable="host == null && file == null"
                 :label="step === 2 ? t('admin.importCredentials.import') : t('admin.importCredentials.next')"
                 color="primary" flat
                 rounded @click="() => next($refs)"/>
        </q-stepper-navigation>
      </template>
    </q-stepper>
  </q-page>
</template>

<style scoped>

</style>