<template>
    <q-page class="column justify-evenly" style="padding: 2em 5em">
        <p class="row text-h3 justify-center">{{ t("editor.predefinedConfigs.title") }}</p>
        <div class="row self-center">
            <q-table
                    style="width: 80vw; height: 50vh; background-color: var(--bg-color); color: var(--text-color)"
                    :rows-per-page-options="[0]"
                    :rows="rows"
                    :columns="columns"
                    :separator="'cell'"
                    hide-bottom
                    :no-data-label="t('common.noData')"
                    :no-results-label="t('common.noResults')"
                    :filter="filter"
                    row-key="name">
                <template v-slot:body-cell-actions="props">
                    <q-td :props="props">
                        <div class="row justify-evenly no-wrap">
                            <q-btn
                              dense
                              round
                              flat
                              style="color: var(--light)"
                              icon="edit"
                              @click="configDialog = true; editConfig(props.row.id); edit = true;
                                      currentConfig = props.row.id">
                            </q-btn>

                            <q-btn
                                dense
                                round
                                flat
                                icon="delete"
                                style="color: var(--light)"
                                @click="deleteDialog = true; currentConfig = props.row.id">
                            </q-btn>
                        </div>
                    </q-td>
                </template>

                <template class="row no-wrap" v-slot:top-right>
                    <q-btn
                       flat
                       rounded
                       icon="add"
                       :label="t('editor.predefinedConfigs.new')"
                       @click="configDialog = true; edit = false; addConfiguration()"
                       style="margin-right: 2em; color: var(--light)">
                    </q-btn>
                    <q-input
                       outlined rounded dense debounce="300" v-model="filter"
                       :placeholder="t( 'common.search')">
                        <template v-slot:append>
                            <q-icon name="search"/>
                        </template>
                    </q-input>
                </template>
            </q-table>
        </div>

        <q-dialog v-model="configDialog" persistent>
            <q-card style="background-color: var(--bg-color); color: var(--text-color)">
                <q-card-section>
                    <q-input class="full-width" filled v-model="configName" :label="t('editor.predefinedConfigs.name')" stack-label></q-input>
                </q-card-section>

              <q-card-section v-for="(config, k) in qSelectGeneral.qSelectsSet">
                <q-toolbar class="bg-primary text-accent">

                  <q-toolbar-title>Konfiguration</q-toolbar-title>
                  <q-icon class="q-mr-xs" color="accent" size="1.25em" name="info_outlined">
                    <q-tooltip max-width="15em" anchor="center right" self="center left">
                      You can only choose one base configuration.
                    </q-tooltip>
                  </q-icon>
                  <q-td v-if= !(checkLength())>
                    <q-btn flat round icon="delete" size="0.75em" @click="removeConfig(k)"/>
                  </q-td>
                </q-toolbar>


                <div class="colomn q-mt-sm justify-around items-center no-wrap">

                  <div class="q-pa-md">
                    <div class="q-gutter-sm">

                      <q-checkbox dense v-model="qSelectGeneral.qSelectsSet[k].baseConfig" label="Basis Konfiguration" color="primary" @click = "check(k)"/>

                    </div>
                  </div>
                  <q-td class="q-pl-md v-if=" v-if="!qSelectGeneral.qSelectsSet[k].baseConfig">
                    <div class="q-gutter-sm row">
                      <div class="q-gutter-sm row">
                        <q-input filled v-model="qSelectGeneral.qSelectsSet[k].startTime" mask="time" :rules="['time']" >
                          <template v-slot:append>
                            <q-icon name="access_time" class="cursor-pointer">
                              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                <q-time v-model="qSelectGeneral.qSelectsSet[k].startTime">
                                  <div class="row items-center justify-end">
                                    <q-btn v-close-popup label="Close" color="primary" flat />
                                  </div>
                                </q-time>
                              </q-popup-proxy>
                            </q-icon>
                          </template>
                        </q-input>
                      </div>
                      <div class="q-gutter-sm row">
                        <q-input filled v-model="qSelectGeneral.qSelectsSet[k].endTime" mask="time" :rules="['time']" :disabled="qSelectGeneral.qSelectsSet[k].baseConfig">
                          <template v-slot:append>
                            <q-icon name="access_time" class="cursor-pointer">
                              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                <q-time v-model="qSelectGeneral.qSelectsSet[k].endTime">
                                  <div class="row items-center justify-end">
                                    <q-btn v-close-popup label="Close" color="primary" flat />
                                  </div>
                                </q-time>
                              </q-popup-proxy>
                            </q-icon>
                          </template>
                        </q-input>
                      </div>
                    </div>
                  </q-td>
                </div>
                <DoorConfig
                    ref="configIn" :door-config="qSelectGeneral.qSelectsSet[k].doorConfigIn"
                    :direction="JSON.stringify(qSelectGeneral.qSelectsSet[k].doorConfigIn) !==
                    JSON.stringify(qSelectGeneral.qSelectsSet[k].doorConfigOut) ? Direction.IN : Direction.BOTH"
                    @changeDirection="changeDirectionOut($event, k)">
                </DoorConfig>
                <DoorConfig v-show="configIn[k]?.direction !== Direction.BOTH" ref="configOut"
                            :direction="Direction.OUT"
                            :is-config-out="true" :door-config="qSelectGeneral.qSelectsSet[k].doorConfigOut">
                </DoorConfig>

                <q-btn class="q-ml-sm q-mb-sm" flat dense rounded color="primary" icon="add" @click="addConfiguration">
                  Konfiguration hinzufügen
                </q-btn>
              </q-card-section>
              <q-card-actions align="right">
                <q-btn flat color="primary" :label="t('common.cancel')" @click="onCancelClick(); edit = false" v-close-popup></q-btn>
                <q-btn flat color="primary" :label="t('common.save')" :disable="!configName || !checkBaseConf()" @click="onOKClick()" v-close-popup></q-btn>
              </q-card-actions>
            </q-card>
        </q-dialog>


        <q-dialog v-model="deleteDialog">
            <q-card style="background-color: var(--bg-color); color: var(--text-color)">
                <q-card-section>
                    <div class="text-h6">{{ t('editor.predefinedConfigs.deleteAlert') }}</div>
                </q-card-section>
                <q-card-section class="q-pt-none">
                    {{ t('editor.predefinedConfigs.deleteQuestion') }}
                </q-card-section>
                <q-card-actions align="right" class="text-primary">
                    <q-btn flat :label="t('common.cancel')" v-close-popup/>
                    <q-btn flat :label="t('common.delete')" @click="deleteConfig(currentConfig); deleteDialog=false"/>
                </q-card-actions>
            </q-card>
        </q-dialog>
    </q-page>
