<template>
  <q-dialog ref="dialog">
    <q-card style="min-width: 45em">
      <q-card-section class="row justify-between no-wrap">
        <div class="text-h6">{{'Validierungsregeln für ' + attribute.name}}</div>
        <div class="row no-wrap">
          <q-select label="Presets" dense outlined style="width: 12em"
                    v-model="test"
                    :options="[
                        'in der Zukunft',
                        'in der Vergangenheit',
                        'in der Zukunft (inkl. heute)',
                        'in der Vergangenheit (inkl. heute)'
                        ]"/>
          <q-icon
              class="q-ml-md cursor-pointer"
              v-close-popup
              name="clear"
              color="grey"
              size="1.5em"/>
        </div>
      </q-card-section>
      <q-card-section>
        <q-card bordered flat>
          <q-card-section class="column">
            <div class="row q-mt-sm"
                 v-for="(vr, index) in attribute.validationRules" style="min-width: 100%">
              <div class="row no-wrap justify-between full-width" v-if="vr.kind === 'comparison'">
                <q-select
                    v-model="vr.comparisonType"
                    :display-value="predicateTypeToString(vr.comparisonType)"
                    :options = "['EQUAL', 'NOT_EQUAL', 'LESS_THAN', 'GREATER_THAN', 'LESS_EQUAL', 'GREATER_EQUAL']"
                    hide-dropdown-icon rounded outlined
                    class="q-mr-lg"
                    style="min-width: 4em; height: 4em">
                  <template v-slot:option="scope">
                    <q-item v-bind="scope.itemProps">
                      {{predicateTypeToString(scope.opt)}}
                    </q-item>
                  </template>
                </q-select>
                <q-input class="full-width"
                         v-model="vr.content"
                         :type="attribute.type"
                         bottom-slots rounded outlined ref="input"
                         :disable="vr.currentDay">
                  <template v-slot:hint>
                    <q-checkbox
                        :label="t('floorplan.currentTime')"
                        v-if="attribute.type.toLowerCase() === 'date'"
                        v-model="vr.currentDay"
                        @update:model-value="vr.content = new Date().toISOString().split('T')[0]"
                        dense
                        size="2em">
                    </q-checkbox>
                  </template>
                </q-input>
                <q-btn
                    class="q-ml-lg"
                    style="min-width: 4em; height: 4em"
                    flat round icon="delete"
                    @click="deleteValRule(attribute, index)"/>
              </div>
              <div class="row no-wrap justify-between full-width" v-if="vr.kind === 'range'">
                  <q-input class="full-width"
                           v-model="vr.valueFrom"
                           :type="attribute.type"
                           bottom-slots rounded outlined ref="input">
                  </q-input>
                  <div class="column justify-center q-mx-md" style="height: 4em">
                    <span style="font-size: 2em; color: grey">&#8210</span>
                  </div>
                  <q-input class="full-width"
                           v-model="vr.valueTo"
                           :rules="[rTo => attribute.type.toLowerCase() === 'date' ? Date.parse(vr.valueFrom) < Date.parse(rTo) : vr.valueFrom < rTo || 'Second ' + attribute.type.toLowerCase() + ' must be greater than first ' + attribute.type.toLowerCase()]"
                           :type="attribute.type"
                           bottom-slots rounded outlined ref="input">
                  </q-input>

                <q-btn
                    class="q-ml-lg"
                    style="min-width: 4em; height: 4em"
                    flat round icon="delete"
                    @click="deleteValRule(attribute, index)"/>
              </div>
              <div class="row no-wrap justify-between full-width" v-if="vr.kind === 'regEx'">
                  <q-input class="full-width"
                           label="RegEx"
                           stack-label
                           v-model="vr.regEx"
                           :type="attribute.type"
                           bottom-slots rounded outlined ref="input">
                  </q-input>
                <q-btn
                    class="q-ml-lg"
                    style="min-width: 4em; height: 4em"
                    flat round icon="delete"
                    @click="deleteValRule(attribute, index)"/>
              </div>
              <div class="row no-wrap justify-between full-width" v-if="vr.kind === 'length'">
                  <q-select
                      v-model="vr.comparisonType"
                      :display-value="predicateTypeToString(vr.comparisonType)"
                      :options = "['EQUAL', 'NOT_EQUAL', 'LESS_THAN', 'GREATER_THAN', 'LESS_EQUAL', 'GREATER_EQUAL']"
                      hide-dropdown-icon rounded outlined
                      class="q-mr-lg"
                      style="min-width: 4em; height: 4em">
                    <template v-slot:option="scope">
                      <q-item v-bind="scope.itemProps">
                        {{predicateTypeToString(scope.opt)}}
                      </q-item>
                    </template>
                  </q-select>
                  <q-input class="full-width"
                           label="Länge"
                           stack-label
                           v-model="vr.length"
                           type="number"
                           bottom-slots rounded outlined ref="input">
                  </q-input>
                <q-btn
                    class="q-ml-lg"
                    style="min-width: 4em; height: 4em"
                    flat round icon="delete"
                    @click="deleteValRule(attribute, index)"/>
              </div>
            </div>
          </q-card-section>
          <div class="row no-wrap q-ml-sm q-mb-sm">
            <q-select dense outlined style="width: 12em"
                      v-model="valRuleType"
                      :options="getValRuleTypes()"/>
            <q-btn class="q-ml-sm" flat dense rounded color="primary" icon="add"
                   @click="addValidationrule(attribute)">
              {{t('issueCredential.validation.addValidationRule')}}
            </q-btn>
          </div>
        </q-card>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script lang="ts">
