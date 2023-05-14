<template>
  <div class="q-pa-md">
    <div class="row q-col-gutter-md">
      <div class="col-12 col-md q-col-gutter-sm">
        <q-input label="Name" outlined type="text"/>
      </div>
      <div class="col-12 col-md q-col-gutter-sm">
        <q-input label="Agent" outlined type="text"/>
        <q-input label="Credential Definition ID" outlined type="text"/>
      </div>
    </div>
    <div class="row q-col-gutter-md q-mt-md">
      <div class="col-12 col-md">
        <div v-for="(attribute, index) in attributes" class="row q-col-gutter-sm q-mb-sm">
          <q-input v-model="attribute.name" class="col-9" label="Name" outlined type="text"
                   @click.prevent="focus(index)"/>
          <q-select v-model="attribute.type" :options="types" class="col-3" emit-value label="Type" map-options
                    outlined/>
        </div>
      </div>
      <div class="col-12 col-md">
        <q-input label="Name" outlined type="text"/>
      </div>
    </div>
    <p>{{ attributes }}</p>
  </div>
  <q-page-sticky position="bottom-right" :offset="[18, 18]">
    <q-btn fab icon="save" color="accent" @click="save" unelevated />
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

const attributes: Ref<CreateAttribute[]> = ref([{type: types[0].value, name: ''}]);

const focus = (index: number) => {
  if (index !== attributes.value.length - 1) {
    return;
  }

  attributes.value.push({type: 'text', name: ''});
}

const save = () => {}
</script>

<style scoped></style>