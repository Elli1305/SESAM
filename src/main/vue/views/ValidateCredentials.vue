<template>
  <q-dialog ref="dialog" persistent>
    <q-card style="min-width: 45em">
      <q-card-section class="row justify-between no-wrap">
        <div class="text-h6">{{'Validierungsregeln für ' + attribute.name}}</div>
        <q-select label="Presets" dense outlined style="width: 12em"
                  :options="[
                      'in der Zukunft',
                      'in der Vergangenheit',
                      'in der Zukunft (inkl. heute)',
                      'in der Vergangenheit (inkl. heute)'
                      ]"/>
      </q-card-section>
      <q-card-section class="column q-px-lg justify-between no-wrap">
        <card class="row q-mt-sm justify-between no-wrap"
              v-if="attribute.type.toLowerCase() === 'date'"
             style="min-width: 100%">
          <div class="row justify-evenly" style="width: 80%">
            <q-btn @click="changePredicateType" color="grey" round outline style="width: 4em; height: 4em">
              <span class="column justify-center fit" style="padding-top: 0.15em; font-size: 2em; font-weight: bolder">
                {{predicateType}}
              </span>
            </q-btn>
            <q-input style="width: 10em"
                     v-model="date1"
                     :type="attribute.type"
                     bottom-slots rounded outlined ref="input">
              <template v-slot:hint>
                <q-checkbox
                    :label="t('floorplan.currentTime')"
                    dense
                    size="2em">
                </q-checkbox>
              </template>
            </q-input>
          </div>
          <q-btn style="width: 4em; height: 4em"
                 flat round icon="delete"/>
        </card>
        <card class="row q-mt-sm justify-between no-wrap"
              v-if="attribute.type.toLowerCase() === 'date'"
                     style="min-width: 100%">
          <div class="row justify-center" style="width: 80%">
            <q-input style="width: 10em"
                     v-model="date2From"
                     :type="attribute.type"
                     bottom-slots rounded outlined ref="input">
            </q-input>
              <div class="column justify-center q-mx-md" style="height: 4em">
                <span style="font-size: 2em; color: grey">&#8210</span>
              </div>
            <q-input style="width: 10em"
                     v-model="date2To"
                     :rules="[d => date2From < date2To || 'Second date must be greater than first date']"
                     :type="attribute.type"
                     bottom-slots rounded outlined ref="input">
            </q-input>
          </div>

          <q-btn style="width: 4em; height: 4em"
                 flat round icon="delete"/>
        </card>
        <card class="row q-mt-sm justify-between no-wrap"
                   v-if="attribute.type.toLowerCase() === 'number'"
                   style="min-width: 100%">
          <div class="row justify-evenly" style="width: 80%">
            <q-btn @click="changePredicateType" color="grey" round outline style="width: 4em; height: 4em">
                <span class="column justify-center fit" style="padding-top: 0.15em; font-size: 2em; font-weight: bolder">
                  {{predicateType}}
                </span>
            </q-btn>
            <q-input style="width: 10em"
                     v-model="num1"
                     :type="attribute.type"
                     bottom-slots rounded outlined ref="input">
            </q-input>
          </div>
          <q-btn style="width: 4em; height: 4em"
                 flat round icon="delete"/>
        </card>
        <card class="row q-mt-sm justify-between no-wrap"
              v-if="attribute.type.toLowerCase() === 'number'"
              style="min-width: 100%">
          <div class="row justify-center" style="width: 80%">
            <q-input style="width: 10em"
                     v-model="num2From"
                     :type="attribute.type"
                     bottom-slots rounded outlined ref="input">
            </q-input>
            <div class="column justify-center q-mx-md" style="height: 4em">
              <span style="font-size: 2em; color: grey">&#8210</span>
            </div>
            <q-input style="width: 10em"
                     v-model="num2To"
                     :type="attribute.type"
                     bottom-slots rounded outlined ref="input">
            </q-input>
          </div>
          <q-btn style="width: 4em; height: 4em"
                 flat round icon="delete"/>
        </card>
        <card class="row q-mt-sm justify-between no-wrap"
              v-if="attribute.type.toLowerCase() === 'text'"
              style="min-width: 100%">
          <div class="row justify-center" style="width: 80%">
            <q-input style="width: 10em"
                     label="RegEx"
                     stack-label
                     v-model="regEx"
                     :type="attribute.type"
                     bottom-slots rounded outlined ref="input">
            </q-input>
          </div>
          <q-btn style="width: 4em; height: 4em"
                 flat round icon="delete"/>
        </card>
        <card class="row q-mt-sm justify-between no-wrap"
              v-if="attribute.type.toLowerCase() === 'text'"
              style="min-width: 100%">
          <div class="row justify-evenly" style="width: 80%">
            <q-btn @click="changePredicateType" color="grey" round outline style="width: 4em; height: 4em">
                <span class="column justify-center fit" style="padding-top: 0.15em; font-size: 2em; font-weight: bolder">
                  {{predicateType}}
                </span>
            </q-btn>
            <q-input style="width: 10em"
                     label="Länge"
                     stack-label
                     v-model="length"
                     type="number"
                     bottom-slots rounded outlined ref="input">
            </q-input>
          </div>
          <q-btn style="width: 4em; height: 4em"
                 flat round icon="delete"/>
        </card>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn flat color="primary" :label="t('common.cancel')" v-close-popup/>
        <q-btn flat color="primary" :label="t('common.save')" @click="onOK" v-close-popup/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
import {useI18n} from "vue-i18n";
import {ref} from "vue";

export default {
  name: "ValidateCredentials",
  props: {
    attribute: null
  },
  setup() {
    const {t} = useI18n()
    const predicateType = ref('<')
    const predicateTypes = ['<', '>', '<=', '>=', '==', '!=']
    const date1 = ref(new Date().toISOString().split('T')[0])
    const date2From = ref(new Date().toISOString().split('T')[0])
    const date2To = ref(new Date().toISOString().split('T')[0])
    const num1 = ref(0)
    const num2From = ref(0)
    const num2To = ref(1)
    const regEx = ref('')
    const length = ref(0)

    function changePredicateType() {
      if (predicateTypes.indexOf(predicateType.value) === predicateTypes.length -1) {
        predicateType.value = predicateTypes.at(0)
      }
      else {
        predicateType.value = predicateTypes.at(predicateTypes.indexOf(predicateType.value) + 1)
      }
    }

    return {
      t,
      predicateType,
      predicateTypes,
      changePredicateType,
      date1,
      date2From,
      date2To,
      num1,
      num2From,
      num2To,
      regEx,
      length
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