<template>
  <q-page class="column justify-evenly items-center" style="padding: 2em 5em">
    <q-stepper v-model="step" ref="stepper" color="primary" animated flat style="width: 80vw">

      <q-step :name="1" class="row justify-center" :title="t('issuer.issueCredential.steps.form')" icon="description"
              :done="step > 1">
        <div class="row justify-around no-wrap" style="height: 45vh">
          <div class="column no-wrap" style="width: 40%">
            <p class="row text-h4">{{ t('issuer.issueCredential.title') }}</p>
            <p class="row text-h6 text-bold">{{ credential?.name }}</p>
            <p>{{ t('issuer.issueCredential.description[0]', [credential?.name]) }}</p>
            <p>{{ t('issuer.issueCredential.description[1]') }}</p>
          </div>
          <q-form class="column no-wrap" style="width: 40%" ref="form" @submit.prevent>
            <q-input class="q-my-md no-padding" outlined v-for="attribute in credential?.form" v-model="attribute.value"
                     :label="attribute.label" :type="attribute.type" :rules="getRules(attribute.validationRules)"/>
          </q-form>
        </div>
      </q-step>

      <q-step :name="2" class="row justify-center" :title="t('issuer.issueCredential.steps.list')" icon="checklist"
              :done="step > 2">
        <div class="row justify-around no-wrap" style="height: 45vh">
          <div class="column no-wrap" style="width: 40%">
            <q-input class="q-my-sm no-padding" outlined v-for="attribute in credential?.form" v-model="attribute.value"
                     :label="attribute.label" :type="attribute.type" :rules="getRules(attribute.validationRules)"/>
          </div>
          <div class="column q-mt-sm no-wrap" style="width: 40%">
            <p>{{ t('issuer.issueCredential.checkConditions') }}</p>
            <q-option-group class="q-gutter-md q-ma-sm" :options="conditions" type="checkbox"
                            v-model="selectedConditions"/>
          </div>

        </div>
      </q-step>

      <q-step :name="3" class="row justify-center" :title="t('issuer.issueCredential.steps.qrcode')" icon="qr_code_scanner">
        <div class="row justify-around no-wrap">
          <div class="column no-wrap" style="width: 60%; height: 45vh">
            <p class="q-mb-xs text-h5">{{ t('issuer.issueCredential.addCredential.title') }}</p>
            <span class="q-mb-lg sub-title text-grey">{{ credential?.name }}</span>
            <p class="q-mb-xs">{{ t('issuer.issueCredential.addCredential.howTo') }}</p>
            <ol class="q-gutter-xs">
              <i18n-t keypath="issueCredential.addCredential.steps.step1" tag="li">
                <a href="https://www2.gov.bc.ca/gov/content/governments/government-id/bc-wallet" target="_blank">
                  BC Wallet App
                </a>
              </i18n-t>
              <li>{{ t('issuer.issueCredential.addCredential.steps.step2') }}</li>
              <li>{{ t('issuer.issueCredential.addCredential.steps.step3') }}</li>
              <li>{{ t('issuer.issueCredential.addCredential.steps.step4') }}</li>
              <li>{{ t('issuer.issueCredential.addCredential.steps.step5') }}</li>
              <li>{{ t('issuer.issueCredential.addCredential.steps.step6') }}</li>
              <li>{{ t('issuer.issueCredential.addCredential.steps.step7') }}</li>
              <li>{{ t('issuer.issueCredential.addCredential.steps.step8') }}</li>
            </ol>
          </div>
          <div class="column justify-center no-wrap" style="padding-left: 2em">
            <QRCode class="q-pa-sm qr-border" style="width: 20vw; height: 20vw" :value="oobUrl" :size="300"/>
          </div>
        </div>
      </q-step>

      <template style="width: 40vw" v-slot:navigation>
        <q-stepper-navigation class="row q-mt-md justify-end">
          <q-btn v-if="step > 1 && step < 3" flat rounded color="primary" @click="previous($refs)"
                 :label="t('issuer.issueCredential.previous')" class="q-ml-sm"/>
          <q-btn v-if="step < 3" @click="next($refs)" flat rounded color="primary" :label="t('issuer.issueCredential.next')"
                 :disable="checklistIncomplete">
            <q-tooltip v-if="checklistIncomplete">
              {{ t('issuer.issueCredential.checklistHint') }}
            </q-tooltip>
          </q-btn>
        </q-stepper-navigation>
      </template>
    </q-stepper>
  </q-page>
</template>

<script setup lang="ts">
import {QForm, QNotifyCreateOptions, QOptionGroupProps, QStepper, useQuasar, ValidationRule} from 'quasar';
import {computed, ComputedRef, Ref, ref} from 'vue';
import {useI18n} from 'vue-i18n';
import {AxiosError, AxiosResponse} from "axios";
import api from '@/main/vue/api';
import {
  ComparisonRule,
  IssueCredential,
  LengthRule,
  RangeRule,
  RegExRule
} from "@/main/vue/entity/credentialDefinition";
import QRCode from 'qrcode.vue';

const props = defineProps<{ id: string }>();

const $q = useQuasar();
const {t} = useI18n();

const step = ref(1);
const credential: Ref<IssueCredential | null> = ref(null);
const form: Ref<QForm | null> = ref(null);

const selectedConditions: Ref<NonNullable<QOptionGroupProps['options']>> = ref([]);
const conditions: ComputedRef<QOptionGroupProps['options']> = computed(() => credential.value?.checklist.map(v => ({
  ...v,
  value: v.id
})) ?? []);
const checklistIncomplete: ComputedRef<boolean> = computed(() => step.value === 2 && selectedConditions.value.length !== conditions.value?.length)

