<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">Credential bearbeiten</p>
      <q-form class="column justify-between self-center no-wrap full-width" ref="form" @submit.prevent style="height: 50vh">
        <div class="row no-wrap full-width">
          <div class="column justify-evenly" style="width: 15vw; height: 40vh; margin-right: 3vw">
            <q-input style="width: 15vw; margin-bottom: -1em" v-model="credential.name" :rules="[required]" lazy-rules error-message=" " no-error-icon label="Name" outlined type="text"/>
            <q-input style="width: 15vw; margin-bottom: -1em" v-if="props.type !== 'external'" v-model="credential.agent" :rules="[required]" lazy-rules error-message=" " no-error-icon label="Agent" outlined type="text"/>
            <q-input style="width: 15vw; margin-bottom: -1em" v-model="credential.credentialDefinitionId" :rules="[required]" lazy-rules error-message=" " no-error-icon class="q-mb-md"
                     label="Credential Definition ID"
                     outlined type="text"/>
          </div>
          <q-virtual-scroll class="column full-width" style="height: 40vh" :items="[0]">
            <div class="row no-wrap full-width">
              <div class="column full-width">
                <h6 v-if="props.type !== 'external'" class="no-margin q-pb-sm">Credentialattribute</h6>
                  <div class="full-width" v-for="(item, index) in credential.attributes">
                    <q-item class="row justify-between q-px-sm" style="margin-bottom: -1em">
                      <q-input style="width: 15vw" v-model="item.name" :rules="[required]" lazy-rules error-message=" " no-error-icon label="Name" outlined type="text"/>
                      <q-input style="width: 15vw" v-model="item.attributeName" :rules="[required]" lazy-rules error-message=" " label="Name des Attributes"
                               outlined type="text"/>
                      <div class="row no-wrap">
                        <q-select style="width: 10vw" v-model="item.type" :options="types" emit-value label="Type" map-options
                                  outlined/>
                        <div class="column justify-between items-center q-mx-sm">
                          <div class="column justify-center no-wrap" style="height: 4em">
                            <q-btn size="0.75em" style="width: 2em" color="black" round flat icon="lock" @click="editValidation(item)"/>
                            <q-btn size="0.75em" style="width: 2em" color="primary" v-if="index !== 0" round flat icon="remove"
                                   @click="() => credential.attributes.splice(index, 1)"/>
                          </div>
                          <q-btn size="0.75em" color="primary" style="width: 2em" round flat icon="add" @click="addAttribute(index)"/>
                        </div>
                        </div>
                      </q-item>
                  </div>
              </div>
              <div class="column" style="width: 20vw; margin-left: 2vw">
                <h6 v-if="props.type !== 'external'" class="no-margin q-pb-sm">Checkliste</h6>
                <div class="full-width" v-for="(item, index) in credential.conditions">
                  <q-item class="row q-px-sm" style="margin-bottom: -1em">
                    <q-input style="width: 15vw" v-model="item.label" :rules="[required]" lazy-rules error-message=" " no-error-icon label="Name" outlined type="text"/>
                    <q-btn-group class="column justify-between items-center q-mx-sm" flat>
                      <div class="column justify-center" style="height: 4em">
                        <q-btn size="0.75em" style="width: 2em" v-if="index !== 0" flat icon="remove"
                               @click="() => credential.conditions.splice(index, 1)"/>
                      </div>
                      <q-btn size="0.75em" style="width: 2em" color="primary" flat icon="add" @click="addCondition(index)"/>
                    </q-btn-group>
                  </q-item>
                </div>
              </div>
            </div>
          </q-virtual-scroll>
        </div>
        <div class="row justify-around q-mt-md">
          <q-btn style="width: 4em; height: 4em" color="negative" text-color="positive" fab icon="delete" unelevated @click="deleteCredential"/>
          <q-btn style="width: 4em; height: 4em" color="positive" text-color="negative" fab icon="save" unelevated @click="save"/>
          </div>
      </q-form>
  </q-page>
</template>

<script lang="ts" setup>
import {QForm, QSelectOption, useQuasar, ValidationRule} from "quasar";
import {onMounted, ref, Ref} from "vue";
import {CreateCredential} from "@/main/vue/entity/credentialDefinition";
import {CreateAttribute} from "@/main/vue/entity/credentialDefinition";
import api from "@/main/vue/api";
import {useI18n} from "vue-i18n";
import router from "@/main/vue/router";
import ValidateCredentials from "@/main/vue/views/ValidateCredentials.vue";

const props = defineProps<{ id?: string, type?: 'internal' | 'external' }>();


const {t} = useI18n();
const $q = useQuasar()

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
  attributes: [{type: types[0].value, name: '', attributeName: '', validationRules: []}],
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
          name: f.label,
          validationRules: f.validationRules
        })),
        conditions: response.data.checklist,
      }
    })
  }
});

const required: ValidationRule<string> = (value) => !!value || t('issuer.issueCredential.validation.inputRequired');
const form: Ref<QForm | null> = ref(null);

const addCondition = (index: number) => {
  credential.value.conditions.splice(index + 1, 0, {label: ''});
}

const addAttribute = (index: number) => {
  credential.value.attributes.splice(index + 1, 0, {type: 'text', name: '', attributeName: '', validationRules: []});
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

const editValidation = (item: CreateAttribute) => {
  $q.dialog({
    component: ValidateCredentials,
    componentProps: {
      attribute: item
    }
  }).onOk(() => {
  })
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