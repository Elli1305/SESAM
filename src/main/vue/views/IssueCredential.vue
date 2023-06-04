<template>
  <q-page class="column justify-evenly items-center" style="padding: 2em 5em" >
      <q-stepper v-model="step" ref="stepper" color="primary" animated flat style="width: 80vw">

        <q-step :name="1" class="row justify-center" :title="t('issueCredential.steps.form')" icon="description" :done="step > 1">
          <div class="row justify-around no-wrap" style="height: 25em">
            <div class="column no-wrap" style="width: 40%">
              <p class="row text-h4">{{t('issueCredential.title')}}</p>
              <p class="row text-h6 text-bold">{{ credential?.name }}</p>
              <p>{{ t('issueCredential.description[0]', [credential?.name]) }}</p>
              <p>{{ t('issueCredential.description[1]') }}</p>
            </div>
            <q-form class="column no-wrap" style="width: 40%" ref="form" @submit.prevent>
              <q-input class="q-my-sm no-padding" outlined v-for="attribute in credential?.form" v-model="attribute.value"
                :label="attribute.label" :type="attribute.type" :rules="[required]" />
            </q-form>
          </div>
        </q-step>

        <q-step :name="2" class="row justify-center" :title="t('issueCredential.steps.list')" icon="checklist" :done="step > 2">
          <div class="row justify-around no-wrap" style="height: 25em">
            <div class="column no-wrap" style="width: 40%">
              <q-input class="q-my-sm no-padding" outlined v-for="attribute in credential?.form" v-model="attribute.value"
                :label="attribute.label" :type="attribute.type" readonly />
            </div>
            <div class="column q-mt-sm no-wrap" style="width: 40%">
              <p>{{ t('issueCredential.checkConditions') }}</p>
              <q-option-group class="q-gutter-md q-ma-sm" :options="conditions" type="checkbox" v-model="selectedConditions" />
            </div>

          </div>
        </q-step>

        <q-step :name="3" :title="t('issueCredential.steps.qrcode')" icon="qr_code_scanner">
          <i18n-t keypath="issueCredential.addCredential.title" tag="h5" class="q-ma-none q-pb-md q-mb-sm">
            <span>{{ credential?.name }}</span>
          </i18n-t>

          <div class="row q-col-gutter-xl">
            <div class="col-12 col-md-8">
              <p>{{ t('issueCredential.addCredential.howTo') }}</p>
              <ol>
                <i18n-t keypath="issueCredential.addCredential.steps.step1" tag="li">
                  <a href="https://www2.gov.bc.ca/gov/content/governments/government-id/bc-wallet" target="_blank">BC Wallet
                    App</a>
                </i18n-t>
                <li>{{ t('issueCredential.addCredential.steps.step2') }}</li>
                <li>{{ t('issueCredential.addCredential.steps.step3') }}</li>
                <li>{{ t('issueCredential.addCredential.steps.step4') }}</li>
                <li>{{ t('issueCredential.addCredential.steps.step5') }}</li>
                <li>{{ t('issueCredential.addCredential.steps.step6') }}</li>
                <li>{{ t('issueCredential.addCredential.steps.step7') }}</li>
                <li>{{ t('issueCredential.addCredential.steps.step8') }}</li>
              </ol>
            </div>
            <div class="col-12 col-md-4" style="justify-content: center; text-align: center;">
              <QRCode class="q-ma-md q-pa-sm qr-border" :value="oobUrl" :size="300" />
            </div>
          </div>
        </q-step>

        <template style="width: 40vw" v-slot:navigation>
          <q-stepper-navigation class="row q-mt-md justify-end">
            <q-btn v-if="step > 1 && step < 3" flat rounded color="primary" @click="previous($refs)"
                   :label="t('issueCredential.previous')" class="q-ml-sm" />
            <q-btn v-if="step < 3" @click="next($refs)" flat rounded color="primary" :label="t('issueCredential.next')"
                   :disable="checklistIncomplete">
              <q-tooltip v-if="checklistIncomplete">
                {{ t('issueCredential.checklistHint') }}
              </q-tooltip>
            </q-btn>
            </q-stepper-navigation>
        </template>
      </q-stepper>
  </q-page>