const oobUrl: Ref<string | undefined> = ref(undefined);

const opts: QNotifyCreateOptions = {
  type: 'negative',
  position: 'bottom',
  timeout: 6000,
};

function getRules(validationRules: (ComparisonRule | RangeRule | RegExRule | LengthRule)[]): ValidationRule[] {
  let rules: ValidationRule[] = []
  rules.push(required)
  validationRules.forEach((vr: ComparisonRule | RangeRule | RegExRule | LengthRule) => {
    switch (vr.kind) {
      case 'comparison':
        switch (vr.comparisonType) {
          case "EQUAL":
            rules.push((value) => value === vr.content || t('issuer.issueCredential.validation.equal', [vr.content]))
            break
          case "NOT_EQUAL":
            rules.push((value) => value !== vr.content || t('issuer.issueCredential.validation.notEqual', [vr.content]))
            break
          case "LESS_THAN":
            rules.push((value) => value < vr.content || t('issuer.issueCredential.validation.lessThan', [vr.content]))
            break
          case "GREATER_THAN":
            rules.push((value) => value > vr.content || t('issuer.issueCredential.validation.greaterThan', [vr.content]))
            break
          case "LESS_EQUAL":
            rules.push((value) => value <= vr.content || t('issuer.issueCredential.validation.lessEqual', [vr.content]))
            break
          case "GREATER_EQUAL":
            rules.push((value) => value >= vr.content || t('issuer.issueCredential.validation.greaterEqual', [vr.content]))
            break
          default:
            console.error("Wrong comparison type")
        }
        break
      case 'range':
        rules.push((value) => value >= vr.valueFrom && value <= vr.valueTo || t('issuer.issueCredential.validation.range', [vr.valueFrom, vr.valueTo]))
        break
      case 'regEx':
        rules.push((value) => new RegExp(vr.regEx).test(value) || vr.description)
        break
      case 'length':
        switch (vr.comparisonType) {
          case "EQUAL":
            rules.push((value) => value.length === vr.length || t('issuer.issueCredential.validation.equalLength', [vr.length]))
            break
          case "NOT_EQUAL":
            rules.push((value) => value.length !== vr.length || t('issuer.issueCredential.validation.notEqualLength', [vr.length]))
            break
          case "LESS_THAN":
            rules.push((value) => value.length < vr.length || t('issuer.issueCredential.validation.lessThanLength', [vr.length]))
            break
          case "GREATER_THAN":
            rules.push((value) => value.length > vr.length || t('issuer.issueCredential.validation.greaterThanLength', [vr.length]))
            break
          case "LESS_EQUAL":
            rules.push((value) => value.length <= vr.length || t('issuer.issueCredential.validation.lessEqualLength', [vr.length]))
            break
          case "GREATER_EQUAL":
            rules.push((value) => value.length >= vr.length || t('issuer.issueCredential.validation.greaterEqualLength', [vr.length]))
            break
          default:
            console.error("Wrong comparison type")
        }
        break
      default:
        console.error("Wrong rule type")
    }
  })
  return rules
}

api.credential.get(props.id)
    .then((v) => credential.value = {...v.data, form: v.data.form.map(a => ({...a, value: ''}))})
    .catch((e: AxiosError) => {
      switch (e.response?.status) {
        case 403:
          $q.notify({
            ...opts,
            message: t('issuer.issueCredential.errors.get.failed'),
            caption: t('issuer.issueCredential.errors.get.unauthorized'),
          })

          break;

        default:
          console.error(e);

          $q.notify({
            ...opts,
            message: t('issuer.issueCredential.errors.get.failed'),
            caption: t('issuer.issueCredential.errors.unknown'),
          })
      }
    });

const required: ValidationRule<string> = (value) => !!value || t('issuer.issueCredential.validation.inputRequired');

const next = async (refs: any) => {
  const stepper = (refs.stepper as QStepper);

  if (step.value === 1) {
    if (!form.value || !(await form.value.validate())) {
      return;
    }

    stepper.next();
  } else if (step.value === 2) {
    $q.dialog({
      title: t('issuer.issueCredential.confirm.title'),
      message: t('issuer.issueCredential.confirm.message', {'name': credential.value?.name}),
      ok: t('issuer.issueCredential.confirm.ok'),
      cancel: t('issuer.issueCredential.confirm.cancel'),
    }).onOk(async () => {
      $q.loading.show({delay: 400});

      api.credential.issue(props.id, credential.value!.form.map(a => ({id: a.id, value: a.value})))
          .then((r: AxiosResponse<string>) => {
            oobUrl.value = r.data;
            stepper.next();
          })
          .catch((e: AxiosError) => {
            switch (e.response?.status) {
              case 403:
                $q.notify({
                  ...opts,
                  message: t('issuer.issueCredential.errors.issue.failed'),
                  caption: t('issuer.issueCredential.errors.issue.unauthorized'),
                })

                break;
              case 424:
                $q.notify({
                  ...opts,
                  message: t('issuer.issueCredential.errors.issue.failed'),
                  caption: t('issuer.issueCredential.errors.issue.failedDependency'),
                })

                break;

              default:
                console.error(e);

                $q.notify({
                  ...opts,
                  message: t('issuer.issueCredential.errors.issue.failed'),
                  caption: t('issuer.issueCredential.errors.unknown'),
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