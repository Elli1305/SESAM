<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p v-if="props.id && props.type" class="row text-h3 justify-center">{{ t('admin.credentialAdministration.edit') }}</p>
    <p v-else class="row text-h3 justify-center">{{ t('admin.credentialAdministration.add') }}</p>
    <q-form ref="form" class="row justify-between self-center no-wrap full-width" style="height: 50vh"
            @submit.prevent>
      <div class="column justify-evenly full-height" style="width: 15vw; height: 40vh; margin-right: 3vw">
        <q-select v-if="!props.type && !props.id" v-model="createType" :options="createOptions" emit-value
                  label="Credential Type" map-options outlined></q-select>

        <q-input v-model="credential.credentialDefinitionId" :disable="createType === 'internal' && createOnLedger"
                 :rules="[required]" error-message=" " label="Credential Definition ID" lazy-rules
                 no-error-icon outlined style="width: 15vw; margin-bottom: -1em" type="text"/>

        <q-checkbox v-show="createType === 'internal'" v-model="createOnLedger" label="Auf dem Ledger erstellen"/>

        <q-input v-model="credential.name" :rules="[required]" error-message=" " label="Name" lazy-rules no-error-icon
                 outlined style="width: 15vw; margin-bottom: -1em" type="text"/>
        <q-select v-if="props.type !== 'external'" v-model="credential.agent" :options="agents" emit-value
                  label="Agent"
                  map-options outlined style="width: 15vw; margin-bottom: -1em"/>
        <q-input v-model="credential.version" :rules="[required, val => new RegExp('^[0123456789.]+$').test(val)]" error-message=" " label="Version" lazy-rules
                 no-error-icon outlined style="width: 15vw; margin-bottom: -1em" type="text"/>
      </div>
      <div class="column no-wrap fit">
        <q-virtual-scroll :items="[0]" class="column fit" style="height: 40vh">
          <div class="row no-wrap full-width">
            <div class="column full-width">
              <h6 v-if="props.type !== 'external'" class="no-margin q-pb-sm">
                {{ t('admin.credentialAdministration.credentialAttribute') }}</h6>
              <div v-for="(item, index) in credential.attributes" class="full-width">
                <q-item class="row justify-between q-px-sm" style="margin-bottom: -1em">
                  <q-input v-model="item.name" :rules="[required]" error-message=" " label="Name" lazy-rules
                           no-error-icon
                           outlined style="width: 15vw" type="text"/>
                  <q-input v-model="item.attributeName" :label="t('admin.credentialAdministration.attribute')"
                           :rules="[required]" error-message=" " lazy-rules outlined style="width: 15vw" type="text"/>
                  <div class="row no-wrap">
                    <q-select v-model="item.type" :options="types" emit-value label="Type" map-options outlined
                              style="width: 10vw"/>
                    <div class="column justify-between items-center q-mx-sm">
                      <div class="column justify-around no-wrap" style="height: 4em">
                        <q-btn flat icon="lock" round size="0.70em" style="width: 2em"
                               @click="editValidation(item, credential.attributes)">
                          <q-tooltip  style="background-color: var(--bg-color); color: var(--text-color)" :offset="[0, 0]">
                            {{t('issuer.issueCredential.validation.validationRules')}}
                          </q-tooltip>
                        </q-btn>
                        <q-btn v-if="index !== 0" flat icon="remove" round size="0.70em"
                               style="width: 2em"
                               @click="() => credential.attributes.splice(index, 1)"/>
                      </div>
                      <q-btn color="primary" flat icon="add" round size="0.8em" style="width: 2em; margin-left: -4em"
                             @click="addAttribute(index)"/>
                    </div>
                  </div>
                </q-item>
              </div>
            </div>
            <div v-if="createType === 'internal'" class="column" style="width: 20vw; margin-left: 2vw">
              <h6 v-if="props.type !== 'external'" class="no-margin q-pb-sm">
                {{ t('admin.credentialAdministration.checklist') }}</h6>
              <div v-for="(item, index) in credential.conditions" class="full-width">
                <q-item class="row q-px-sm" style="margin-bottom: -1em">
                  <q-input v-model="item.label" :rules="[required]" error-message=" " label="Name" lazy-rules
                           no-error-icon outlined style="width: 15vw" type="text"/>
                  <q-btn-group class="column justify-between items-center q-mx-sm" flat>
                    <div class="column justify-center" style="height: 4em">
                      <q-btn v-if="index !== 0" flat icon="remove" size="0.75em" style="width: 2em"
                             @click="() => credential.conditions.splice(index, 1)"/>
                    </div>
                    <q-btn color="primary" flat icon="add" round size="0.8em" style="width: 2em; margin-left: -2em"
                           @click="addCondition(index)"/>
                  </q-btn-group>
                </q-item>
              </div>
            </div>
          </div>
        </q-virtual-scroll>
        <div class="row justify-end q-mt-md">
          <q-btn
              flat rounded
              size="1.25em"
              color="primary"
              :label="t('common.delete')"
              v-if="props.id && props.type"
              @click="deleteCredential"/>
          <q-btn
              flat rounded
              size="1.25em"
              color="primary"
              :label="t('common.save')"
              @click="save"/>
        </div>
      </div>
    </q-form>
  </q-page>