</template>

<script setup lang="ts">
import { QForm, QNotifyCreateOptions, QOptionGroupProps, QStepper, ValidationRule, useQuasar } from 'quasar';
import { computed, ComputedRef, Ref, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { AxiosError, AxiosResponse } from "axios";
import api from '@/main/vue/api';
import {IssueCredential} from "@/main/vue/entity/credentialDefinition";

const props = defineProps<{ id: string }>();

const $q = useQuasar();
const { t } = useI18n();

const step = ref(1);
const credential: Ref<IssueCredential | null> = ref(null);
const form: Ref<QForm | null> = ref(null);

const selectedConditions: Ref<NonNullable<QOptionGroupProps['options']>> = ref([]);
const conditions: ComputedRef<QOptionGroupProps['options']> = computed(() => credential.value?.checklist.map(v => ({ ...v, value: v.id })) ?? []);
const checklistIncomplete: ComputedRef<boolean> = computed(() => step.value === 2 && selectedConditions.value.length !== conditions.value?.length)

const oobUrl: Ref<string | undefined> = ref(undefined);

const opts: QNotifyCreateOptions = {
  type: 'negative',
  position: 'bottom',
  timeout: 6000,
};

api.credential.get(props.id)
  .then((v) => credential.value = { ...v.data, form: v.data.form.map(a => ({ ...a, value: '' })) })
  .catch((e: AxiosError) => {
    switch (e.response?.status) {
      case 403:
        $q.notify({
          ...opts,
          message: t('issueCredential.errors.get.failed'),
          caption: t('issueCredential.errors.get.unauthorized'),
        })

        break;

      default:
        console.error(e);

        $q.notify({
          ...opts,
          message: t('issueCredential.errors.get.failed'),
          caption: t('issueCredential.errors.unknown'),
        })
    }
  });

const required: ValidationRule<string> = (value) => !!value || t('issueCredential.validation.inputRequired');

const next = async (refs: any) => {
  const stepper = (refs.stepper as QStepper);

  if (step.value === 1) {
    if (!form.value || !(await form.value.validate())) {
      return;
    }

    stepper.next();
  } else if (step.value === 2) {
    $q.dialog({
      title: t('issueCredential.confirm.title'),
      message: t('issueCredential.confirm.message', { 'name': credential.value?.name }),
      ok: t('issueCredential.confirm.ok'),
      cancel: t('issueCredential.confirm.cancel'),
    }).onOk(async () => {
      $q.loading.show({ delay: 400 });

      api.credential.issue(props.id, credential.value!.form.map(a => ({ id: a.id, value: a.value })))
        .then((r: AxiosResponse<string>) => {
          oobUrl.value = r.data;
          stepper.next();
        })
        .catch((e: AxiosError) => {
          switch (e.response?.status) {
            case 403:
              $q.notify({
                ...opts,
                message: t('issueCredential.errors.issue.failed'),
                caption: t('issueCredential.errors.issue.unauthorized'),
              })

              break;
            case 424:
              $q.notify({
                ...opts,
                message: t('issueCredential.errors.issue.failed'),
                caption: t('issueCredential.errors.issue.failedDependency'),
              })

              break;

            default:
              console.error(e);

              $q.notify({
                ...opts,
                message: t('issueCredential.errors.issue.failed'),
                caption: t('issueCredential.errors.unknown'),
              })
          }
        })
        .finally(() => $q.loading.hide());
    });
  }
};

const previous = (refs: any) => (refs.stepper as QStepper).previous();
</script>

<style scoped>
.qr-border {
  border: 4px dashed rgba(0, 0, 0, 0.1);
  border-radius: .75rem;
}
</style>