<template>
    <q-dialog persistent>
        <q-card style="min-width: 50em; background-color: var(--bg-color); color: var(--text-color)">
            <q-card-section>
                <div class="text-h6">{{ t("editor.groupRooms.groupsConfig") }}</div>
            </q-card-section>
            <q-card-section>
                <q-table
                    style="background-color: var(--bg-color); color: var(--text-color)"
                    flat bordered
                    hide-bottom
                    :rows-per-page-options="[0]"
                    :title="t('editor.groupRooms.roomSelection')"
                    :rows="rows2"
                    :columns="columns2"
                    :filter="searchinput"
                    row-key="name"
                    separator="cell"
                    :no-data-label="t('common.noData')"
                    :no-results-label="t('common.noResults')"
                    :pagination-label="getPaginationLabel"
                    :visible-columns="visibleColumns"
                >
                    <template v-slot:top-right>
                        <div v-if="$q.screen.gt.xs" class="col" style="padding-right: 2em">
                            <q-toggle v-model="visibleColumns" @update:model-value="fetchDoors(rows2)" val="doorNames"
                                      :label="t('editor.groupRooms.doors')" size="2.5em"/>
                        </div>
                        <q-input class="q-ml-xs" outlined rounded dense debounce="250" v-model="searchinput"
                                 :placeholder="t('common.search')">
                            <template v-slot:append>
                                <q-icon name="search"/>
                            </template>
                        </q-input>
                    </template>

                    <template v-slot:body-cell-doorNames="props">
                        <q-td style="width: 60%" :props="props">
                            <q-select v-if="props.row.selected"
                                      class="q-my-sm"
                                      filled dense options-dense
                                      emit-value
                                      v-model="model[props.row.room]"
                                      multiple
                                      :options="props.row.doors"
                                      option-label="name"
                                      options-cover
                                      map-options
                                      use-chips
                            />
                        </q-td>
                    </template>
                    <template v-slot:body-cell-actions="props">
                        <q-td style="width: 10%" :props="props">
                            <q-checkbox v-model="props.row.selected"
                                        @update:model-value="checkIfSelected(props.row.selected, props.row.room)"/>
                        </q-td>
                    </template>
                </q-table>
            </q-card-section>
            <q-card-section>
                <q-select
                    class="q-ml-md"
                    style="min-width: 20em"
                    filled
                    use-input
                    hide-selected
                    fill-input
                    input-debounce="0"
                    @filter="filterConfigOptions"
                    :label="t('floorPlan.chooseConfig')"
                    option-label="name"
                    v-model="selectedConfig"
                    :options="configOptions"
                    clearable
                >

                    <template v-slot:no-option>
                        <q-item>
                            <q-item-section class="text-grey">
                                No results
                            </q-item-section>
                        </q-item>
                    </template>
                </q-select>
            </q-card-section>
            <q-card-section v-for="(selectConf,k) in qSelectGeneral.qSelectsSet">
                <q-card bordered flat>
                    <q-toolbar class="bg-primary text-accent">
                        <q-toolbar-title>Konfiguration</q-toolbar-title>
                        <q-icon class="q-mr-xs" color="accent" size="1.25em" name="info_outlined">
                            <q-tooltip max-width="15em" anchor="center right" self="center left">
                                You can only choose one base configuration.
                            </q-tooltip>
                        </q-icon>
                        <q-td v-if=!(checkLength())>
                            <q-btn flat round icon="delete" size="0.75em" @click="removeConfig(k)"/>
                        </q-td>
                    </q-toolbar>
                    <div class="colomn q-mt-sm justify-around items-center no-wrap">
                        <div class="q-pa-md">
                            <div class="q-gutter-sm">
                                <q-checkbox dense v-model="qSelectGeneral.qSelectsSet[k].baseConfig"
                                            label="Basis Konfiguration"
                                            color="primary" @click="check(k)"/>
                            </div>
                        </div>
                        <q-td class="q-pl-md v-if=" v-if="!qSelectGeneral.qSelectsSet[k].baseConfig">
                            <div class="q-gutter-sm row">
                                <div class="q-gutter-sm row">
                                    <q-input filled v-model="qSelectGeneral.qSelectsSet[k].startTime" mask="time"
                                             :rules="['time']">
                                        <template v-slot:append>
                                            <q-icon name="access_time" class="cursor-pointer">
                                                <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                                    <q-time v-model="qSelectGeneral.qSelectsSet[k].startTime">
                                                        <div class="row items-center justify-end">
                                                            <q-btn v-close-popup label="Close" color="primary" flat/>
                                                        </div>
                                                    </q-time>
                                                </q-popup-proxy>
                                            </q-icon>
                                        </template>
                                    </q-input>
                                </div>
                                <div class="q-gutter-sm row">
                                    <q-input filled v-model="qSelectGeneral.qSelectsSet[k].endTime" mask="time"
                                             :rules="['time']"
                                             :disabled="qSelectGeneral.qSelectsSet[k].baseConfig">
                                        <template v-slot:append>
                                            <q-icon name="access_time" class="cursor-pointer">
                                                <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                                    <q-time v-model="qSelectGeneral.qSelectsSet[k].endTime">
                                                        <div class="row items-center justify-end">
                                                            <q-btn v-close-popup label="Close" color="primary" flat/>
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
                    <door-config :door-config="qSelectGeneral.qSelectsSet[k].doorConfigIn"
                                 :direction="JSON.stringify(qSelectGeneral.qSelectsSet[k].doorConfigIn) !==
                        JSON.stringify(qSelectGeneral.qSelectsSet[k].doorConfigOut) ? Direction.IN : Direction.BOTH"
                                 @changeDirection="changeDirectionOut($event, k)" ref="doorIn">
                    </door-config>
                    <door-config v-show="JSON.stringify(qSelectGeneral.qSelectsSet[k].doorConfigIn.direction) !==
                         JSON.stringify(qSelectGeneral.qSelectsSet[k].doorConfigOut.direction)"
                                 :direction="Direction.BOTH"
                                 :door-config="qSelectGeneral.qSelectsSet[k].doorConfigOut" :is-config-out="true"
                                 ref="doorOut">
                    </door-config>
                    <q-btn class="q-ml-sm q-mb-sm" flat dense rounded color="primary" icon="add"
                           @click="addConfiguration">
                        Konfiguration hinzufügen
                    </q-btn>
                </q-card>
            </q-card-section>
            <q-card>
                <q-card-actions align="right" class="text-primary">
                    <q-btn flat @click="resetConfig()" v-close-popup> {{ t("common.cancel") }}</q-btn>
                    <q-btn flat :disable="!checkBaseConf()" v-close-popup @click="saveConfig(model)">
                        {{ t("common.save") }}
                    </q-btn>
                </q-card-actions>
            </q-card>
        </q-card>
    </q-dialog>
