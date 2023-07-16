<script lang="ts" setup>
import {QForm, QStepper, QTableColumn, useQuasar} from "quasar";
import {Ref, ref} from "vue";
import axios from "axios";
import {CredentialExport, ExternalCredentialExport} from "@/main/vue/entity/credentialDefinition";
import api from "@/main/vue/api";
import router from "@/main/vue/router";

const $q = useQuasar();

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
            message: 'Die eingegebene URL ist nicht valide.',
            color: 'negative',
            textColor: 'positive',
          })

          return;
        }

        $q.notify({
          type: 'negative',
          position: 'bottom',
          timeout: 6000,
          message: 'Ein unbekannter Fehler ist aufgetreten.',
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
          message: 'Beim öffnen der Datei ist ein unbekannter Fehler aufgetreten.',
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
        message: 'Es wurden keine Credentials ausgewählt.',
        color: 'negative',
        textColor: 'positive',
      })
    }

    await api.credential.importCredentials(
        {
          internalCredentials: selected.value.filter(value => value.type === 'internal') as any,
          externalCredentials: selected.value.filter(value => value.type === 'external')
        }
    );

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
          title="Quelle auswählen">
        <div class="row justify-evenly" style="height: 45vh">
          <div class="column q-pa-lg" style="width: 40%; font-size: 1.5em">
            <p>Um Credentials zu importieren, stehen Ihnen auf unserer Seite verschiedene Optionen zur Verfügung. Sie
              können entweder die URL einer anderen Instanz angeben oder eine zuvor exportierte Datei importieren.</p>

            <p>Wenn Sie sich für die erste Option entscheiden, geben Sie einfach die URL der entsprechenden Seite ein.
              Dadurch werden die Credentials von dieser Instanz abgerufen und auf unserer Seite angezeigt.</p>

            <p>Alternativ dazu können Sie eine zuvor exportierte Datei importieren. Klicken Sie dazu auf die
              Schaltfläche
              "Datei auswählen" und suchen Sie die entsprechende Datei auf Ihrem Computer. Nach dem Hochladen werden die
              Credentials aus der Datei extrahiert und auf unserer Seite angezeigt.</p>
          </div>
          <q-form ref="form" class="column justify-evenly" @submit.prevent style="width: 40%">
            <q-input v-model="host" :disable="file != null" class="q-mb-md" type="url"
                     label="URL einer anderen Instanz"/>
            <q-file v-model="file" :disable="host != null && host.length > 0" accept="application/json"
                    label="Exportierte Datei hochladen"/>
          </q-form>
        </div>
      </q-step>

      <q-step
          :done="step > 2"
          :name="2"
          icon="checklist"
          title="Credentials auswählen"
      >
        <div class="column no-wrap" style="height: 45vh">
          <div class="q-pa-lg" style="font-size: 1.25em">
            <p>Nun haben Sie die Möglichkeit, aus den angezeigten Credentials auszuwählen, welche Sie importieren
              möchten. Dafür gibt es in jeder Zeile ein Kontrollkästchen. Wählen Sie die gewünschten Credentials aus,
              indem Sie die entsprechenden Kontrollkästchen aktivieren. Möchten Sie alle Credentials auf einmal
              auswählen, können Sie auch das Kontrollkästchen in der Tabellenüberschrift verwenden.</p>

            <p>Nachdem Sie Ihre Auswahl getroffen haben, klicken Sie einfach auf die Schaltfläche "Importieren". Dadurch
              werden die ausgewählten Credentials in Ihre Instanz integriert.</p>
          </div>
          <q-table
              class="fit"
              style="background-color: var(--bg-color); color: var(--text-color)"
              v-model:selected="selected"
              :columns="columns"
              :rows="rows"
              flat
              row-key="name"
              selection="multiple"/>
        </div>
      </q-step>

      <template v-slot:navigation>
        <q-stepper-navigation class="row q-mt-md justify-end">
          <q-btn v-if="step > 1" class="q-ml-sm" color="primary" flat label="Zurück" rounded
                 @click="$refs.stepper.previous()"/>
          <q-btn :disable="host == null && file == null" :label="step === 2 ? 'Importieren' : 'Weiter'"
                 color="primary" flat
                 rounded @click="() => next($refs)"/>
        </q-stepper-navigation>
      </template>
    </q-stepper>
  </q-page>
</template>

<style scoped>

</style>