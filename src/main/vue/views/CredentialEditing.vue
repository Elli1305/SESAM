<template>
  <div class="q-pa-md">
    <div class="row q-col-gutter-md">
      <div class="col-12 col-md q-col-gutter-sm">
        <q-input v-model="credential.name" label="Name" outlined type="text"/>
        <q-input v-model="credential.agent" label="Agent" outlined type="text"/>
        <q-input v-model="credential.credentialDefinitionId" class="q-mb-md" label="Credential Definition ID" outlined
                 type="text"/>

        <div v-for="(attribute, index) in credential.attributes" class="row q-col-gutter-sm">
          <q-input v-model="attribute.name" class="col-8" label="Name" outlined type="text" />
          <q-select v-model="attribute.type" :options="types" class="col-2" emit-value label="Type" map-options
                    outlined/>
          <q-btn-group flat class="col-2">
            <q-btn v-if="index !== 0" color="primary" icon="remove" flat @click="() => credential.attributes.splice(index, 1)" />
            <q-btn v-if="index === 0" color="primary" icon="add" flat @click="addAttribute(index)" />
          </q-btn-group>
        </div>
      </div>
      <div class="col-12 col-md q-col-gutter-sm">
        <div v-for="(condition, index) in credential.conditions" class="row q-col-gutter-sm">
          <q-input v-model="condition.value" label="Name" outlined type="text" class="col-10" />

          <q-btn-group flat class="col-2">
            <q-btn v-if="index !== 0" color="primary" icon="remove" flat @click="() => credential.conditions.splice(index, 1)" />
            <q-btn v-if="index === 0" color="primary" icon="add" flat @click="() => credential.conditions.push({value: ''})" />
          </q-btn-group>
        </div>
      </div>
    </div>
    <p class="q-mt-lg">{{ credential }}</p>
  </div>
  <q-page-sticky :offset="[18, 18]" position="bottom-right">
    <q-btn color="accent" fab icon="save" unelevated @click="save"/>
  </q-page-sticky>
</template>

<script lang="ts" setup>
import {QSelectOption} from "quasar";
import {ref, Ref} from "vue";

const props = defineProps<{ id?: number }>();

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

interface CreateAttribute {
  type: string,
  name: string
}

interface CreateCredential {
  name: string;
  agent: string;
  credentialDefinitionId: string;

  attributes: CreateAttribute[];
  conditions: { value: string; }[];
}

const credential: Ref<CreateCredential> = ref({
  name: '',
  agent: '',
  credentialDefinitionId: '',
  attributes: [{type: types[0].value, name: ''}],
  conditions: [{value: ''}],
});

const addAttribute = (index: number) => {
  if (index !== credential.value.attributes.length - 1) {
    return;
  }

  credential.value.attributes.push({type: 'text', name: ''});
}

const save = () => {
}
</script>

<style scoped></style>