<template>
  <q-dialog ref="dialog">
    <q-card style="min-width: 45em; background-color: var(--bg-color); color: var(--text-color)">
      <q-card-section class="row justify-between no-wrap q-pb-none">
        <div class="text-h6">{{t('issuer.issueCredential.validation.validationRules')}} &#8210 {{attribute.name}}</div>
        <div class="row no-wrap">
          <q-btn
              flat rounded
              size="0.9em">
            <q-icon name="content_copy" size="1em" left/>
            {{t('issuer.issueCredential.validation.presetsLabel')}}
            <q-menu style="background-color: var(--bg-color); color: var(--text-color)">
              <q-list>
                <q-item clickable v-close-popup v-for="preset in getPresets().entries()" @click="attribute.validationRules.push(...preset[1])">
                  <q-item-section>
                    <q-item-label>{{preset[0]}}</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </q-menu>
          </q-btn>
          <q-icon
              class="q-ml-md cursor-pointer"
              v-close-popup
              name="clear"
              color="grey"
              size="1.5em"/>
        </div>
      </q-card-section>
      <q-card-section>
        <q-card class="column" style="background-color: var(--bg-color); color: var(--text-color)" bordered flat>
          <q-icon class="cursor-pointer self-end q-mt-sm q-mr-sm" size="1em" name="info_outlined" color="info">
            <q-tooltip style="background-color: var(--bg-color); color: var(--text-color); font-size: 1em" max-width="15em" anchor="center right" self="center left">
              {{t('issuer.issueCredential.validation.info')}}
            </q-tooltip>
          </q-icon>
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
                         v-if="!vr.compareWithAttribute"
                         v-model="vr.content"
                         :type="attribute.type"
                         bottom-slots rounded outlined ref="input"
                         :disable="vr.currentDay">
                  <template v-slot:hint>
                    <q-checkbox
                        :label="t('floorPlan.currentTime')"
                        v-if="attribute.type.toLowerCase() === 'date'"
                        v-model="vr.currentDay"
                        @update:model-value="vr.content = new Date().toISOString().split('T')[0]"
                        dense
                        size="2em">
                    </q-checkbox>
                  </template>
                </q-input>
                <q-select
                    class="full-width"
                    v-if="vr.compareWithAttribute"
                    :label="t('issuer.issueCredential.validation.chooseAttribute')"
                    v-model="vr.attributeName"
                    :options="getAttributes(attribute, attributes)"
                    option-label="name"
                    bottom-slots rounded outlined ref="input">
                </q-select>
                <q-toggle
                    class="q-ml-sm"
                    style="height: 4em"
                    unchecked-icon="text_fields"
                    checked-icon="grid_view"
                    v-model="vr.compareWithAttribute"
                    keep-color size="3em"/>
                <q-btn
                    class="q-ml-sm"
                    style="min-width: 4em; height: 4em"
                    flat round icon="delete"
                    @click="deleteValRule(attribute, index)"/>
              </div>


              <div class="row no-wrap justify-between full-width" v-if="vr.kind === 'range'">
                <q-input class="full-width"
                         v-if="!vr.compareWithAttribute"
                         v-model="vr.valueFrom"
                         :type="attribute.type"
                         bottom-slots rounded outlined ref="input">
                </q-input>
                <q-select
                    class="full-width"
                    v-if="vr.compareWithAttribute"
                    :label="t('issuer.issueCredential.validation.chooseAttribute')"
                    v-model="vr.attributeNameFrom"
                    :options="getAttributes(attribute, attributes)"
                    option-label="name"
                    bottom-slots rounded outlined ref="input">
                </q-select>
                <div class="column justify-center q-mx-md" style="height: 4em">
                  <span style="font-size: 2em; color: grey">&#8210</span>
                </div>
                <q-input class="full-width"
                         v-if="!vr.compareWithAttribute"
                         v-model="vr.valueTo"
                         :rules="[rTo => attribute.type.toLowerCase() === 'date' ? Date.parse(vr.valueFrom) < Date.parse(rTo) : vr.valueFrom < rTo || 'Second ' + attribute.type.toLowerCase() + ' must be greater than first ' + attribute.type.toLowerCase()]"
                         :type="attribute.type"
                         bottom-slots rounded outlined ref="input">
                </q-input>
                <q-select
                    class="full-width"
                    v-if="vr.compareWithAttribute"
                    :label="t('issuer.issueCredential.validation.chooseAttribute')"
                    v-model="vr.attributeNameTo"
                    :options="getAttributes(attribute, attributes)"
                    option-label="name"
                    bottom-slots rounded outlined ref="input">
                </q-select>
                <q-toggle
                    class="q-ml-sm"
                    style="height: 4em"
                    unchecked-icon="text_fields"
                    checked-icon="format_list_bulleted"
                    v-model="vr.compareWithAttribute"
                    keep-color size="2.5em"/>
                <q-btn
                    class="q-ml-lg"
                    style="min-width: 4em; height: 4em"
                    flat round icon="delete"
                    @click="deleteValRule(attribute, index)"/>
              </div>


              <div class="row no-wrap justify-between full-width" v-if="vr.kind === 'regEx'">
                <q-input class="full-width"
                         :label="t('issuer.issueCredential.validation.vType.regEx')"
                         stack-label
                         v-model="vr.regEx"
                         :type="attribute.type"
                         bottom-slots rounded outlined ref="input">
                </q-input>
                <q-space class="q-mx-md"/>
                <q-input class="full-width"
                         :label="t('issuer.issueCredential.validation.description')"
                         stack-label
                         v-model="vr.description"
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
                         v-if="!vr.compareWithAttribute"
                         :label="t('issuer.issueCredential.validation.vType.length')"
                         stack-label
                         v-model="vr.length"
                         type="number"
                         bottom-slots rounded outlined ref="input">
                </q-input>
                <q-select
                    class="full-width"
                    v-if="vr.compareWithAttribute"
                    :label="t('issuer.issueCredential.validation.chooseAttribute')"
                    v-model="vr.attributeName"
                    :options="getAttributes(attribute, attributes)"
                    option-label="name"
                    bottom-slots rounded outlined ref="input">
                </q-select>
                <q-toggle
                    class="q-ml-sm"
                    style="height: 4em"
                    unchecked-icon="text_fields"
                    checked-icon="format_list_bulleted"
                    v-model="vr.compareWithAttribute"
                    keep-color size="2.5em"/>
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
                      :label="t('issuer.issueCredential.validation.chooseType')"
                      v-model="valRuleType"
                      :options="getValRuleTypes(attribute)"
                      option-label="name"/>
            <q-btn class="q-ml-sm" flat dense rounded color="primary" icon="add"
                   @click="addValidationRule(attribute)">
              {{t('issuer.issueCredential.validation.addValidationRule')}}
            </q-btn>
          </div>
        </q-card>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script lang="ts">
