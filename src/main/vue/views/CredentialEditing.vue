<template>
  <q-form ref="form" class="q-pa-md" @submit.prevent>
    <q-input v-model="credential.name" :rules="[required]" label="Name" outlined type="text"/>
    <q-input v-if="props.type !== 'external'" v-model="credential.agent" :rules="[required]" label="Agent" outlined type="text"/>
    <q-input v-model="credential.credentialDefinitionId" :rules="[required]" class="q-mb-md"
             label="Credential Definition ID"
             outlined type="text"/>

    <h6 v-if="props.type !== 'external'" class="q-ma-none q-pa-none q-pb-sm">Credential Attributes</h6>
    <div v-if="props.type !== 'external'" v-for="(attribute, index) in credential.attributes">
      <div class="row q-col-gutter-sm">
        <q-input v-model="attribute.name" :rules="[required]" class="col-5" label="Name" outlined type="text"/>
        <q-input v-model="attribute.attributeName" :rules="[required]" class="col-5" label="Attribute Name"
                 outlined type="text"/>
        <q-select v-model="attribute.type" :options="types" class="col-1" emit-value label="Type" map-options
                  outlined/>
        <q-btn-group class="col-1" flat>
          <q-btn v-if="index !== 0" color="primary" flat icon="remove"
                 @click="() => credential.attributes.splice(index, 1)"/>
          <q-btn color="primary" flat icon="add" @click="addAttribute(index)"/>
        </q-btn-group>
      </div>
    </div>

    <h6 v-if="props.type !== 'external'" class="q-ma-none q-pa-none q-pb-sm">Credential Conditions</h6>
    <div v-if="props.type !== 'external'" v-for="(condition, index) in credential.conditions" class="row q-col-gutter-sm">
      <q-input v-model="condition.label" :rules="[required]" class="col-11" label="Name" outlined type="text"/>

      <q-btn-group class="col-1" flat>
        <q-btn v-if="index !== 0" color="primary" flat icon="remove"
               @click="() => credential.conditions.splice(index, 1)"/>
        <q-btn color="primary" flat icon="add" @click="addCondition(index)"/>
      </q-btn-group>
    </div>
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
import {onMounted, ref, Ref} from "vue";
import {CreateCredential} from "@/main/vue/entity/credentialDefinition";
import api from "@/main/vue/api";
import {useI18n} from "vue-i18n";
import router from "@/main/vue/router";

const props = defineProps<{ id?: number, type?: 'internal' | 'external' }>();


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

onMounted(() => {
  if (props.id !== undefined) {
    api.credential.get(props.id.toString()).then(response => {
      credential.value = {
        name: response.data.name,
        agent: response.data.agent,
        credentialDefinitionId: response.data.credentialDefinitionId,
        attributes: response.data.form.map(f => ({
          id: f.id,
          attributeName: f.attributeName,
          type: f.type,
          name: f.label
        })),
        conditions: response.data.checklist,
      }
    })
  }
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

  if (!props.id) {
    api.credential.create(credential.value)
        .then(() => {
          router.push("/credential_administration");
        })
        .catch(reason => {
          console.log(reason);
        });
  } else {
    api.credential.update(props.id, credential.value)
        .then(() => {
          router.push("/credential_administration");
        })
        .catch(reason => {
          console.log(reason);
        });
  }
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