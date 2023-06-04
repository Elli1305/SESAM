<template>
  <q-page class="column justify-evenly items-center" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{t('imprintEdit.title')}}</p>
      <div class="row justify-evenly no-wrap" style="width: 75vw; height: 25em">
        <q-btn class="self-end q-mr-lg" round icon="delete" color="negative" text-color="positive" @click="showDeleteDialog" style="width: 4em; height: 4em"/>
        <q-editor
            class="column full-height"
            toolbar-rounded
            :definitions="{
              left: {icon: 'format_align_left', tip: ''},
              center: {icon: 'format_align_center', tip: ''},
              right: {icon: 'format_align_right', tip: ''},
              justify: {icon: 'format_align_justify', tip: ''},
              bold: {icon: 'format_bold', tip: ''},
              italic: {icon: 'format_italic', tip: ''},
              underline: {icon: 'format_underline', tip: ''},
              strike: {icon: 'format_strikethrough', tip: ''},
              undo: {icon: 'undo', tip: ''},
              redo: {icon: 'redo', tip: ''}}"
            v-model="editorContent"
            style="width: 50vw"/>
        <q-btn class="self-end q-ml-lg" round icon="save" color="positive" text-color="negative" @click="showConfirmDialog" style="width: 4em; height: 4em"/>
      </div>
    <q-dialog v-model="confirmDialog">
      <q-card>
        <q-card-section>
          <div class="text-h6">Bestätigen</div>
          </q-card-section>
        <q-card-section class="row items-center">
          <div class="q-mx-sm">Möchten Sie das bestehende Impressum wirklich überschreiben?</div>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn flat label="Abbrechen" color="primary" @click="confirmDialog = false"/>
          <q-btn flat label="Ok" color="primary" @click="postText"/>
        </q-card-actions>
      </q-card>
    </q-dialog>

    <q-dialog v-model="deleteDialog">
      <q-card>
        <q-card-section>
          <div class="text-h6">Löschen bestätigen</div>
        </q-card-section>
        <q-card-section class="row items-center">
          <div class="q-mx-sm">Sind Sie sicher, dass Sie das Impressum löschen wollen?</div>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn flat label="Abbrechen" color="primary" @click="deleteDialog = false"/>
          <q-btn flat label="Löschen" color="primary" @click="deletePostedContent"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>
<script lang="ts">
import {ref, onMounted} from 'vue';
import {QDialog, useQuasar} from 'quasar';
import {getImprintContent, postImprintContent, deleteImprintContent, getLatestImprint} from '../api/imprint';
import { useI18n } from 'vue-i18n';

export default {
  name: "ImprintEditor",
  components: {
    QDialog,
  },
  setup() {
    const $q = useQuasar();
    const { t } = useI18n()
    const editorContent = ref('');
    const imprintContent = ref('');
    const showEditor = ref(false);
    const confirmDialog = ref(false);
    const deleteDialog = ref(false);
    const initialImprintContent = ref('');


    onMounted(async () => {
      try {
        const response = await getImprintContent();
        imprintContent.value = response.data;
        initialImprintContent.value = response.data;
        editorContent.value = imprintContent.value;
      } catch (error) {
        console.log(error);
      }
    });


    const showConfirmDialog = () => {
      confirmDialog.value = true;
    };

    const postText = () => {
      console.log("postText called");
      postImprintContent(editorContent.value)
          .then(() => {
            imprintContent.value = editorContent.value;
            showEditor.value = false;
            confirmDialog.value = false;

            $q.notify({
              type: 'positive',
              message: 'Inhalt erfolgreich gespeichert',
              position: 'bottom',
              color: "positive",
              textColor: "negative",
              timeout: 1500,
            });
          })
          .catch(error => {
            console.log(error);
          })
          .finally(() => {
            confirmDialog.value = false;
          });
    };

    const deletePostedContent = () => {
      deleteImprintContent()
          .then(() => {
            imprintContent.value = '';
            editorContent.value = '';
            initialImprintContent.value = '';
            showEditor.value = false;
            deleteDialog.value = false;

            $q.notify({
              type: 'negative',
              message: 'Inhalt erfolgreich gelöscht',
              position: 'bottom',
              timeout: 1500,
              color: "negative",
              textColor: "positive",
            });
          })
          .catch(error => {
            console.log(error);
          });
    };

    const showDeleteDialog = () => {
      deleteDialog.value = true;
    };

    const loadLatestContent = async () => {
      try {
        const response = await getLatestImprint();
        console.log(response.data);
        editorContent.value = response.data;
      } catch (error) {
        console.error(error);
      }
    };

    return {
      editorContent,
      imprintContent: imprintContent,
      showEditor,
      loadLatestContent,
      confirmDialog,
      deleteDialog,
      showConfirmDialog,
      postText,
      showDeleteDialog,
      deletePostedContent,
      t,


      initialImprintContent: initialImprintContent
    };
  }
}
</script>

<style scoped>
</style>
