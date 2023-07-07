<template>
    <q-card-section>
        <q-card bordered flat>
            <q-toolbar class="bg-primary text-accent">
        <q-toolbar-title>{{ t('floorPlan.config') }}</q-toolbar-title>
                <q-field dark borderless>
                    <template v-slot:control>
            <div class="no-outline text-subtitle2">{{t('floorPlan.direction')}}</div>
                    </template>
                    <template v-slot:append>
                        <q-btn-toggle
                                toggle-indeterminate
                                v-model="direction"
                                style="margin: 1em 1em 1em 0"
                :label="t('floorPlan.direction')"
                                color="white"
                                text-color="black"
                                :options="[
                  {label: t('floorPlan.directions.in'), value: Direction.IN},
                  {label: t('floorPlan.directions.both'), value: Direction.BOTH},
                  {label: t('floorPlan.directions.out'), value: Direction.OUT}
                ]"
                                rounded
                                size="0.5em"
                                @update:model-value="$emit('changeDirection', direction)"
                                :readonly="isConfigOut"
                        />
                    </template>
                </q-field>
                <q-icon class="q-mr-xs" color="accent" size="1.25em" name="info_outlined">
                    <q-tooltip max-width="15em" anchor="center right" self="center left">
            {{t('floorPlan.infoConfigGroups')}}
                    </q-tooltip>
                </q-icon>
            </q-toolbar>
            <q-card-section>
        <q-input filled v-model="configDescription" :label="t('floorPlan.configDescription')" stack-label/>
            </q-card-section>
            <q-card-section v-for="(select,i) in qSelects.configParts">
                <q-card bordered flat>
                    <q-toolbar class="bg-primary text-white shadow-2">
            <q-toolbar-title>{{ t('floorPlan.configGroup') }}</q-toolbar-title>
                        <q-btn flat round icon="delete" size="0.75em" @click="removeConfigGroup(i)"/>
                    </q-toolbar>
                    <q-card-section class="column">
                        <q-select
                                class="q-mb-sm"
                                filled
                                multiple
                                label="Credentials"
                                option-label="name"
                :hint="t('floorPlan.infoCredential')"
                                :options="credentialStore.allCredentials"
                                v-model="qSelects.configParts[i].credentials"
                                use-chips>
                            <template v-slot:after>
                                <q-icon class="cursor-pointer" size="0.75em" name="filter_none">
                                    <q-tooltip max-width="15em" anchor="center right" self="center left">
                    {{t('floorPlan.directions.infoCredentialGroups')}}
                                    </q-tooltip>
                                    <q-menu anchor="bottom right" self="top right" transition-show="jump-down" transition-hide="jump-up" style="background-color: var(--bg-color)">
                                        <q-list dense>
                                            <q-item-label header class="text-bold text-primary" >
                                                {{ t('common.categories') }}
                                            </q-item-label>
                                            <q-item @click="addCategory(i, category)" v-for="category in credentialStore.categories" v-close-popup clickable>
                                                <q-item-section>
                                                    {{category.name}}
                                                </q-item-section>
                                            </q-item>
                                        </q-list>
                                    </q-menu>
                                </q-icon>
                            </template>
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
                        <div class="row q-mt-sm justify-around no-wrap"
                             v-for="(attributeFilter,j) in qSelects.configParts[i].attributeFilter" style="min-width: 100%">
                            <q-select style="width: 12em"
                                      rounded outlined dropdown-icon="expand_more"
                                      v-model="qSelects.configParts[i].attributeFilter[j].attribute"
                                      :options="commonAttributeFilter(qSelects.configParts[i].credentials)"
                                      :display-value="qSelects.configParts[i].attributeFilter[j].attribute?.label"
                                      @update:model-value="resetPredicateType(i,j)">

                            </q-select>
                            <q-select style="width: 5em"
                                      rounded outlined dropdown-icon="expand_more"
                                      v-model="qSelects.configParts[i].attributeFilter[j].predicateType" ref="predicateType"
                                      :options="getPredicates(qSelects.configParts[i].attributeFilter[j].attribute)"/>
                            <q-input style="width: 10em"
                                     bottom-slots rounded outlined v-model="qSelects.configParts[i].attributeFilter[j].value"
                                     :type="getType(qSelects.configParts[i].attributeFilter[j].attribute)"
                                     :disable="qSelects.configParts[i].attributeFilter[j].currentDate" ref="input">
                                <template v-slot:hint>
                                    <q-checkbox
                      :label="t('floorPlan.currentTime')"
                                            dense
                                            size="2em"
                                            v-model="qSelects.configParts[i].attributeFilter[j].currentDate"
                                            v-if="qSelects.configParts[i].attributeFilter[j].attribute?.type.toLowerCase() === 'date'"
                                            @update:model-value="setDate(i,j)">
                                    </q-checkbox>
                                </template>
                            </q-input>
                            <q-btn style="width: 4em; height: 4em"
                                   flat round icon="delete" @click="removeFilter(i,j)"/>
                        </div>
                    </q-card-section>
                    <q-btn class="q-ml-sm q-mb-sm" flat dense rounded color="primary" icon="add"
                           @click="addAttributeFilter(i)">
            {{t('floorPlan.addAttribute')}}
                    </q-btn>
                </q-card>
            </q-card-section>
            <q-btn class="q-ml-sm q-mb-sm" flat dense rounded color="primary" icon="add" @click="addConfigurationGroup">
        {{t('floorPlan.addConfigGroup')}}
            </q-btn>
        </q-card>
    </q-card-section>
</template>

<script>
import {Direction, PredicateType} from "@/main/vue/entity/doorConfiguration";
import {ref} from "vue";
import {useCredentialStore} from "@/main/vue/stores/credential";
import {useI18n} from "vue-i18n";

export default {
    name: "DoorConfig",
    computed: {
        Direction() {
            return Direction
        }
    },

    props: {
        isConfigOut: {
            required: false
        },
        doorConfig: {
            required: false
        },
        direction: {
            required: false
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
        addCategory(i, category) {
            let credentials = this.qSelects.configParts[i].credentials
            category.credentials.forEach(ic => {
                if (credentials.filter(c => c.name === ic.name).length === 0)
                    credentials.push(ic)
            })
            category.externalCredentials.forEach(ec => {
                if (credentials.filter(c => c.name === ec.name).length === 0)
                    credentials.push(ec)
            })
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
        const credentialStore = useCredentialStore()
        credentialStore.getAllCredentials()
        credentialStore.getCategory()
        const credentials = ref()
        const configDescription = ref()
        const direction = ref(Direction.BOTH)
        const {t} = useI18n()

        if (props.direction) {
            direction.value = props.direction;
        }


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

        if (props.doorConfig) {
            configDescription.value = props.doorConfig.description
            qSelects.value = props.doorConfig
        }

        const commonAttributeFilter = function (credentials) {
            let formEntries = credentials.map((credential) => {
                return credential.form
            })
            if (formEntries.length > 1) {
                return formEntries.shift().filter((v) => {
                    return formEntries.every((a) => {
                        return a.some(ele => ele.attributeName === v.attributeName);
                    });
                })
            }
            return formEntries[0];
        }

        return {
            direction,
            configDescription,
            credentials,
            credentialStore,
            qSelects,
            commonAttributeFilter,
            t
        }
    }
}
</script>

<style scoped>

</style>