import {useI18n} from "vue-i18n";
import {ref} from "vue";
import {
  ComparisonRule,
  CreateAttribute,
  LengthRule,
  RangeRule,
  RegExRule
} from "@/main/vue/entity/credentialDefinition";

export default {
  name: "ValidateCredentials",
  props: {
    attribute: null
  },
  setup() {
    const {t} = useI18n()
    const predicateType = ref('<')
    const predicateTypes = ['<', '>', '<=', '>=', '==', '!=']
    const comparisonContent = ref('')
    const rangeFrom = ref('')
    const rangeTo = ref('')
    const regEx = ref('')
    const length = ref(0)
    const valRuleType = ref('comparison')
    const test = ref('')

    function addValidationrule(attribute: CreateAttribute) {
      switch (valRuleType.value) {
        case 'comparison':
          attribute.validationRules.push({kind: 'comparison', comparisonType: 'EQUAL', content: '', currentDay: false})
          break
        case 'range':
          attribute.validationRules.push({kind: 'range', valueFrom: '', valueTo: ''})
          break
        case 'regEx':
          attribute.validationRules.push({kind: 'regEx', regEx: '', description: ''})
          break
        case 'length':
          attribute.validationRules.push({kind: 'length', comparisonType: 'EQUAL', length: 0})
          break
        default:
          console.error('Wrong validation rule type')
      }
    }

    function deleteValRule(attribute: CreateAttribute, index: number) {
      attribute.validationRules.splice(index, 1)
    }

    function getValRuleTypes() {
      let valRuleTypes: string[] = []
      valRuleTypes.push('comparison')
      valRuleTypes.push('range')
      valRuleTypes.push('regEx')
      valRuleTypes.push('length')
      return valRuleTypes
    }

    function predicateTypeToString(predicateType: string) {
      switch (predicateType) {
        case 'EQUAL':
          return '=='
        case 'NOT_EQUAL':
          return '!='
        case 'LESS_THAN':
          return '<'
        case 'GREATER_THAN':
          return '>'
        case 'LESS_EQUAL':
          return '<='
        case 'GREATER_EQUAL':
          return '>='
        default:
          console.error('Wrong predicate type')
      }
    }

    return {
      t,
      predicateType,
      predicateTypes,
      comparisonContent,
      rangeFrom,
      rangeTo,
      regEx,
      length,
      addValidationrule,
      predicateTypeToString,
      getValRuleTypes,
      valRuleType,
      deleteValRule,
      test
    }
  },
  methods: {
    onOK() {

    }
  }
}
</script>

<style scoped>

</style>