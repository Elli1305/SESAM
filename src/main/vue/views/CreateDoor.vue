<template>
  <q-dialog ref="dialog" @hide="onDialogHide">
    <q-card class="q-dialog-plugin" style="width: 80em; max-width: fit-content">
      <q-card-section class="q-pa-md">
        <div class="text-h3">Tür zuweisen</div>
        <div class="q-mt-md">
          <q-input filled v-model="doorName" label="Türname" stack-label
                   style="width: 250px; padding-bottom: 32px"/>
        </div>
        <div class="q-mt-md">
          <q-select
              filled
              v-model="room"
              use-input
              hide-selected
              label="Raum auswählen"
              option-label="name"
              fill-input
              input-debounce="0"
              :options="rooms"
              v-if="!door"
              @filter="filterFn"
              style="width: 250px; padding-bottom: 32px"
          >
            <template v-slot:no-option>
              <q-item>
                <q-item-section class="text-grey">
                  No results
                </q-item-section>
              </q-item>
            </template>
          </q-select>
        </div>
        <div class="q-gutter-sm">

          <q-card bordered flat style="min-width: 60em">
            <q-toolbar class="bg-primary text-white shadow-2" style="margin-bottom: 1em">
              <q-toolbar-title>Konfigurationsgruppen</q-toolbar-title>
              <q-icon class="q-mr-xs" color="white" size="2em" name="info"/>
              <q-tooltip class="bg-grey-14" anchor="center middle" self="center middle" style="font-size: medium">
                Konfigurationsgruppen sind untereinander mit UND verknüpft
              </q-tooltip>
            </q-toolbar>
            <q-card-section>
              <q-input filled v-model="configDescription" label="Beschreibung der Konfiguration" stack-label
                       style="min-width: 100%"/>
            </q-card-section>
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
                    <div style="width: 40%">
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
                    <div style="width: 40%">
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

      </q-card-section>
      <q-card-actions align="right">
        <q-btn color="primary" label="Abbrechen" @click="onCancelClick"/>
        <q-btn color="primary" label="Speichern" :disable="!doorName || (!room && !door)" @click="onOKClick"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
import {ref} from "vue";
import {useCredentialStore} from "@/main/vue/stores/credential";
import {PredicateType} from "@/main/vue/entity/doorConfiguration";

export default {
  props: {
    rooms: Array,
    door: {
      required: false
    },
    doorConfig: {
      required: false
    }
  },

  emits: [
    'ok', 'hide'
  ],

  methods: {
    show() {
      this.$refs.dialog.show()
    },
    hide() {
      this.$refs.dialog.hide()
    },
    onDialogHide() {
      this.$emit('hide')
    },

    onOKClick() {
      this.$emit('ok', {
        room: this.room,
        doorName: this.doorName,
        configuration: this.qSelects,
        configDescription: this.configDescription
      })
      this.hide()
    },
    onCancelClick() {
      this.hide()
    },
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
    const room = ref(null)
    const doorName = ref('')
    const roomOptions = ref(props.rooms)
    const credentialStore = useCredentialStore()
    credentialStore.getAllCredentials()
    const credentials = ref()
    const configDescription = ref()

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

    if (props.door) {
      doorName.value = props.door.name
    }

    if (props.doorConfig) {
      configDescription.value = props.doorConfig
    }

    const filterFn = function (val, update, abort) {
      update(() => {
        const needle = val.toLowerCase()
        roomOptions.value = props.rooms.filter(room => room.name.toLowerCase().indexOf(needle) > -1)
      })
    }

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
      filterFn,
      doorName,
      credentialStore,
      qSelects,
      configDescription,
      commonAttributeFilter
    }
  }
}
</script>