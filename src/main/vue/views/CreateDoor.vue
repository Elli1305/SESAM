<template>
    <q-dialog ref="dialog" @hide="onDialogHide">
        <q-card style="min-width: 60em">
            <q-card-section>
                <div class="text-h6">{{ t('floorplan.addDoor') }}</div>
            </q-card-section>
            <q-card-section class="row q-px-lg justify-between no-wrap">
                <q-input class="full-width" filled v-model="doorName" :label="t('floorplan.doorName')" stack-label/>
                <q-select
                        class="q-ml-md"
                        style="min-width: 20em"
                        filled
                        v-model="room"
                        use-input
                        hide-selected
                        :label="t('floorplan.pickRoom')"
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
        <q-select
             class="q-ml-md"
             style="min-width: 20em"
             filled
             use-input
             hide-selected
             fill-input
             input-debounce="0"
             @filter="filterFn"
             label="Konfiguration auswählen"
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
            <q-card-section  v-for="(selectConf,k) in qSelectgeneral.qSelectsSet">
                <q-card bordered flat>
                    <q-toolbar class="bg-primary text-accent">

                        <q-toolbar-title>Konfiguration</q-toolbar-title>
                        <q-icon class="q-mr-xs" color="accent" size="1.25em" name="info_outlined">
                            <q-tooltip max-width="15em" anchor="center right" self="center left">
                                You can only choose one base configuration.
                            </q-tooltip>
                        </q-icon>
                        <q-td v-if= !(checkLength())>
                        <q-btn flat round icon="delete" size="0.75em" @click="removeConfig(i)"/>
                        </q-td>
                    </q-toolbar>


                        <div class="colomn q-mt-sm justify-around items-center no-wrap">

                    <div class="q-pa-md">
                        <div class="q-gutter-sm">

                                <q-checkbox dense v-model="qSelectgeneral.qSelectsSet[k].basisConf" label="Basis Konfiguration" color="primary" @click = "check(k)"/>

                        </div>
                    </div>
                            <q-td class="q-pl-md v-if=" v-if="!qSelectgeneral.qSelectsSet[k].basisConf">
                    <div class="q-gutter-sm row">
                        <div class="q-gutter-sm row">
                            <q-input filled v-model="qSelectgeneral.qSelectsSet[k].startTime" mask="time" :rules="['time']" >
                                <template v-slot:append>
                                    <q-icon name="access_time" class="cursor-pointer">
                                        <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                            <q-time v-model="qSelectgeneral.qSelectsSet[k].startTime">
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
                            <q-input filled v-model="qSelectgeneral.qSelectsSet[k].endTime" mask="time" :rules="['time']" :disabled="qSelectgeneral.qSelectsSet[k].basisConf">
                                <template v-slot:append>
                                    <q-icon name="access_time" class="cursor-pointer">
                                        <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                            <q-time v-model="qSelectgeneral.qSelectsSet[k].endTime">
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

                <door-config ref="configIn" :door-config="qSelectgeneral.qSelectsSet[k].doorConfigIn"
                         :direction="JSON.stringify(this.qSelectgeneral.qSelectsSet[k].doorConfigIn) !== JSON.stringify(this.qSelectgeneral.qSelectsSet[k].doorConfigOut) ? Direction.IN : Direction.BOTH"
                         @changeDirection="changeDirectionOut(k)"></door-config>
            <door-config v-show="qSelectgeneral.qSelectsSet[k].doorConfigIn?.direction !== Direction.BOTH" ref="configOut"
                         :direction="JSON.stringify(this.qSelectgeneral.qSelectsSet[k].doorConfigIn) !== JSON.stringify(this.qSelectgeneral.qSelectsSet[k].doorConfigOut) ? Direction.OUT : Direction.BOTH"
                         :door-config="qSelectgeneral.qSelectsSet[k].doorConfigOut" :is-config-out="true"></door-config>

                <q-btn class="q-ml-sm q-mb-sm" flat dense rounded color="primary" icon="add" @click="addConfiguration">
                    Konfiguration hinzufügen
                </q-btn>


                <q-card-actions align="right">
                <q-btn flat color="primary" :label="t('common.cancel')" @click="onCancelClick"/>
                <q-btn flat color="primary" :label="t('common.save')" :disable="!doorName || (!room && !door) || !checkBaseConf()" @click="onOKClick"/>
            </q-card-actions>
        </q-card>
            </q-card-section>
        </q-card>
    </q-dialog>
