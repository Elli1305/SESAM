<template>
  <q-dialog ref="dialog" @hide="onDialogHide">
    <q-card style="width: 40em">
      <q-card-section>
        <div class="text-h6">Tür zuweisen</div>
      </q-card-section>
      <q-card-section class="row justify-around no-wrap">
        <q-input style="width: 18em" filled v-model="doorName" label="Türname" stack-label/>
        <q-select
            style="width: 18em"
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
            @filter="filterFn">
          <template v-slot:no-option>
            <q-item>
              <q-item-section class="text-grey">
                No results
              </q-item-section>
            </q-item>
          </template>
        </q-select>
      </q-card-section>
      <q-card-section>
        <q-card bordered flat>
          <q-toolbar class="bg-primary text-accent">
            <q-toolbar-title>Konfigurationsgruppen</q-toolbar-title>
            <q-icon class="q-mr-xs" color="accent" size="1.25em" name="info_outlined">
              <q-tooltip class="bg-grey-14" anchor="bottom middle" self="top middle" :offset="[0,0]">
                Konfigurationsgruppen sind untereinander mit UND verknüpft
              </q-tooltip>
            </q-icon>
          </q-toolbar>
          <q-card-section>
            <q-input filled v-model="configDescription" label="Beschreibung der Konfiguration" stack-label/>
          </q-card-section>
          <q-card-section v-for="(select,i) in qSelects.configParts">
            <q-card bordered flat>
              <q-toolbar class="bg-primary text-white shadow-2">
                <q-toolbar-title>Konfiguration</q-toolbar-title>
                <q-btn flat round icon="delete" size="0.75em" @click="removeConfigGroup(i)"/>
              </q-toolbar>
              <q-card-section class="column">
                <q-select
                    class="q-mb-sm"
                    filled
                    multiple
                    label="Credentials"
                    option-label="name"
                    hint="Credentials in dieser Auswahl sind ODER-Verknüpft"
                    :options="credentialStore.allCredentials"
                    v-model="qSelects.configParts[i].credentials"
                    use-chips>
                  <template v-slot:selected-item="scope">
                    <q-chip
                        class="q-pa-sm"
                        style="line-height: 1"
                        :label="scope.opt.name"
                        :tabindex="scope.tabindex"
                        dense
                        removable
                        icon-remove="clear"
                        @remove="scope.removeAtIndex(scope.index)"/>
                  </template>
                  <template v-slot:no-option>
                    <q-item>
                      <q-item-section class="text-grey">
                        No results
                      </q-item-section>
                    </q-item>
                  </template>
                </q-select>
                <div class="row q-mt-sm justify-around items-center no-wrap"
                     v-for="(attributeFilter,j) in qSelects.configParts[i].attributeFilter" style="min-width: 100%">
                  <q-select style="width: 12em"
                      rounded outlined dropdown-icon="expand_more" v-model="qSelects.configParts[i].attributeFilter[j].attribute"
                      :options="commonAttributeFilter(qSelects.configParts[i].credentials)"
                      :display-value="qSelects.configParts[i].attributeFilter[j].attribute?.label"
                      @update:model-value="resetPredicateType(i,j)">

                  </q-select>
                  <q-select style="width: 5em"
                      rounded outlined dropdown-icon="expand_more" v-model="qSelects.configParts[i].attributeFilter[j].predicateType" ref="predicateType"
                      :options="getPredicates(qSelects.configParts[i].attributeFilter[j].attribute)"/>
                  <q-input style="width: 10em"
                      rounded outlined v-model="qSelects.configParts[i].attributeFilter[j].value"
                     :type="getType(qSelects.configParts[i].attributeFilter[j].attribute)"
                     :disable="qSelects.configParts[i].attributeFilter[j].currentDate" ref="input">
                  </q-input>
                  <q-btn style="width: 3em; height: 3em"
                      flat round icon="delete" @click="removeFilter(i,j)"/>
                </div>
              </q-card-section>
              <q-btn class="q-ml-sm q-mb-sm" flat dense rounded color="primary" icon="add" @click="addAttributeFilter(i)">Attribut hinzufügen</q-btn>
            </q-card>
          </q-card-section>
          <q-btn class="q-ml-sm q-mb-sm" flat dense rounded color="primary" icon="add" @click="addConfigurationGroup">
            Konfigurationsgruppe hinzufügen
          </q-btn>
        </q-card>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn flat color="primary" label="Abbrechen" @click="onCancelClick"/>
        <q-btn flat color="primary" label="Speichern" :disable="!doorName || (!room && !door)" @click="onOKClick"/>
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
      console.log(props.doorConfig)
      configDescription.value = props.doorConfig.description
      qSelects.value = props.doorConfig
      console.log(qSelects.value)
      console.log(qSelects.value.configParts)
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