import {useI18n} from "vue-i18n";
import {Ref, ref} from "vue";
import {
  ComparisonRule,
  LengthRule,
  RangeRule,
  RegExRule
} from "@/main/vue/entity/credentialDefinition";

export default {
  name: "ValidateCredentials",
  props: {
    attribute: ref(null),
    attributes: ref(null)
  },
  setup(props: any) {
    const {t} = useI18n()
    const predicateType = ref('<')
    const predicateTypes = ['<', '>', '<=', '>=', '==', '!=']
    const comparisonContent = ref('')
    const rangeFrom = ref('')
    const rangeTo = ref('')
    const regEx = ref('')
    const length = ref(0)
    const valRuleType: Ref<{name: string, type: string} | null> = ref(null)

    function addValidationRule(attribute: any | undefined) {
      if (valRuleType.value)
        switch (valRuleType.value.type) {
          case 'comparison':
            attribute.validationRules.push({kind: 'comparison', comparisonType: 'EQUAL', content: '', currentDay: false, compareWithAttribute: false})
            break
          case 'range':
            attribute.validationRules.push({kind: 'range', valueFrom: '', valueTo: '', compareWithAttribute: false})
            break
          case 'regEx':
            attribute.validationRules.push({kind: 'regEx', regEx: '', description: ''})
            break
          case 'length':
            attribute.validationRules.push({kind: 'length', comparisonType: 'EQUAL', length: 0, compareWithAttribute: false})
            break
          default:
            console.error('Wrong validation rule type')
        }
    }

    function deleteValRule(attribute: any | undefined, index: string | number) {
      attribute.validationRules.splice(index, 1)
    }

    function getValRuleTypes(attribute: any | undefined) {
      let valRuleTypes = []
      if (attribute.type === 'number' || attribute.type === 'date') {
        valRuleTypes.push({name: t('issuer.issueCredential.validation.vType.comparison'), type: 'comparison'})
        valRuleTypes.push({name: t('issuer.issueCredential.validation.vType.range'), type: 'range'})
      } else if (attribute.type === 'text') {
        valRuleTypes.push({name: t('issuer.issueCredential.validation.vType.regEx'), type: 'regEx'})
        valRuleTypes.push({name: t('issuer.issueCredential.validation.vType.length'), type: 'length'})
      }
      return valRuleTypes
    }

    function getAttributes(attribute: any | undefined, attributes: any | undefined) {
      let attributeList: any[] = []
      attributes.forEach( (a: { type: string; attributeName: string; name: string }) => {
        if (attribute.type === a.type && attribute.attributeName !== a.attributeName) {
          attributeList.push(a.name)
        }
      })
      return attributeList
    }

    function getPresets() {
      let presets = new Map<string, (ComparisonRule | RangeRule | RegExRule | LengthRule)[]>()
      switch (props.attribute.type) {
        case 'number':
          presets.set(t('issuer.issueCredential.validation.presets.positiveWithZero'), [{kind: "comparison", comparisonType: "GREATER_EQUAL", content: "0", currentDay: false, compareWithAttribute: false, attributeName: ""}])
          presets.set(t('issuer.issueCredential.validation.presets.positiveWithoutZero'), [{kind: "comparison", comparisonType: "GREATER_THAN", content: "0", currentDay: false, compareWithAttribute: false, attributeName: ""}])
          presets.set(t('issuer.issueCredential.validation.presets.negativeWithZero'), [{kind: "comparison", comparisonType: "LESS_EQUAL", content: "0", currentDay: false, compareWithAttribute: false, attributeName: ""}])
          presets.set(t('issuer.issueCredential.validation.presets.negativeWithoutZero'), [{kind: "comparison", comparisonType: "LESS_THAN", content: "0", currentDay: false, compareWithAttribute: false, attributeName: ""}])
          break
        case 'date':
          presets.set(t('issuer.issueCredential.validation.presets.futureWithToday'), [{kind: "comparison", comparisonType: "GREATER_EQUAL", content: "", currentDay: true, compareWithAttribute: false, attributeName: ""}])
          presets.set(t('issuer.issueCredential.validation.presets.futureWithoutToday'), [{kind: "comparison", comparisonType: "GREATER_THAN", content: "", currentDay: true, compareWithAttribute: false, attributeName: ""}])
          presets.set(t('issuer.issueCredential.validation.presets.pastWithToday'), [{kind: "comparison", comparisonType: "LESS_EQUAL", content: "", currentDay: true, compareWithAttribute: false, attributeName: ""}])
          presets.set(t('issuer.issueCredential.validation.presets.pastWithoutToday'), [{kind: "comparison", comparisonType: "LESS_THAN", content: "", currentDay: true, compareWithAttribute: false, attributeName: ""}])
          presets.set(t('issuer.issueCredential.validation.presets.today'), [{kind: "comparison", comparisonType: "EQUAL", content: "", currentDay: true, compareWithAttribute: false, attributeName: ""}])
          break
        case 'text':
          presets.set(t('issuer.issueCredential.validation.presets.names'), [{kind: "regEx", regEx: "^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+$", description: "Wähle eine realen Name / Choose a real name"}, {kind: "length", comparisonType: "LESS_THAN", length: 50, compareWithAttribute: false, attributeName: ""}])
          presets.set(t('issuer.issueCredential.validation.presets.noSpecialCharacter'), [{kind: "regEx", regEx: "^[a-zA-Z0-9]{4,10}$", description: "Darf nur Buchstaben und Nummern enthalten / Should only contain letters and numbers"}])
          presets.set(t('issuer.issueCredential.validation.presets.email'), [{kind: "regEx", regEx: "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\\])", description: "Wähle eine valide E-Mail / Choose a valid email"}])
          presets.set(t('issuer.issueCredential.validation.presets.phoneNumbers'), [{kind: "regEx", regEx: "^\\+?[1-9][0-9]{7,14}$", description: "Wählen Sie eine valide Telefonnummer / Choose a valid phone number"}])
          presets.set(t('issuer.issueCredential.validation.presets.uppercase'), [{kind: "regEx", regEx: "^[A-Z]*$", description: "Darf nur Großbuchstaben enthalten / Should only contain uppercase letters"}])
          presets.set(t('issuer.issueCredential.validation.presets.lowercase'), [{kind: "regEx", regEx: "^[a-z]*$", description: "Darf nur Kleinbuchstaben enthalten / Should only contain lowercase letters"}])
          break
        default:
          console.error('No presets exist for type ' + props.attribute.type)
      }
      return presets
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
      addValidationRule,
      predicateTypeToString,
      getValRuleTypes,
      valRuleType,
      deleteValRule,
      getAttributes,
      getPresets,
    }
  },
}
</script>

<style scoped>

</style>