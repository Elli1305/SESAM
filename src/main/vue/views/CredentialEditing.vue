<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">Credential bearbeiten</p>
      <q-form class="column justify-between self-center no-wrap" ref="form" @submit.prevent style="width: 80vw; height: 50vh">
        <div class="column no-wrap">
          <div class="row justify-evenly">
            <q-input style="width: 20vw" v-model="credential.name" :rules="[required]" label="Name" outlined type="text"/>
            <q-input style="width: 20vw" v-if="props.type !== 'external'" v-model="credential.agent" :rules="[required]" label="Agent" outlined type="text"/>
            <q-input style="width: 20vw" v-model="credential.credentialDefinitionId" :rules="[required]" class="q-mb-md"
                     label="Credential Definition ID"
                     outlined type="text"/>
          </div>
          <div class="row no-wrap">
            <div class="column full-width" style="padding-right: 3vw">
              <h6 v-if="props.type !== 'external'" class="no-margin q-pb-sm">Credentialattribute</h6>
                <q-virtual-scroll class="full-width" style="height: 30vh" :items="credential.attributes" v-slot="{ item, index }">
                  <q-item class="row justify-between q-px-sm">
                    <q-input style="width: 15vw" v-model="item.name" :rules="[required]" lazy-rules label="Name" outlined type="text"/>
                    <q-input style="width: 15vw" v-model="item.attributeName" :rules="[required]" lazy-rules label="Name des Attributes"
                             outlined type="text"/>
                    <div class="row no-wrap">
                      <q-select style="width: 10vw" v-model="item.type" :options="types" emit-value label="Type" map-options
                                outlined/>
                      <q-btn-group class="column justify-evenly" flat style="width: 2em; max-height: 4em">
                        <q-btn size="0.75em" v-if="index !== 0" color="primary" flat icon="remove"
                               @click="() => credential.attributes.splice(index, 1)"/>
                        <q-btn size="0.75em" color="primary" flat icon="add" @click="addAttribute(index)"/>
                      </q-btn-group>
                    </div>
                  </q-item>
                </q-virtual-scroll>
            </div>
            <div class="column justify-evenly" style="width: 25vw">
              <h6 v-if="props.type !== 'external'" class="no-margin q-pb-sm">Checkliste</h6>
              <q-virtual-scroll class="full-width" style="height: 30vh" :items="credential.conditions" v-slot="{ item, index }">
                  <q-item class="row q-px-sm">
                    <q-input style="width: 20vw" v-model="item.label" :rules="[required]" label="Name" outlined type="text"/>
                    <q-btn-group class="column justify-evenly" flat style="width: 2em; max-height: 4em">
                      <q-btn size="0.75em" v-if="index !== 0" color="primary" flat icon="remove"
                             @click="() => credential.conditions.splice(index, 1)"/>
                      <q-btn size="0.75em" color="primary" flat icon="add" @click="addCondition(index)"/>
                    </q-btn-group>
                  </q-item>
              </q-virtual-scroll>
            </div>
          </div>
        </div>
        <div class="row justify-around">
          <q-btn style="width: 4em; height: 4em" color="negative" text-color="positive" fab icon="delete" unelevated @click="deleteCredential"/>
          <q-btn style="width: 4em; height: 4em" color="positive" text-color="negative" fab icon="save" unelevated @click="save"/>
          </div>
      </q-form>
  </q-page>
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