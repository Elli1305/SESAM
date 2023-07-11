<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{ t('admin.credentialAdministration.edit') }}</p>
    <q-form ref="form" class="column justify-between self-center no-wrap full-width" style="height: 50vh"
            @submit.prevent>
      <div class="row no-wrap full-width">
        <div class="column justify-evenly" style="width: 15vw; height: 40vh; margin-right: 3vw">
          <q-input v-model="credential.credentialDefinitionId" :rules="[required]"
                   class="q-mb-md" error-message=" " label="Credential Definition ID" lazy-rules no-error-icon
                   outlined
                   style="width: 15vw; margin-bottom: -1em" type="text"/>

          <q-btn flat color="primary" :label="t('admin.credentialAdministration.loadCredentialSchema')" @click="getCredentialSchema"/>
          <q-input v-model="credential.name" :rules="[required]" error-message=" " label="Name"
                   lazy-rules no-error-icon outlined style="width: 15vw; margin-bottom: -1em" type="text"/>
          <q-select v-if="props.type !== 'external'" v-model="credential.agent" :options="['university', 'tlabs']"
                    label="Agent" outlined style="width: 15vw; margin-bottom: -1em"/>
        </div>
        <q-virtual-scroll :items="[0]" class="column full-width" style="height: 40vh">
          <div class="row no-wrap full-width">
            <div class="column full-width">
              <h6 v-if="props.type !== 'external'" class="no-margin q-pb-sm">
                {{ t('admin.credentialAdministration.credentialAttribute') }}</h6>
              <div v-for="(item, index) in credential.attributes" class="full-width">
                <q-item class="row justify-between q-px-sm" style="margin-bottom: -1em">
                  <q-input v-model="item.name" :rules="[required]" error-message=" " label="Name" lazy-rules
                           no-error-icon outlined style="width: 15vw" type="text"/>
                  <q-input v-model="item.attributeName" :label="t('admin.credentialAdministration.attribute')" :rules="[required]" error-message=" "
                           lazy-rules outlined
                           style="width: 15vw" type="text"/>
                  <div class="row no-wrap">
                    <q-select v-model="item.type" :options="types" emit-value label="Type" map-options
                              outlined
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
            <div class="column" style="width: 20vw; margin-left: 2vw">
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
      </div>
      <div class="row justify-around q-mt-md">
        <q-btn color="negative" fab icon="delete" style="width: 4em; height: 4em" text-color="positive" unelevated
               @click="deleteCredential"/>
        <q-btn color="positive" fab icon="save" style="width: 4em; height: 4em" text-color="negative" unelevated
               @click="save"/>
      </div>
    </q-form>
  </q-page>
</template>

<script lang="ts" setup>
import {QForm, QSelectOption, useQuasar, ValidationRule} from "quasar";
import {onBeforeMount, ref, Ref} from "vue";
import {CreateAttribute, CreateCredential} from "@/main/vue/entity/credentialDefinition";
import api from "@/main/vue/api";
import {useI18n} from "vue-i18n";
import router from "@/main/vue/router";
import ValidateCredentials from "@/main/vue/views/ValidateCredentials.vue";
import {AxiosError} from "axios";

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

onBeforeMount(async () => {
  if (props.id !== undefined) {
    await api.credential.get(props.id.toString()).then(response => {
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

const getCredentialSchema = async () => {
  if (credential.value.credentialDefinitionId.length == 0) {
    return;
  }

  $q.loading.show({delay: 400});

  await api.credential.getCredentialSchema(credential.value.credentialDefinitionId)
      .then(response => {
        credential.value.name = response.data.name;
        credential.value.credentialDefinitionId = response.data.credentialDefinitionId;

        if (response.data.agent) {
          credential.value.agent = response.data.agent;
        }

        credential.value.attributes = response.data.attrs.map(v => ({
          type: 'text',
          name: '',
          attributeName: v,
          validationRules: [],
        }));
      })
      .catch((e: AxiosError<{ code: string; }>) => {
        $q.notify({
          type: 'negative',
          position: 'bottom',
          timeout: 6000,
          message: t('admin.credentialEditing.credentialSchemaLoadFailed'),
          caption: t(`admin.credentialEditing.errors.${e.response?.data.code ?? 'ERR_LEDGER_COMMUNICATION_FAILED'}`),
        });
      })
      .finally($q.loading.hide);
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