</template>

<script setup lang="ts">
import {computed, Ref, ref} from "vue";
import {useI18n} from "vue-i18n";
import {PredefinedConfiguration} from "@/main/vue/entity/predefinedConfiguration";
import DoorConfig from "@/main/vue/views/DoorConfig.vue";
import {Direction} from "@/main/vue/entity/doorConfiguration";
import {useConfigStore} from "@/main/vue/stores/config";
import {QTableProps, useQuasar} from "quasar";

const direction = computed(() => {
    return Direction
})
const $q = useQuasar()
const edit = ref(false)
const configStore = useConfigStore()
const configName = ref()
const configDialog = ref(false)
const deleteDialog = ref(false)
const filter = ref('')
const {t} = useI18n()
const columns: QTableProps['columns'] = [
    {
        name: 'name',
        required: true,
        label: t('editor.predefinedConfigs.nameConfig'),
        align: 'center',
        field: 'name',
        sortable: true
    },
    {name: 'actions', align: 'center', label: t('common.edit'), style: 'width: 12em', headerStyle: 'width: 12em',
        field: row => row}
]

const rows: Ref<PredefinedConfiguration[]> = ref([])
const ini = configStore.getAllConfigs().then(() => rows.value = configStore.allPreConfigs)

const currentConfig = ref()
const configIn: any = ref([])
const configOut: any= ref([])

const qSelectGeneral = ref({
  qSelectsSet: <any[]>[]
})

function removeConfig(i: any) {
  qSelectGeneral.value.qSelectsSet.splice(i, 1)
}

function checkBaseConf() {
  let x = 0;
  let y = false;
  qSelectGeneral.value.qSelectsSet.forEach((element: any, index: any) => {
    if(qSelectGeneral.value.qSelectsSet[index].baseConfig){
      x= x+1;
    }
  })
  if (x===1){
    y= true
  }
  return y
}

function checkLength(){
  return (qSelectGeneral.value.qSelectsSet.length===1);
}

const check = (k: any) => {

  const baseConfCount = qSelectGeneral.value.qSelectsSet.filter(
      (config: any) => config.baseConfig
  ).length;
  if (baseConfCount > 1) {
    // Display warning or prevent saving
    console.log('Warning: You can only select one base configuration.');
    $q.notify({
      type: 'negative',
      message: "You can only choose one base configuration.",
      caption: "Error",
      position: "top",
      color: 'negative',
      textColor: 'postitive',
      timeout: 3000,
      classes: "loginNotify"
    })
    qSelectGeneral.value.qSelectsSet.forEach((element: any, index: any)=>{
      if(!(index===k)){
        element.baseConfig= false;
      }
    })
    return false; // Prevent saving
  }
  return true; // Allow saving
}

