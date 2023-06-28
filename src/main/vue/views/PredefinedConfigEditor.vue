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
                                @click="">
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
                    <q-input></q-input>
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
                    <q-btn flat color="primary" label="Speichern" :disable="!currentConfigName" @click="onOKClick()" v-close-popup></q-btn>
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

const currentConfigName = ref()
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

function changeDirectionOut(this: any, direction: Direction) {
    if (direction === Direction.IN) {
        this.$refs.configOut.direction = Direction.OUT
    } else if (direction === Direction.OUT) {
        this.$refs.configOut.direction = Direction.IN
    }
}

function onCancelClick() {
    currentConfigName.value = null
}

function onOKClick() {

}

async function addConfig() {

}

async function deleteConfig() {

}




</script>

<style scoped>
</style>