<template>
    <q-page class="column justify-evenly" style="padding: 2em 5em">
        <p class="row text-h3 justify-center">{{ t("predefinedConfigs.title") }}</p>
        <div class="row self-center">
            <q-table
                    style="width: 80vw; height: 50vh"
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
                              color="grey"
                              icon="edit"
                              @click="configDialog = true">
                            </q-btn>

                            <q-btn
                                dense
                                round
                                flat
                                icon="delete"
                                color="grey"
                                @click="deleteConfig(props.row.id)">
                            </q-btn>
                        </div>
                    </q-td>
                </template>

                <template class="row no-wrap" v-slot:top-right>
                    <q-btn
                       flat
                       rounded
                       icon="add"
                       color="grey"
                       :label="t('predefinedConfigs.new')"
                       @click="configDialog = true"
                       style="margin-right: 2em">
                    </q-btn>
                    <q-input
                       borderless dense debounce="300" v-model="filter"
                       :placeholder="t( 'common.search')">
                        <template v-slot:append>
                            <q-icon name="search"/>
                        </template>
                    </q-input>
                </template>
            </q-table>
        </div>

        <q-dialog v-model="configDialog">
            <q-card>
                <q-card-section>
                    <q-input class="full-width" filled v-model="configName" label="Konfigurationsbezeichnung" stack-label></q-input>
                </q-card-section>
                <DoorConfig
                    ref="configIn" :door-config="doorConfigIn"
                    :direction="JSON.stringify(this.doorConfigIn) !== JSON.stringify(this.doorConfigOut) ? Direction.IN : Direction.BOTH"
                    @changeDirection="changeDirectionOut">
                </DoorConfig>
                <DoorConfig v-show="$refs.configIn?.direction !== Direction.BOTH" ref="configOut"
                            :direction="JSON.stringify(this.doorConfigIn) !== JSON.stringify(this.doorConfigOut) ? Direction.OUT : Direction.BOTH"
                            :door-config="doorConfigOut" :is-config-out="true">
                </DoorConfig>
                <q-card-actions align="right">
                    <q-btn flat color="primary" label="Abbrechen" @click="onCancelClick()" v-close-popup></q-btn>
                    <q-btn flat color="primary" label="Speichern" :disable="!configName" @click="onOKClick()" v-close-popup></q-btn>
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


const direction = computed(() => {
    return Direction
})

const props = defineProps({
    doorConfigIn: {
        required: false
    },
    doorConfigOut: {
        required: false
    }
})
const emits = defineEmits({

})

const configStore = useConfigStore()
const configIn = ref()
const configOut = ref()
const configName = ref()
const configDialog = ref(false)
const filter = ref('')
const {t} = useI18n()
const columns = [
    {
        name: 'name',
        required: true,
        label: "Bezeichung",
        align: 'center',
        field: 'name',
        sortable: true
    },
    {name: 'actions', align: 'center', label: "Bearbeiten", style: 'width: 12em', headerStyle: 'width: 12em'}
]

const rows: Ref<PredefinedConfiguration[]> = ref([])
const ini = configStore.getAllConfigs().then(() => rows.value = configStore.allPreConfigs)


function changeDirectionOut(direction: Direction) {
    if (direction === Direction.IN) {
        configOut.value.direction = Direction.OUT
    } else if (direction === Direction.OUT) {
        configOut.value.direction = Direction.IN
    }
}

function onCancelClick() {
    configName.value = null
}

async function onOKClick() {
  await addConfig()
  await configStore.getAllConfigs()
  rows.value = configStore.allPreConfigs
}

async function addConfig() {
  let config: PredefinedConfiguration = {
    name: '',
    doorConfigIn: {
      description: '',
      configParts: []
    },
    doorConfigOut: {
      description: '',
      configParts: []
    }
  }

  console.log('qselects', configIn.value.qSelects)
  config.name = configName.value
  if (configIn.value.direction === Direction.BOTH) {
    config.doorConfigIn = JSON.parse(JSON.stringify(configIn.value.qSelects))
    config.doorConfigOut = JSON.parse(JSON.stringify(configIn.value.qSelects))
    config.doorConfigIn.description = configIn.value.configDescription
    config.doorConfigOut.description = configIn.value.configDescription
  } else if (configIn.value.direction === Direction.IN) {
    config.doorConfigIn = JSON.parse(JSON.stringify(configIn.value.qSelects))
    config.doorConfigOut = JSON.parse(JSON.stringify(configOut.value.qSelects))
    config.doorConfigIn.description = configIn.value.configDescription
    config.doorConfigOut.description = configOut.value.configDescription
  } else if (configIn.value.direction === Direction.OUT) {
    config.doorConfigIn = JSON.parse(JSON.stringify(configOut.value.qSelects))
    config.doorConfigOut = JSON.parse(JSON.stringify(configIn.value.qSelects))
    config.doorConfigIn.description = configOut.value.configDescription
    config.doorConfigOut.description = configIn.value.configDescription
  }
  await configStore.createConfig(config)
}

async function deleteConfig(config: any) {
  await configStore.deleteConfig(config)
  await configStore.getAllConfigs()
  rows.value = configStore.allPreConfigs
}

async function editConfig() {

}




</script>

<style scoped>
</style>