</template>

<script>
import {ref, watch} from "vue";
import DoorConfig from "@/main/vue/views/DoorConfig.vue";
import {Direction} from "@/main/vue/entity/doorConfiguration";
import {useI18n} from "vue-i18n";
import {useConfigStore} from "@/main/vue/stores/config";
import {storeToRefs} from "pinia";
import {useQuasar} from "quasar";
const $q = useQuasar()


export default {
    computed: {
        Direction() {
            return Direction
        },

    multipleBaseConfigs() {
            return this.qSelectgeneral.qSelectsSet.filter(conf => conf.basisConf).length > 1;

    }
    },
    components: {DoorConfig},
    props: {
        rooms: Array,
        door: {
            required: false
        },
    },

    emits: [
        'ok', 'hide'
    ],

    methods: {
        changeDirectionOut(direction,k) {
            if (direction === Direction.IN) {
                this.qSelectgeneral.qSelectsSet[k].doorConfigOut.direction = Direction.OUT
            } else if (direction === Direction.OUT) {
                this.qSelectgeneral.qSelectsSet[k].doorConfigOut.direction= Direction.IN
            }
        },
        show() {
            this.$refs.dialog.show()
        },
        hide() {
            this.$refs.dialog.hide()
        },
        onDialogHide() {
            this.$emit('hide')
        },

        checkBaseConf(){
            let x = 0;
            let y = false;
            this.qSelectgeneral.qSelectsSet.forEach((element, index) => {
                if(this.qSelectgeneral.qSelectsSet[index].basisConf.valueOf() === true){
                    x= x+1;
                }
            })
            if (x===1){
                y= true
            }
            return y
        },

        addConfiguration() {
            this.qSelectgeneral.qSelectsSet.push( {
                DoorConfig,
                doorConfigIn: null,
                doorConfigOut: null,
                basisConf: false,
                startTime: null,
                endTime: null
            })
        },
        removeConfig(i) {
                this.qSelectgeneral.qSelectsSet.splice(i, 1)
        },
        checkLength(){
            return (this.qSelectgeneral.qSelectsSet.length===1);
        },


        onOKClick() {
            this.qSelectgeneral.qSelectsSet.forEach((elemnt, index) => {
                let config = {}

                //console.log('qselects', this.$refs.configIn.qSelects)
                if (this.$refs.configIn.direction === Direction.BOTH) {
                    config.doorConfigIn = JSON.parse(JSON.stringify(this.$refs.qSelectgeneral.qSelectsSet[index].doorConfigIn.qSelects))
                    config.doorConfigOut = JSON.parse(JSON.stringify(this.$refs.qSelectgeneral.qSelectsSet[index].doorConfigIn.qSelects))
                    config.doorConfigIn.description = this.$refs.qSelectgeneral.qSelectsSet[index].doorConfigIn.configDescription
                    config.doorConfigOut.description = this.$refs.qSelectgeneral.qSelectsSet[index].doorConfigIn.configDescription
                    //config.doorConfigOut = JSON.parse(JSON.stringify(this.$refs.configIn.qSelects))
                    //config.doorConfigIn.description = this.$refs.configIn.configDescription
                    //config.doorConfigOut.description = this.$refs.configIn.configDescription
                } else if (this.$refs.configIn.direction === Direction.IN) {
                    config.doorConfigIn = JSON.parse(JSON.stringify(this.$refs.qSelectgeneral.qSelectsSet[index].doorConfigIn.qSelects))
                    config.doorConfigOut = JSON.parse(JSON.stringify(this.$refs.qSelectgeneral.qSelectsSet[index].doorConfigOut.qSelects))
                    config.doorConfigIn.description = this.$refs.qSelectgeneral.qSelectsSet[index].doorConfigIn.configDescription
                    config.doorConfigOut.description = this.$refs.qSelectgeneral.qSelectsSet[index].doorConfigOut.configDescription
                } else if (this.$refs.configIn.direction === Direction.OUT) {
                    config.doorConfigIn = JSON.parse(JSON.stringify(this.$refs.qSelectgeneral.qSelectsSet[index].doorConfigOut.qSelects))
                    config.doorConfigOut = JSON.parse(JSON.stringify(this.$refs.qSelectgeneral.qSelectsSet[index].doorConfigIn.qSelects))
                    config.doorConfigIn.description = this.$refs.qSelectgeneral.qSelectsSet[index].doorConfigIn.configDescription
                    config.doorConfigOut.description = this.$refs.qSelectgeneral.qSelectsSet[index].doorConfigIn.configDescription
                }
                config.doorConfigIn?.configParts.forEach(part => part.credentials = part.credentials.map(credential => credential.credentialDefinitionId))
                config.doorConfigOut?.configParts.forEach(part => part.credentials = part.credentials.map(credential => credential.credentialDefinitionId))
                this.$emit('ok', {
                    room: this.room,
                    doorName: this.doorName,
                    configuration: config
                })
                this.hide()
            })
        },
        onCancelClick() {
            this.hide()
        },
    },
    setup(props) {
        const room = ref(null)
        const doorName = ref('')
        const roomOptions = ref(props.rooms)
        const {t} = useI18n()
        const $q = useQuasar()
        const configStore = useConfigStore()
        configStore.getAllConfigs()
        const {allPreConfigs} = storeToRefs(configStore)
        const configOptions = ref()
        configOptions.value = configStore.allPreConfigs
        const selectedConfig = ref()

        const configIn = ref()
        const configOut = ref()

        watch(allPreConfigs, () => {
            configOptions.value = configStore.allPreConfigs
        })

        watch(selectedConfig, async () => {
            if (selectedConfig.value == null) {
                configIn.value.configDescription = null
                configIn.value.qSelects.configParts = []
                configOut.value.configDescription = null
                configOut.value.qSelects.configParts = []
                configIn.value.direction = Direction.BOTH
            } else {
                await configStore.getConfig(selectedConfig.value.id)
                let chosenConfig = configStore.currentConfig
                configIn.value.configDescription = chosenConfig?.doorConfigIn.description
                configIn.value.qSelects.configParts = chosenConfig?.doorConfigIn.configParts
                configOut.value.configDescription = chosenConfig?.doorConfigOut.description
                configOut.value.qSelects.configParts = chosenConfig?.doorConfigOut.configParts
                if (JSON.stringify(chosenConfig?.doorConfigIn) !== JSON.stringify(chosenConfig?.doorConfigOut)) {
                    configIn.value.direction = Direction.IN
                    configOut.value.direction = Direction.OUT
                }
            }
        })


        const disableSave = ref(false);

        const qSelectgeneral = ref({

            qSelectsSet: [{
                DoorConfig,
                doorConfigIn: null,
                doorConfigOut: null,
                basisConf: false,
                startTime: null,
                endTime: null
            }]

        })


        if (props.door) {
            doorName.value = props.door.name
        }

        const check = (k) => {

            const baseConfCount = qSelectgeneral.value.qSelectsSet.filter(
                (config) => config.basisConf
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
                qSelectgeneral.value.qSelectsSet.forEach((element, index)=>{
                    if(!(index===k)){
                        element.basisConf= false;
                    }
                })
                return false; // Prevent saving
            }
            return true; // Allow saving
        };

        const filterFn = function (val, update, abort) {
            update(() => {
                const needle = val.toLowerCase()
                roomOptions.value = props.rooms.filter(room => room.name.toLowerCase().indexOf(needle) > -1)
            })
        }

    return {
        qSelectgeneral,
      room,
      filterFn,
      doorName,
        disableSave,
        t,
        check,
        configOptions,
        selectedConfig,
        configOut,
        configIn,
    }
  }
}
</script>