</template>

<script>
import DoorConfig from "@/main/vue/views/DoorConfig.vue";
import {Direction} from "@/main/vue/entity/doorConfiguration";
import {useQuasar} from "quasar";
import {prop} from "vue-class-component";
import {useRoomGroupStore} from "@/main/vue/stores/roomGroupStore";
import {useI18n} from "vue-i18n";
import {useDoorStore} from "@/main/vue/stores/door";
import {reactive, ref, watch} from "vue";
import {useConfigStore} from "@/main/vue/stores/config";
import {storeToRefs} from "pinia";
const $q = useQuasar()



export default {
    name: "MultipleRoomsDoorConfigDialog",
    components: {DoorConfig},
    computed: {
        Direction() {
            return Direction
        },
    },
    props: {
        group: {
            required: true
        },
    },
        methods: {

            checkBaseConf() {
                let x = 0;
                let y = false;
                this.qSelectGeneral.qSelectsSet.forEach((element, index) => {
                    if (this.qSelectGeneral.qSelectsSet[index].baseConfig === true) {
                        x = x + 1;
                    }
                })
                if (x === 1) {
                    y = true
                }
                return y
            },
            useRoomGroupStore,
            addConfiguration() {
                this.qSelectGeneral.qSelectsSet.push({
                    doorConfigIn: {
                        configParts: [{
                            credentials: [], attributeFilter: [{
                                attribute: null,
                                predicateType: null, value: null, currentDate: false
                            }]
                        }], description: "", direction: Direction.BOTH
                    },
                    doorConfigOut: {
                        configParts: [{
                            credentials: [], attributeFilter: [{
                                attribute: null,
                                predicateType: null, value: null, currentDate: false
                            }]
                        }], description: "", direction: Direction.BOTH
                    },
                    baseConfig: false,
                    startTime: null,
                    endTime: null
                })
            },
            removeConfig(i) {
                this.qSelectGeneral.qSelectsSet.splice(i, 1)
            },
            checkLength() {
                return (this.qSelectGeneral.qSelectsSet.length === 1);
            },


            changeDirectionOut(direction, k) {

                if (direction === Direction.IN) {
                    this.$refs.doorOut[k].direction = Direction.OUT
                    this.qSelectGeneral.qSelectsSet[k].doorConfigOut.direction = Direction.IN
                    this.qSelectGeneral.qSelectsSet[k].doorConfigIn.direction = Direction.OUT

                } else if (direction === Direction.OUT) {
                    this.$refs.doorOut[k].direction = Direction.IN
                    this.qSelectGeneral.qSelectsSet[k].doorConfigOut.direction = Direction.OUT
                    this.qSelectGeneral.qSelectsSet[k].doorConfigIn.direction = Direction.IN

                } else if (direction === Direction.BOTH) {
                    this.$refs.doorOut[k].direction = Direction.BOTH
                    this.qSelectGeneral.qSelectsSet[k].doorConfigOut.direction = Direction.BOTH
                    this.qSelectGeneral.qSelectsSet[k].doorConfigIn.direction = Direction.BOTH

                }
            },
            prop,
            customFilter(rows, terms) {
                // rows contain the entire data
                // terms contains whatever you have as filter
                const lowerSearch = terms.search ? terms.search.toLowerCase() : ""

                const filteredRows = rows.filter(
                    (row) => {

                        let ans

                        //Assume true in case there is no search
                        let s1 = true
                        let s2 = true

                        //If search term exists, convert to lower case and see which rows contain it
                        if (lowerSearch !== "") {
                            s1 = false
                            s2 = false
                            //Get the values
                            let s1_values = Object.values(row)
                            let s2_values = row.rooms.map(r => r.name);
                            //Convert to lowercase
                            let s1_lower = s1_values.map(x => x.toString().toLowerCase())
                            let s2_lower = s2_values.map(x => x.toString().toLowerCase())

                            for (let val = 0; val < s1_lower.length; val++) {
                                if (s1_lower[val].includes(lowerSearch)) {
                                    s1 = true
                                    break
                                }
                            }
                            for (let val = 0; val < s2_lower.length; val++) {
                                if (s2_lower[val].includes(lowerSearch)) {
                                    s2 = true
                                    break
                                }
                            }
                        }
                        //assume row doesn't match
                        ans = false
                        //check if any of the conditions match
                        if ((s1) || s2) {
                            ans = true
                        }
                        return ans
                    })
                return filteredRows
            },
            resetConfig() {
                this.qSelectGeneral.qSelectsSet = [{
                    doorConfigIn: {
                        configParts: [{
                            credentials: [], attributeFilter: [{
                                attribute: null,
                                predicateType: null, value: null, currentDate: false
                            }]
                        }], description: "", direction: Direction.BOTH
                    },
                    doorConfigOut: {
                        configParts: [{
                            credentials: [], attributeFilter: [{
                                attribute: null,
                                predicateType: null, value: null, currentDate: false
                            }]
                        }], description: "", direction: Direction.BOTH
                    },
                    baseConfig: false,
                    startTime: null,
                    endTime: null
                }]
                this.selectedConfig = null
            },
            saveConfig(model) {
                console.log("model", model)
                const roomGroupStore = useRoomGroupStore()
                const res = []
                for (const val in model) {
                    if (val && val !== 0) {
                        res.push(model[val])
                    }
                }
                let finalArray = res.flat(1).map(d => d.id)
                const allConfig = []
                this.qSelectGeneral.qSelectsSet.forEach((element, index) => {
                    let config = {}
                    config.baseConfig = this.qSelectGeneral.qSelectsSet[index].baseConfig
                    config.startTime = this.qSelectGeneral.qSelectsSet[index].startTime
                    config.endTime = this.qSelectGeneral.qSelectsSet[index].endTime
                    if (this.$refs.doorIn[index].direction === Direction.BOTH) {
                        config.doorConfigIn = JSON.parse(JSON.stringify(this.qSelectGeneral.qSelectsSet[index].doorConfigIn))
                        config.doorConfigOut = JSON.parse(JSON.stringify(this.qSelectGeneral.qSelectsSet[index].doorConfigIn))
                        config.doorConfigIn.description = this.qSelectGeneral.qSelectsSet[index].doorConfigIn.description
                        config.doorConfigOut.description = this.qSelectGeneral.qSelectsSet[index].doorConfigIn.description
                    } else if (this.$refs.doorIn[index].direction === Direction.IN) {
                        config.doorConfigIn = JSON.parse(JSON.stringify(this.qSelectGeneral.qSelectsSet[index].doorConfigIn))
                        config.doorConfigOut = JSON.parse(JSON.stringify(this.qSelectGeneral.qSelectsSet[index].doorConfigOut))
                        config.doorConfigIn.description = this.qSelectGeneral.qSelectsSet[index].doorConfigIn.description
                        config.doorConfigOut.description = this.qSelectGeneral.qSelectsSet[index].doorConfigOut.description
                    } else if (this.$refs.doorIn[index].direction === Direction.OUT) {
                        config.doorConfigIn = JSON.parse(JSON.stringify(this.qSelectGeneral.qSelectsSet[index].doorConfigOut))
                        config.doorConfigOut = JSON.parse(JSON.stringify(this.qSelectGeneral.qSelectsSet[index].doorConfigIn))
                        config.doorConfigIn.description = this.qSelectGeneral.qSelectsSet[index].doorConfigOut.description
                        config.doorConfigOut.description = this.qSelectGeneral.qSelectsSet[index].doorConfigIn.description
                    }
                    allConfig.push(config)
                })
                console.log("finalArray", finalArray)
                let configList = {
                    doorIds: finalArray,
                    doorConfig: allConfig
                }
                roomGroupStore.setGroupConfig(configList).then(() => this.$emit('ok'))
                finalArray = []
            }
        },
    emits: [
        'ok'
    ],

    setup(props) {

        const {t} = useI18n();
        const $q = useQuasar();
        const roomGroupStore = useRoomGroupStore()
        const doorStore = useDoorStore()
        const options = ref([]);
        const list = [];
        const model = ref([[]])
        const res = []
        let finalArray = []

        const configStore = useConfigStore()
        configStore.getAllConfigs()
        const {allPreConfigs} = storeToRefs(configStore)
        const configOptions = ref()
        configOptions.value = configStore.allPreConfigs
        const selectedConfig = ref()

        const qSelectGeneral = reactive({
            qSelectsSet: [{
                doorConfigIn: {
                    configParts: [{
                        credentials: [],
                        attributeFilter: [{
                            attribute: null,
                            predicateType: null,
                            value: null,
                            currentDate: false
                        }]
                    }],
                    description: "",
                    direction: Direction.BOTH
                },
                doorConfigOut: {
                    configParts: [{
                        credentials: [],
                        attributeFilter: [{
                            attribute: null,
                            predicateType: null,
                            value: null,
                            currentDate: false
                        }]
                    }],
                    description: "",
                    direction: Direction.BOTH
                },
                baseConfig: false,
                startTime: null,
                endTime: null
            }]
        })

        if(props.group) {
            getRoomsAndDoors(props.group)
        }

        function fetchDoors(rows) {
            rows?.forEach(row => getDoors(row.room, true))
        }

        const filterConfigOptions = function (val, update) {
            update(() => {
                const needle = val.toLowerCase()
                configOptions.value = allPreConfigs.value.filter(config => config.name.toLowerCase().indexOf(needle) > -1)
            })
        }

        watch(allPreConfigs, () => {
            configOptions.value = configStore.allPreConfigs
        })

        watch(selectedConfig, async () => {
            if (selectedConfig.value == null) {
                qSelectGeneral.qSelectsSet.splice(0)
                qSelectGeneral.qSelectsSet.push({
                    doorConfigIn: {
                        configParts: [{
                            credentials: [], attributeFilter: [{
                                attribute: null,
                                predicateType: null, value: null, currentDate: false
                            }]
                        }], description: "", direction: Direction.BOTH
                    },
                    doorConfigOut: {
                        configParts: [{
                            credentials: [], attributeFilter: [{
                                attribute: null,
                                predicateType: null, value: null, currentDate: false
                            }]
                        }], description: "", direction: Direction.BOTH
                    },
                    baseConfig: false,
                    startTime: null,
                    endTime: null
                })
            } else {
                await configStore.getConfig(selectedConfig.value.id)
                let chosenConfig = configStore.currentConfig
                let tempConfig = []
                chosenConfig?.doorConfig.forEach((element) => {
                    console.log(element)
                    let object = {
                        doorConfigIn: {
                            direction: JSON.stringify(element.doorConfigIn)
                            !== JSON.stringify(element.doorConfigOut) ? Direction.IN : Direction.BOTH,
                            description: element.doorConfigIn.description,
                            configParts: element.doorConfigIn.configParts
                        },
                        doorConfigOut: {
                            direction: JSON.stringify(element.doorConfigIn)
                            !== JSON.stringify(element.doorConfigOut) ? Direction.OUT : Direction.BOTH,
                            description: element.doorConfigOut.description,
                            configParts: element.doorConfigOut.configParts
                        },
                        startTime: element.startTime || '',
                        endTime: element.endTime || '',
                        baseConfig: element.baseConfig || false
                    }
                    tempConfig.push(object)
                })
                qSelectGeneral.qSelectsSet = tempConfig
                console.log("temp: ", tempConfig)
                console.log("qselect: ", qSelectGeneral.qSelectsSet)
            }
        })

        const check = (k) => {

            const baseConfCount = qSelectGeneral.qSelectsSet.filter(
                (config) => config.baseConfig
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
                qSelectGeneral.qSelectsSet.forEach((element, index) => {
                    if (!(index === k)) {
                        element.baseConfig = false;
                    }
                })
                return false; // Prevent saving
            }
            return true; // Allow saving
        }

        const columns2 = [
            {
                name: 'name',
                required: true,
                label: t('floorPlan.room'),
                align: 'left',
                field: row => row.roomName,
                sortable: true
            },
            {name: 'room', label: t('floorPlan.room'), align: 'left', field: row => row.room, sortable: true},
            {
                name: 'actions',
                label: t('editor.groupRooms.select'),
                style: 'width: 8em',
                headerStyle: 'width: 8em',
                align: 'left'
            },
            {
                name: 'doorNames',
                label: t('editor.groupRooms.doors'),
                field: row => row.doors,
                format: (val) => val.map(e => e.id).join(', '),
                style: 'width: 8em',
                headerStyle: 'width: 8em',
                align: 'left'
            },
            {
                name: 'doors',
                label: t('editor.groupRooms.doors'),
                field: row => row.doors,
                format: (val) => val.map(e => e.id).join(', '),
                style: 'width: 8em',
                headerStyle: 'width: 8em',
                align: 'left'
            }
        ]

        const rows2 = ref([]);
        rows2.value = []

        let currentGroup = ref({
            id: null,
            name: null,
            building: null,
            rooms: []
        });

        function getRoomsAndDoors(id) {
            roomGroupStore.getRoomsAndDoorsByGroupId(id).then((rooms) => {
                rows2.value = rooms.map(r => ({...r, selected: true}))
                fetchDoors(rows2.value)
            })
        }

        const editedRow = ref({})
        const openForm = (row) => {
            editedRow.value = {...row};
        }

        function getDoors(id, checked) {
          if (checked) {
            doorStore.getByRoomId(id).then((doors) => {
              model.value[id] = doors
            })
          }
        }

        function getPaginationLabel(firstRowIndex, endRowIndex, totalRowsNumber) {
            return firstRowIndex.toString() + "-" + endRowIndex.toString() + " / " + totalRowsNumber.toString()
        }

        function checkIfSelected(room, id) {
            if (!room) {
                model.value[id] = []
            }
        }

        return {
            list,
            finalArray,
            res,
            getDoors,
            openForm,
            getRoomsAndDoors,
            currentGroup,
            columns2,
            check,
            filterConfigOptions,
            qSelectGeneral,
            filter: ref({
                search: ''
            }),
            model,
            t,
            group: ref([]),
            rows2,
            searchinput: ref(''),
            visibleColumns: ref(['name', 'actions', 'doorNames']),
            options,
            getPaginationLabel,
            selectedConfig,
            configOptions,
            checkIfSelected,
            fetchDoors
        }
    }
}

</script>

<style scoped>

</style>