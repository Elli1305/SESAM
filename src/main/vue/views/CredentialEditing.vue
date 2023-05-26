<template>
  <q-form ref="form" class="q-pa-md" @submit.prevent>
    <div class="row q-col-gutter-md">
      <div class="col-12 col-md q-col-gutter-sm">
        <q-input v-model="credential.name" :rules="[required]" label="Name" outlined type="text"/>
        <q-input v-model="credential.agent" :rules="[required]" label="Agent" outlined type="text"/>
        <q-input v-model="credential.credentialDefinitionId" :rules="[required]" class="q-mb-md"
                 label="Credential Definition ID"
                 outlined type="text"/>

        <div v-for="(attribute, index) in credential.attributes" class="q-col-gutter-sm">
          <div class="row q-col-gutter-sm">
            <q-input v-model="attribute.name" :rules="[required]" class="col-10" label="Name" outlined type="text"/>
            <q-select v-model="attribute.type" :options="types" class="col-2" emit-value label="Type" map-options
                      outlined/>
          </div>

          <div class="row q-col-gutter-sm">
            <q-input v-model="attribute.attributeName" :rules="[required]" class="col-10" label="Attribute Name"
                     outlined type="text"/>

            <q-btn-group class="col-2" flat>
              <q-btn v-if="index !== 0" color="primary" flat icon="remove"
                     @click="() => credential.attributes.splice(index, 1)"/>
              <q-btn color="primary" flat icon="add" @click="addAttribute(index)"/>
            </q-btn-group>
          </div>
        </div>
      </div>
      <div class="col-12 col-md q-col-gutter-sm">
        <div v-for="(condition, index) in credential.conditions" class="row q-col-gutter-sm">
          <q-input v-model="condition.label" :rules="[required]" class="col-10" label="Name" outlined type="text"/>

          <q-btn-group class="col-2" flat>
            <q-btn v-if="index !== 0" color="primary" flat icon="remove"
                   @click="() => credential.conditions.splice(index, 1)"/>
            <q-btn color="primary" flat icon="add" @click="addCondition(index)"/>
          </q-btn-group>
        </div>
      </div>
    </div>
    <p class="q-mt-lg">{{ credential }}</p>
  </q-form>
  <q-page-sticky :offset="[18, 18]" position="bottom-right">
    <q-btn color="primary" fab icon="save" unelevated @click="save"/>
  </q-page-sticky>
  <q-page-sticky v-if="props.id !== undefined" :offset="[18, 18]" position="bottom-left">
    <q-btn color="primary" fab icon="delete" unelevated @click="deleteCredential"/>
  </q-page-sticky>
</template>

<script lang="ts" setup>
import {QForm, QSelectOption, ValidationRule} from "quasar";
import {ref, Ref} from "vue";
import {CreateCredential} from "@/main/vue/entity/credentialDefinition";
import api from "@/main/vue/api";
import {useI18n} from "vue-i18n";
import router from "@/main/vue/router";

const props = defineProps<{ id?: number }>();

const {t} = useI18n();

const types: QSelectOption[] = [
  {
    label: 'Text',
    value: 'text',
  },
  {
    label: 'Number',
    value: 'number',
  },
  {
    label: 'Date',
    value: 'date',
  }
];

const credential: Ref<CreateCredential> = ref({
  name: '',
  agent: '',
  credentialDefinitionId: '',
  attributes: [{type: types[0].value, name: '', attributeName: ''}],
  conditions: [{label: ''}],
});

const required: ValidationRule<string> = (value) => !!value || t('issueCredential.validation.inputRequired');
const form: Ref<QForm | null> = ref(null);

const addCondition = (index: number) => {
  credential.value.conditions.splice(index + 1, 0, {label: ''});
}

const addAttribute = (index: number) => {
  credential.value.attributes.splice(index + 1, 0, {type: 'text', name: '', attributeName: ''});
}

const save = async () => {
  if (!form.value || !(await form.value.validate())) {
    return;
  }

  api.credential.create(credential.value)
      .then(() => {
        router.push("/credential_administration");
      })
      .catch(reason => {
        console.log(reason);
      });
}

const deleteCredential = async () => {
  if (!props.id) {
    return;
  }

  api.credential.delete(props.id)
      .then(() => {
        router.push("/credential_administration");
      })
      .catch(reason => {
        console.log(reason);
      });
}
</script>

<style scoped></style>