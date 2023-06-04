<template>
  <div class="q-gutter-sm">

    <q-card bordered flat style="min-width: 70em; margin: 0.5em">
      <q-toolbar class="bg-primary text-white shadow-2" style="margin-bottom: 1em">
        <q-toolbar-title>Konfigurationsgruppen</q-toolbar-title>
        <q-icon class="q-mr-xs" color="white" size="2em" name="info" />
        <q-tooltip class="bg-grey-14" anchor="center middle" self="center middle" style="font-size: medium">
          Konfigurationsgruppen sind untereinander mit AND verknüpft
        </q-tooltip>
      </q-toolbar>
      <div v-for="(select,i) in qSelects.configParts">

        <q-separator v-if="i !== 0"/>
        <q-card-section>
          <div class="row q-gutter-sm">
            <q-toolbar class="bg-primary text-white shadow-2" style="min-width: 100%; margin-bottom: 0.5em">
              <q-toolbar-title>Konfiguration</q-toolbar-title>
              <q-btn flat icon="delete" @click="removeConfigGroup(i)"/>
            </q-toolbar>
            <q-select
                filled
                multiple
                label="Credentials"
                option-label="name"
                hint="Credentials in dieser Auswahl sind ODER-Verknüpft"
                :options="credentialStore.allCredentials"
                v-model="qSelects.configParts[i].credentials"
                use-chips
                style="min-width: 100%"
            >
              <template v-slot:no-option>
                <q-item>
                  <q-item-section class="text-grey">
                    No results
                  </q-item-section>
                </q-item>
              </template>
            </q-select>

            <div class="q-gutter-sm row no-wrap"
                 v-for="(attributeFilter,j) in qSelects.configParts[i].attributeFilter" style="min-width: 100%">
              <div style="width: 42%">
                <q-select v-model="qSelects.configParts[i].attributeFilter[j].attribute"
                          :options="commonAttributeFilter(qSelects.configParts[i].credentials)"
                          :display-value="qSelects.configParts[i].attributeFilter[j].attribute?.label"
                          @update:model-value="resetPredicateType(i,j)">

                </q-select>
              </div>
              <div style="width: 16%">
                <q-select v-model="qSelects.configParts[i].attributeFilter[j].predicateType" ref="predicateType"
                          :options="getPredicates(qSelects.configParts[i].attributeFilter[j].attribute)"
                >

                </q-select>
              </div>
              <div style="width: 42%">
                <q-input v-model="qSelects.configParts[i].attributeFilter[j].value"
                         :type="getType(qSelects.configParts[i].attributeFilter[j].attribute)"
                         :disable="qSelects.configParts[i].attributeFilter[j].currentDate" ref="input">
                </q-input>
                <q-checkbox v-model="qSelects.configParts[i].attributeFilter[j].currentDate"
                            v-if="qSelects.configParts[i].attributeFilter[j].attribute?.type.toLowerCase() === 'date'"
                            @update:model-value="setDate(i,j)"
                >
                  Aktueller Zeitpunkt
                </q-checkbox>
              </div>
              <q-btn flat icon="delete" @click="removeFilter(i,j)"/>
            </div>
            <q-btn flat color="primary" icon="add" @click="addAttributeFilter(i)">Attribut hinzufügen</q-btn>
          </div>
        </q-card-section>
      </div>
      <q-separator color="bg-grey"/>
      <q-btn flat color="primary" icon="add" @click="addConfigurationGroup">
        Konfigurationsgruppe hinzufügen
      </q-btn>
    </q-card>
  </div>
</template>

<script>
import {PredicateType} from "@/main/vue/entity/doorConfiguration";
import {ref} from "vue";
import {useCredentialStore} from "@/main/vue/stores/credential";

export default {
  name: "DoorConfig",

  props: {
    room: {
      required: true
    },
    doorName: {
      required: true
    }
  },
  emits: [
    'ok', 'hide'
  ],

  methods: {

    getPredicates(attribute) {
      if (attribute?.type.toLowerCase() === 'text') {
        return [PredicateType.EQUALS]
      } else if (attribute?.type.toLowerCase() === 'number' || attribute?.type.toLowerCase() === 'date') {
        return Object.values(PredicateType)
      }
    },
    resetPredicateType(i, j) {
      this.qSelects.configParts[i].attributeFilter[j].predicateType = null;
      this.qSelects.configParts[i].attributeFilter[j].value = null;
    },

    getType(attribute) {
      return attribute?.type.toLowerCase()
    },
    addAttributeFilter(i) {
      this.qSelects.configParts[i].attributeFilter.push({
        attribute: null,
        predicateType: null,
        value: null,
        currentDate: false
      })
    },
    removeConfigGroup(i) {
      this.qSelects.configParts.splice(i, 1)
    },
    removeFilter(i, j) {
      this.qSelects.configParts[i].attributeFilter.splice(j, 1)
    },
    addConfigurationGroup() {
      this.qSelects.configParts.push({
        credentials: [],
        attributeFilter: [{
          attribute: null,
          predicateType: null,
          value: null,
          currentDate: false
        }]
      })
    },
    setDate(i, j) {
      if (this.qSelects.configParts[i].attributeFilter[j].currentDate) {
        const date = new Date();
        this.qSelects.configParts[i].attributeFilter[j].value = date.toISOString().split('T')[0];
      }
    }
  },
  setup(props) {
    const room = ref(props.room)
    const doorName = ref(props.doorName)
    const credentialStore = useCredentialStore()
    credentialStore.getAllCredentials()
    const credentials = ref()

    const qSelects = ref({
      configParts: [{
        credentials: [],
        attributeFilter: [{
          attribute: null,
          predicateType: null,
          value: null,
          currentDate: false
        }]
      }]
    })

    const commonAttributeFilter = function (credentials) {
      let formEntrys = credentials.map((credential) => {
        return credential.form
      })
      if (formEntrys.length > 1) {
        return formEntrys.shift().filter((v) => {
          return formEntrys.every((a) => {
            return a.some(ele => ele.attributeName === v.attributeName);
          });
        })
      }
      return formEntrys[0];
    }

    return {
      room,
      credentials,
      doorName,
      credentialStore,
      qSelects,
      commonAttributeFilter
    }
  }
}
</script>

<style scoped>

</style>