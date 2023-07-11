<template>
  <q-page class="column justify-evenly items-center" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{ t('imprint.imprintEditorTitle') }}</p>
    <div class="row justify-evenly no-wrap" style="width: 80vw; height: 50vh">
      <q-btn class="self-end q-mr-lg" round icon="delete" color="negative" text-color="positive"
             @click="showDeleteDialog" style="width: 4em; height: 4em"/>
      <div style="display: flex; flex-direction: column; align-items: center;">
        <q-editor
            v-model="editorContent"
            style="margin-bottom: 20px;"
            :toolbar="[
        [
          {
            label: $q.lang.editor.align,
            icon: $q.iconSet.editor.align,
            fixedLabel: true,
            list: 'only-icons',
            options: ['left', 'center', 'right', 'justify']
          },
          {
            label: $q.lang.editor.align,
            icon: $q.iconSet.editor.align,
            fixedLabel: true,
            options: ['left', 'center', 'right', 'justify']
          }
        ],
        ['bold', 'italic', 'strike', 'underline', 'subscript', 'superscript'],
        ['token', 'hr', 'link', 'custom_btn'],
        ['print', 'fullscreen'],
        [
          {
            label: $q.lang.editor.formatting,
            icon: $q.iconSet.editor.formatting,
            list: 'no-icons',
            options: [
              'p',
              'h1',
              'h2',
              'h3',
              'h4',
              'h5',
              'h6',
              'code'
            ]
          },
          {
            label: $q.lang.editor.fontSize,
            icon: $q.iconSet.editor.fontSize,
            fixedLabel: true,
            fixedIcon: true,
            list: 'no-icons',
            options: [
              'size-1',
              'size-2',
              'size-3',
              'size-4',
              'size-5',
              'size-6',
              'size-7'
            ]
          },
          {
            label: $q.lang.editor.defaultFont,
            icon: $q.iconSet.editor.font,
            fixedIcon: true,
            list: 'no-icons',
            options: [
              'default_font',
              'arial',
              'arial_black',
              'comic_sans',
              'courier_new',
              'impact',
              'lucida_grande',
              'times_new_roman',
              'verdana'
            ]
          },
          'removeFormat'
        ],
        ['quote', 'unordered', 'ordered', 'outdent', 'indent'],

        ['undo', 'redo'],
        ['viewsource']
      ]"
            :fonts="{
        arial: 'Arial',
        arial_black: 'Arial Black',
        comic_sans: 'Comic Sans MS',
        courier_new: 'Courier New',
        impact: 'Impact',
        lucida_grande: 'Lucida Grande',
        times_new_roman: 'Times New Roman',
        verdana: 'Verdana'
      }"
        />

        <q-btn class="self-end q-ml-lg" round icon="save" color="positive" text-color="negative"
               @click="showConfirmDialog" style="width: 4em; height: 4em"/>

      </div>
      <q-dialog v-model="confirmDialog">
        <q-card>
          <q-card-section>
            <div class="text-h6">Bestätigen</div>
          </q-card-section>
          <q-card-section class="row items-center">
            <div class="q-mx-sm">{{ t('admin.imprint.imprintEditorMessageSave') }}</div>
          </q-card-section>
          <q-card-actions align="right">
            <q-btn flat :label="t('common.cancel')" color="primary" @click="confirmDialog = false"/>
            <q-btn flat :label="t('common.save')" color="primary" @click="postText"/>
          </q-card-actions>
        </q-card>
      </q-dialog>

      <q-dialog v-model="deleteDialog">
        <q-card>
          <q-card-section>
            <div class="text-h6">Löschen bestätigen</div>
          </q-card-section>
          <q-card-section class="row items-center">
            <div class="q-mx-sm">{{ t('admin.imprint.imprintEditorMessageDelete') }}</div>
          </q-card-section>
          <q-card-actions align="right">
            <q-btn flat :label="t('common.cancel')" color="primary" @click="deleteDialog = false"/>
            <q-btn flat :label="t('common.delete')" color="primary" @click="deletePostedContent"/>
          </q-card-actions>
        </q-card>
      </q-dialog>
    </div>
  </q-page>
</template>

<script lang="ts">
import {ref, onMounted} from 'vue';
import {QDialog, useQuasar} from 'quasar';
import {
  getImprintContent,
  postImprintContent,
  deleteImprintContent,
  getLatestImprint,
} from '../api/imprint';
import {useI18n} from "vue-i18n";

export default {

  components: {
    QDialog,
  },
  setup() {
    const $q = useQuasar();
    const {t} = useI18n()
    const editorContent = ref('');
    const imprintContent = ref('');
    const showEditor = ref(false);
    const confirmDialog = ref(false);
    const deleteDialog = ref(false);
    const initialImprintContent = ref('');
    const imprintPhotos = ref([] as File[]);
    const uploaderRef = ref<any>(null);


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
              message: t('admin.imprint.imprintEditorMessageSaveConfirmation'),
              position: 'bottom',
              color: "positive",
              textColor: "negative",
              timeout: 1500,
            });
          })
          .catch((error) => {
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
              message: t('admin.imprint.imprintEditorMessageDeleteConfirmation'),
              position: 'bottom',
              timeout: 1500,
              color: "negative",
              textColor: "positive",
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
    const uploadPhotos = () => {
      const files = imprintPhotos.value;

      if (files.length === 0) {
        return;
      }


    };

    const onFilesAdded = () => {
      if (uploaderRef.value) {
        const fileInput = uploaderRef.value.$el.querySelector('input[type=file]') as HTMLInputElement;
        if (fileInput && fileInput.files) {
          const files: File[] = Array.from(fileInput.files);
          imprintPhotos.value.push(...files);
        }
      }
    };


    return {
      editorContent,
      imprintContent,
      showEditor,
      loadLatestContent,
      confirmDialog,
      deleteDialog,
      showConfirmDialog,
      postText,
      showDeleteDialog,
      deletePostedContent,
      initialImprintContent,
      imprintPhotos,
      uploadPhotos,
      onFilesAdded,
      t,
    };
  },
};
</script>

<style scoped>
</style>