</template>

<script lang="ts" setup>
import {QForm, QSelectOption, useQuasar, ValidationRule} from "quasar";
import {onBeforeMount, ref, Ref} from "vue";
import {
  CreateAttribute,
  CreateCredential,
  ExternalCredential,
  InternalCredential
} from "@/main/vue/entity/credentialDefinition";
import api from "@/main/vue/api";
import {useI18n} from "vue-i18n";
import router from "@/main/vue/router";
import ValidateCredentials from "@/main/vue/views/ValidateCredentials.vue";
import {AxiosError, AxiosResponse} from "axios";

const props = defineProps<{ id?: string, type?: 'internal' | 'external' }>();

const {t} = useI18n();
const $q = useQuasar();

const createOnLedger: Ref<boolean> = ref(false);

const createOptions: QSelectOption[] = [
  {
    label: 'Internal',
    value: 'internal'
  },
  {
    label: 'External',
    value: 'external'
  },
];

const createType: Ref<string> = ref(createOptions[0].value);

const agents: QSelectOption[] = [
  {
    label: 'University',
    value: 'university'
  },
  {
    label: 'T-Labs',
    value: 'tlabs'
  },
];

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
  version: '',
  credentialDefinitionId: '',
  attributes: [{type: types[0].value, name: '', attributeName: '', validationRules: []}],
  conditions: [{label: ''}],
});

onBeforeMount(async () => {
  if (!props.id || !props.type) {
    return;
  }

  api.credential[(props.type === 'internal' ? 'get' : 'getExternalCredential')](props.id)
      .then((response: AxiosResponse<InternalCredential | ExternalCredential>) => {
        credential.value = {
          name: response.data.name,
          agent: "agent" in response.data ? response.data.agent : '',
          version: response.data.version,
          credentialDefinitionId: response.data.credentialDefinitionId,
          attributes: response.data.form.map(f => ({
            id: f.id,
            attributeName: f.attributeName,
            type: f.type,
            name: f.label,
            validationRules: f.validationRules
          })),
          conditions: "checklist" in response.data ? response.data.checklist : [],
        }
      });
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

  $q.loading.show({delay: 400});

  if (!props.id) {
    (createType.value === 'internal' ? api.credential.create(createOnLedger.value, credential.value) : api.credential.createExternalCredential(credential.value))
        .then(() => {
          router.push("/credential_administration");
        })
        .catch(reason => {
          console.log(reason);

          $q.notify({
            type: 'negative',
            position: 'bottom',
            timeout: 6000,
            message: 'Beim erstellen des Credential ist ein Fehler aufgetreten.',
          });
        })
        .finally($q.loading.hide);
  } else {
    api.credential[(props.type === 'internal' ? 'update' : 'updateExternalCredential')](props.id, credential.value)
        .then(() => {
          router.push("/credential_administration");
        })
        .catch(reason => {
          console.log(reason);

          $q.notify({
            type: 'negative',
            position: 'bottom',
            timeout: 6000,
            message: 'Beim updaten des Credential ist ein Fehler aufgetreten.',
          });
        })
        .finally($q.loading.hide);
  }
}

const editValidation = (item: CreateAttribute, items: CreateAttribute[]) => {
  $q.dialog({
    component: ValidateCredentials,
    componentProps: {
      attribute: item,
      attributes: items
    }
  }).onOk(() => {
  })
}

const deleteCredential = async () => {
  if (!props.id || !props.type) {
    return;
  }

  $q.loading.show({delay: 400});

  api.credential[(props.type === 'internal' ? 'delete' : 'deleteExternalCredential')](props.id)
      .then(() => {
        router.push("/credential_administration");
      })
      .catch(reason => {
        console.log(reason);

        $q.notify({
          type: 'negative',
          position: 'bottom',
          timeout: 6000,
          message: 'Beim löschen des Credential ist ein Fehler aufgetreten.',
        });
      })
      .finally($q.loading.hide);
}
</script>

<style scoped></style>