<template>
  <q-page class="items-center justify-center" style="display: flex">
    <div class="q-gutter-y-md column" style="max-width: 40em; min-width: 20em; display: flex">
      <div class="q-gutter-y-md column" style="max-width: 40em; min-width: 20em; display: flex">
        <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">{{ t('imprint.imprintEditorTitle') }}</h1>
        <div v-if="!showEditor" class="imprint-content" v-html="imprintContent || initialImprintContent"></div>
      </div>
      <q-editor v-model="editorContent" v-if="showEditor"></q-editor>
      <div class="editor-result" v-if="showEditor" v-html="editorContent"></div>
      <div class="action-buttons" v-if="showEditor"
           style="display: flex; justify-content: space-between; width: 100%; margin-top: 1em; margin-bottom: 2em;">
        <q-btn icon="delete" color="primary" @click="showDeleteDialog" size="md"/>
        <q-btn icon="save" color="primary" @click="showConfirmDialog" size="md"/>
      </div>
      <div class="action-buttons"
           style="display: flex; justify-content: center; width: 100%; margin-top: 1em; margin-bottom: 2em;">
        <q-btn icon="edit" color="primary" @click="toggleEditor" v-if="!showEditor" size="md"/>
      </div>
    </div>
    <q-dialog v-model="confirmDialog">
      <q-card>
        <q-card-section class="q-pa-md">
          <div class="q-mt-md">{{ t('imprint.imprintEditorMessageSave') }}</div>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn :label="t('imprint.cancel')" color="primary" @click="confirmDialog = false"/>
          <q-btn :label="t('imprint.save')" color="primary" @click="postText"/>
        </q-card-actions>
      </q-card>
    </q-dialog>

    <q-dialog v-model="deleteDialog">
      <q-card>
        <q-card-section class="q-pa-md">
          <div class="q-mt-md">{{ t('imprint.imprintEditorMessageDelete') }}</div>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn :label="t('imprint.cancel')" color="primary" @click="deleteDialog = false"/>
          <q-btn :label="t('imprint.delete')" color="primary" @click="deletePostedContent"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script lang="ts">
import {ref, onMounted} from 'vue';
import {QDialog, useQuasar} from 'quasar';
import {getImprintContent, postImprintContent, deleteImprintContent, getLatestImprint} from '../api/imprint';
import {useI18n} from "vue-i18n";

export default {

  components: {
    QDialog,
  },
  setup() {
    const $q = useQuasar();
    const editorContent = ref('');
    const imprintContent = ref('');
    const showEditor = ref(false);
    const confirmDialog = ref(false);
    const deleteDialog = ref(false);
    const initialImprintContent = ref('');
    const {t} = useI18n();

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
      console.log('postText called');
      postImprintContent(editorContent.value)
          .then(() => {
            imprintContent.value = editorContent.value;
            showEditor.value = false;
            confirmDialog.value = false;

            $q.notify({
              type: 'positive',
              message: t('imprint.imprintEditorMessageSaveConfirmation'),
              position: 'bottom',
              timeout: 3000,
            });
          })
          .catch((error) => {
            console.log(error);
          })
          .finally(() => {
            confirmDialog.value = false;
          });
    };

    const toggleEditor = () => {
      showEditor.value = !showEditor.value;
      if (showEditor.value) {
        loadLatestContent();
      }
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
              message: t('imprint.imprintEditorMessageDeleteConfirmation'),
              position: 'bottom',
              timeout: 3000,
            });
          })
          .catch((error) => {
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
      imprintContent,
      showEditor,
      toggleEditor,
      loadLatestContent,
      confirmDialog,
      deleteDialog,
      showConfirmDialog,
      postText,
      showDeleteDialog,
      deletePostedContent,
      initialImprintContent,
      t,
    };
  },
};
</script>

<style scoped>
h1 {
  font-size: 3em;
  text-align: center;
  margin-bottom: -0.5em;
}

.editor-result {
  margin-top: 2em;
}
</style>