function addConfiguration() {
  qSelectGeneral.value.qSelectsSet.push( {
    doorConfigIn: {configParts: [{credentials: [] , attributeFilter: [{ attribute: null,
          predicateType: null, value: null, currentDate: false } ]}], description: ""},
    doorConfigOut: {configParts: [{credentials:[], attributeFilter: [{ attribute: null,
          predicateType: null, value: null, currentDate: false } ]}], description: ""},
    baseConfig: false,
    startTime: '',
    endTime: ''
  })
}

function changeDirectionOut(direction: Direction, k: any) {
    if (direction === Direction.IN) {
        configOut.value[k].direction = Direction.OUT
    } else if (direction === Direction.OUT) {
        configOut.value[k].direction = Direction.IN
    }
}

function onCancelClick() {
    configName.value = null
    currentConfig.value = null
    qSelectGeneral.value.qSelectsSet.splice(0)
}

async function onOKClick() {
    await addConfig()
    await configStore.getAllConfigs()
    rows.value = configStore.allPreConfigs
    configName.value = null
    currentConfig.value = null
}

async function addConfig() {
  let config: PredefinedConfiguration = {
    name: '',
    doorConfig: [],
  }

  config.name = configName.value
  console.log("addConfig", qSelectGeneral.value.qSelectsSet)
  qSelectGeneral.value.qSelectsSet.forEach((element: any, index: any) => {
    console.log(element)
    let oneConfig = {
      doorConfigIn: {
        description: '',
        configParts: []
      },
      doorConfigOut: {
        description: '',
        configParts: []
      },
      startTime: '',
      endTime: '',
      baseConfig: false
    }
    oneConfig.startTime = element.startTime
    oneConfig.endTime = element.endTime
    oneConfig.baseConfig = element.baseConfig
    if (configIn.value[index].direction === Direction.BOTH) {
      oneConfig.doorConfigIn = JSON.parse(JSON.stringify(element.doorConfigIn))
      oneConfig.doorConfigOut = JSON.parse(JSON.stringify(element.doorConfigIn))
      oneConfig.doorConfigIn.description = element.doorConfigIn.description
      oneConfig.doorConfigOut.description = element.doorConfigIn.description
    } else if (configIn.value[index].direction === Direction.IN) {
      oneConfig.doorConfigIn = JSON.parse(JSON.stringify(element.doorConfigIn))
      oneConfig.doorConfigOut = JSON.parse(JSON.stringify(element.doorConfigOut))
      oneConfig.doorConfigIn.description = element.doorConfigIn.description
      oneConfig.doorConfigOut.description = element.doorConfigOut.description
    } else if (configIn.value[index].direction === Direction.OUT) {
      oneConfig.doorConfigIn = JSON.parse(JSON.stringify(element.doorConfigOut))
      oneConfig.doorConfigOut = JSON.parse(JSON.stringify(element.doorConfigIn))
      oneConfig.doorConfigIn.description = element.doorConfigOut.description
      oneConfig.doorConfigOut.description = element.doorConfigIn.description
    }
    console.log(oneConfig)
    config.doorConfig.push(oneConfig)
  })
  if(edit.value) {
      config.id = currentConfig.value
      await configStore.updateConfig(config)
      edit.value = false
  } else {
      await configStore.createConfig(config)
  }
  qSelectGeneral.value.qSelectsSet.splice(0)
}

async function deleteConfig(config: any) {
    console.log("delete", config)
    await configStore.deleteConfig(config)
    await configStore.getAllConfigs()
    rows.value = configStore.allPreConfigs
    currentConfig.value = null
    configName.value = null
}

async function editConfig(config: any) {
    await configStore.getConfig(config)
    let editConfig = configStore.currentConfig
    configName.value = editConfig?.name
    qSelectGeneral.value.qSelectsSet.splice(0)
    editConfig?.doorConfig.forEach((element, index) => {
      console.log(element)
      let object = {
        doorConfigIn: {
          description: element.doorConfigIn.description,
          configParts: element.doorConfigIn.configParts
        },
        doorConfigOut: {
          description: element.doorConfigOut.description,
          configParts: element.doorConfigOut.configParts
        },
        startTime: element.startTime || '',
        endTime: element.endTime || '',
        baseConfig: element.baseConfig || false
      }
      qSelectGeneral.value.qSelectsSet.push(object)
    })

}

</script>

<style scoped>
</style>