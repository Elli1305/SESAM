<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{ t("editor.groupRooms.title") }}</p>
    <div class="row self-center">
      <q-table
          style="width: 80vw; height: 50vh; background-color: var(--bg-color); color: var(--text-color)"
          :rows-per-page-options="[0]"
          :rows="rows"
          :columns="columns"
          :separator="'cell'"
          :no-data-label="t('common.noData')"
          :no-results-label="t('common.noResults')"
          :pagination-label="getPaginationLabel"
          row-key="username"
          :filter="filter"
          :filter-method="customFilter">

        <template v-slot:body-cell-actions="props">
          <q-td :props="props">
            <div class="row justify-evenly no-wrap">
              <q-btn
                  dense
                  round
                  flat
                  style="color: var(--light)"
                  @click="getInfosForEditGroup(Object.values(props)); getOldName(); updateRoomList(currentBuilding); editAlert = true"
                  test="props.value">
                <q-icon name="edit"/>
              </q-btn>
              <q-btn
                  dense
                  round
                  flat
                  style="color: var(--light)"
                  @click="prompt=true; openForm(props.row); getRoomsAndDoors(editedRow.id); resetConfig()"
                  test="props.value"
                  icon="meeting_room"/>
              <q-btn
                  dense
                  round
                  flat
                  icon="delete"
                  style="color: var(--light)"
                  @click="deleteAlert = true; delGroup(Object.values(props));"/>
            </div>
          </q-td>
        </template>

        <template class="row no-wrap" v-slot:top-right>
          <q-btn
              flat
              rounded
              icon="add"
              :label="t('editor.groupRooms.newGroup')"
              @click="newGroup = true; toDefault(); updateRoomList(currentBuilding);"
              style="margin-right: 2em; color: var(--light)"/>
          <q-input outlined rounded dense debounce="300" v-model="filter.search"
                   :placeholder="t('common.search')">
            <template v-slot:append>
              <q-icon name="search"/>
            </template>
          </q-input>
        </template>


        <template class="row no-wrap" v-slot:top-left>
          <q-select
              :label="t('editor.groupRooms.chooseLocation')"
              behavior="menu"
              v-model="modelForLocation"
              borderless
              dense
              options-dense
              :options="optionsLocations"
              option-label="name"
              @filter="filterLocations"
              style="width: 12em; margin-right: 2em"
              @update:model-value="changed">
            <template v-slot:no-option>
              <q-item>
                <q-item-section class="text-grey">
                  No results
                </q-item-section>
              </q-item>
            </template>
          </q-select>
          <q-select
              :label="t('editor.groupRooms.chooseBuilding')"
              behavior="menu"
              v-model="modelForBuilding"
              borderless
              dense
              options-dense
              :options="buildingListNames"
              option-label="name"
              @filter="filterBuilding"
              style="width: 12em"
              @update:model-value="getBuilding">
            <template v-slot:no-option>
              <q-item>
                <q-item-section class="text-grey">
                  No results
                </q-item-section>
              </q-item>
            </template>
          </q-select>
        </template>
      </q-table>
    </div>

    <q-dialog v-model="deleteAlert">
      <q-card style="background-color: var(--bg-color); color: var(--text-color)">
        <q-card-section>
          <div class="text-h6">{{ t('editor.groupRooms.deleteGroup') }}</div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          {{ t('editor.groupRooms.question') }}
        </q-card-section>
        <q-card-actions align="right" class="text-primary">
          <q-btn flat :label="t('common.cancel')" v-close-popup/>
          <q-btn flat :label="t('common.delete')" @click="deleteGroup(); deleteAlert=false"/>
        </q-card-actions>
      </q-card>
    </q-dialog>

    <q-dialog v-model="editAlert">
      <q-card style="background-color: var(--bg-color); color: var(--text-color)">
        <q-card-section>
          <div class="text-h6">{{ t('editor.groupRooms.nameOfGroup') }}</div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <q-input dense v-model="editName" autofocus @keyup.enter="prompt = false"/>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <div class="text-h6">{{ t('editor.groupRooms.chooseRooms') }}</div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <q-select
              filled
              v-model="modelRoomsNew"
              multiple
              :options="listOfAllRoomsViaBuilding"
              option-label="name"/>
        </q-card-section>
        <q-card-actions align="right" class="text-primary">
          <q-btn flat :label="t('common.cancel')" v-close-popup/>
          <q-btn flat :label="t('common.save')" @click="updateCurrentGroup(editName,modelRoomsNew);"
                 v-close-popup="closeEditAlert"/>
        </q-card-actions>
      </q-card>


    </q-dialog>

    <q-dialog v-model="newGroup">
      <q-card style="background-color: var(--bg-color); color: var(--text-color)">
        <q-card-section>
          <div class="text-h6">Name der Gruppe</div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <q-input dense v-model="newGroupName" autofocus @keyup.enter="prompt = false"/>
        </q-card-section>
        <q-card-section>
          <div class="text-h6">Räume auswählen</div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <q-select
              filled
              v-model="modelRooms"
              multiple
              :options="listOfAllRoomsViaBuilding"
              option-label="name"/>
        </q-card-section>
        <q-card-actions align="right" class="text-primary">
          <q-btn flat :label="t('common.cancel')" @click="toDefault()" v-close-popup/>
          <q-btn flat :label="t('common.save')"
                 @click="checkName(newGroupName); makeNewGroup(newGroupName,modelRooms);"/>
        </q-card-actions>
      </q-card>
    </q-dialog>

    <q-dialog v-model="prompt" persistent>
      <q-card style="min-width: 50em; background-color: var(--bg-color); color: var(--text-color)">
        <q-card-section class="row justify-between">
          <div class="text-h6">{{ t("editor.groupRooms.groupsConfig") }}</div>
          <q-btn flat rounded size="0.9em">
            <q-icon name="content_copy" size="1em" left/>
            {{ t('floorPlan.chooseConfig') }}
            <q-menu style="background-color: var(--bg-color); color: var(--text-color)">
              <q-list>
                <q-item clickable v-close-popup v-for="config in configOptions" @click="selectedConfig = config">
                  <q-item-section>
                    <q-item-label>{{ config.name }}</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </q-menu>
          </q-btn>
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
                          map-options
                          use-chips
                />
              </q-td>
            </template>
            <template v-slot:body-cell-actions="props">
              <q-td style="width: 10%" :props="props">
                <q-checkbox v-model="props.row.selected"
                            @update:model-value="getDoors(props.row.room, props.row.selected); checkIfSelected(props.row.selected, props.row.room)"/>
              </q-td>
            </template>
          </q-table>
        </q-card-section>
        <q-card-section v-for="(selectConf,k) in qSelectGeneral.qSelectsSet">
          <q-card style="background-color: var(--bg-color); color: var(--text-color)" bordered flat>
            <q-toolbar class="bg-primary text-accent">
              <q-toolbar-title>{{t('floorPlan.config')}}</q-toolbar-title>
              <q-checkbox class="q-mr-lg" size="2em" keep-color dense v-model="qSelectGeneral.qSelectsSet[k].baseConfig"
                          :label="t('floorPlan.base')"
                          color="accent" @click="check(k)"/>
              <q-icon class="q-mr-xs" color="accent" size="1.25em" name="info_outlined">
                <q-tooltip style="background-color: var(--bg-color); color: var(--text-color); font-size: 1em" max-width="15em" anchor="center right" self="center left">
                  {{t('floorPlan.baseConfig')}}
                </q-tooltip>
              </q-icon>
              <q-td v-if=!(checkLength())>
                <q-btn flat round icon="delete" size="0.75em" @click="removeConfig(k)"/>
              </q-td>
            </q-toolbar>


            <div class="row justify-center q-mt-lg no-wrap"
                 v-if="!qSelectGeneral.qSelectsSet[k].baseConfig">
              <q-input outlined rounded v-model="qSelectGeneral.qSelectsSet[k].startTime" mask="time" :rules="['time']">
                <template v-slot:append>
                  <q-icon name="access_time" class="cursor-pointer">
                    <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                      <q-time text-color="accent" style="background-color: var(--bg-color); color: var(--text-color)"
                              v-model="qSelectGeneral.qSelectsSet[k].startTime">
                        <q-icon
                            class="cursor-pointer"
                            style="position: absolute; margin-top: -17.5em; margin-left: 11.7em"
                            v-close-popup
                            name="clear"
                            color="accent"
                            size="1.5em"/>
                      </q-time>
                    </q-popup-proxy>
                  </q-icon>
                </template>
              </q-input>
              <div class="column justify-center q-mx-lg" style="height: 4em">
                <span style="font-size: 2em; color: grey">&#8210</span>
              </div>
              <q-input outlined rounded v-model="qSelectGeneral.qSelectsSet[k].endTime" mask="time" :rules="['time']"
                       :disabled="qSelectGeneral.qSelectsSet[k].baseConfig">
                <template v-slot:append>
                  <q-icon name="access_time" class="cursor-pointer">
                    <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                      <q-time style="background-color: var(--bg-color); color: var(--text-color)"
                              v-model="qSelectGeneral.qSelectsSet[k].endTime">
                        <q-icon
                            class="cursor-pointer"
                            style="position: absolute; margin-top: -17.5em; margin-left: 11.7em"
                            v-close-popup
                            name="clear"
                            color="accent"
                            size="1.5em"/>
                      </q-time>
                    </q-popup-proxy>
                  </q-icon>
                </template>
              </q-input>
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

            <q-btn class="q-ml-sm q-mb-sm" flat dense rounded color="primary" icon="add" @click="addConfiguration">
              {{t('floorPlan.addConfig')}}
            </q-btn>
          </q-card>
        </q-card-section>
        <q-card-actions align="right" class="text-primary">
          <q-btn flat @click="resetConfig()" v-close-popup> {{ t("common.cancel") }}</q-btn>
          <q-btn flat :disable="!checkBaseConf()" v-close-popup @click="saveConfig(model)">
            {{
              t("common.save")
            }}
          </q-btn>
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script>

import {reactive, ref, watch} from 'vue'

import {useI18n} from "vue-i18n";
import {useQuasar} from "quasar";
import {useLocationStore} from "@/main/vue/stores/locations";
import {useDoorStore} from "@/main/vue/stores/door";
import {prop} from "vue-class-component";
import {useRoomGroupStore} from "@/main/vue/stores/roomGroupStore";
import {Direction} from "@/main/vue/entity/doorConfiguration";
import DoorConfig from "@/main/vue/views/DoorConfig.vue";
import {useConfigStore} from "@/main/vue/stores/config";
import {storeToRefs} from "pinia";


export default {
  name: "GroupRooms",
  components: {DoorConfig},
  computed: {
    Direction() {
      return Direction
    }
  },

  props: {
    doorConfigIn: {
      required: false
    },
    doorConfigOut: {
      required: false
    }
  },
  emits: [
    'ok'
  ],
  setup() {

    const {t} = useI18n();
    const $q = useQuasar();
    const locationStore = useLocationStore()
    const roomGroupStore = useRoomGroupStore()
    const doorStore = useDoorStore()
    let editAlert = ref(false);
    let newGroup = ref(false);
    let optionsLocations = ref([])
    let editGroupName = ref(null);
    let editGroupRooms = ref(null);
    let locationList = [];
    let locationListNames = [];
    let roomGroupList = [];
    const options = ref([]);
    let buildingList = ref([]);
    const currentBuilding = ref(null);
    let buildingListNames = ref([]);
    let buildingListForFilter = ref([]);
    let listOfAllRoomsViaBuilding = ref([]);
    const checkNameAllowed = ref(false);
    const closeNow = ref(false);
    const closeEditAlert = ref(false);
    const list = [];
    const model = ref([[]])
    const res = []
    const value = ref([])
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
      }
    })

    const check = (k) => {

      const baseConfCount = qSelectGeneral.qSelectsSet.filter(
          (config) => config.baseConfig
      ).length;
      if (baseConfCount > 1) {
        // Display warning or prevent saving
        $q.notify({
          type: 'negative',
          message: t('floorPlan.baseConfig'),
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

    const columns = [
      {
        name: 'name',
        required: true,
        label: t('editor.groupRooms.group'),
        align: 'center',
        field: row => row.name,
        sortable: true
      },
      {
        name: 'rooms',
        align: 'center',
        label: t('floorPlan.rooms'),
        field: row => row.rooms.map(r => r.name).join(", "),
        sortable: true
      },
      {name: 'actions', label: t('common.edit'), style: 'width: 8em', headerStyle: 'width: 8em', align: 'center'},
    ]

    const rows = ref([]);


    rows.value = []

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
      model.value = [[]]
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


    function checkIfSelected(room, id) {
      if (!room) {
        model.value[id] = []
      }
    }


    let modelForBuilding = ref(null);
    let modelForLocation = ref(null);

    async function loadLocations() {
      await locationStore.getLocations().then((locations) => {
        for (const loc of locations) {
          locationList.push(loc);
        }
        for (const loc of locationList) {
          locationListNames.push(loc.name);
        }
        optionsLocations.value = locationList;
      })
      if (optionsLocations.value !== null) {
        modelForLocation.value = optionsLocations.value[0].name;
        adjustBuildingList(modelForLocation.value);
      }
    }

    async function loadRoomGroups() {
      roomGroupList = [];
      await roomGroupStore.getRoomGroups().then(() => {
        for (const roomG of roomGroupStore.allRoomGroups) {
          roomGroupList.push(roomG);
        }
        adaptRoomGroupsToBuilding();
      })
    }

    function getPaginationLabel(firstRowIndex, endRowIndex, totalRowsNumber) {
      return firstRowIndex.toString() + "-" + endRowIndex.toString() + " / " + totalRowsNumber.toString()
    }

    function adaptRoomGroupsToBuilding() {
      rows.value = [];
      for (const roomGs of roomGroupList) {
        if (currentBuilding.value != null) {
          if (roomGs.building.id === currentBuilding.value.id) {
            rows.value.push(roomGs);
          }
        }
      }
    }

    loadLocations().then(() => loadRoomGroups());


    function adjustBuildingList(nameLoc) {
      buildingList.value = [];
      for (const loc of locationList) {
        if (loc.name === nameLoc) {
          buildingList.value.push(loc.buildings);
        }
      }
      for (const building of buildingList.value) {
        buildingListNames.value = building;
        buildingListForFilter.value = building;
      }
      if (buildingListNames.value !== null) {
        modelForBuilding.value = buildingListNames.value[0];
        currentBuilding.value = buildingListNames.value[0];
        updateRoomList(currentBuilding.value);
      }
    }


    async function checkName(newName) {
      checkNameAllowed.value = false;
      if (newName.trim() === "") {
        $q.notify({
          type: 'negative',
          message: t('editor.groupRooms.checkNameMessage'),
          caption: t('editor.groupRooms.checkNameCaption')
        })
      } else {
        checkNameAllowed.value = true;
      }
      for (const theGroup of rows.value) {
        if (theGroup.name === newName) {
          $q.notify({
            type: 'negative',
            message: t('editor.groupRooms.assigned'),
            caption: t('editor.groupRooms.newName')
          })
          checkNameAllowed.value = false;
        }
      }
    }

    function toDefault() {
      this.newGroupName = ref('');
      this.modelRooms = ref(null);
    }

    function updateRoomList(building) {
      listOfAllRoomsViaBuilding.value = [];
      for (const floor of building.floors) {
        for (const rooms of floor.rooms) {
          listOfAllRoomsViaBuilding.value.push(rooms)
        }
      }
    }

    function setCurrentGroup(groupId, groupName, groupRooms, groupBuilding) {
      currentGroup.value.name = groupName;
      currentGroup.value.id = groupId;
      currentGroup.value.building = groupBuilding
      currentGroup.value.rooms = groupRooms;
    }

    function updateCurrentGroup(groupName, groupRooms) {
      currentGroup.value.name = groupName;
      currentGroup.value.rooms = groupRooms;
      editGroup();
    }

    async function makeNewGroup(newGroupName, newGroupRooms) {
      if (checkNameAllowed.value) {
        await roomGroupStore.makeNewGroup(newGroupName, currentBuilding.value, newGroupRooms);
        await loadRoomGroups();
        newGroup.value = false;

      }
    }

    async function editGroup() {
      if (!(currentGroup.value.name === editGroupName.value)) {
        await checkName(currentGroup.value.name);
      } else {
        checkNameAllowed.value = true;
      }
      if (checkNameAllowed.value) {
        await roomGroupStore.editGroup(currentGroup.value);
        await loadRoomGroups();
        editAlert.value = false;
      }
    }

    async function deleteGroup() {
      if (currentGroup.value !== null) {
        await roomGroupStore.deleteGroup(currentGroup.value.id).then(() => {
          loadRoomGroups();
        });

      }
    }

    function fetchDoors(rows) {
      rows?.forEach(row => getDoors(row.room, true))
    }

    return {
      fetchDoors,
      check,
      qSelectGeneral,
      filterConfigOptions,
      configOptions,
      selectedConfig,
      editedRow,
      openForm,
      getRoomsAndDoors,
      finalArray,
      list,
      checkIfSelected,
      deleteAlert: ref(false),
      editAlert,
      model,
      closeNow,
      newGroup,
      getDoors,
      res,
      value,
      newGroupName: ref(''),
      editName: ref(''),
      modelRooms: ref(null),
      modelRoomsNew: ref(editGroupRooms.value),
      updateRoomList,
      checkName,
      checkNameAllowed,
      updateCurrentGroup,
      currentBuilding,
      makeNewGroup,
      deleteGroup,
      options,
      editGroup,
      editGroupName,
      editGroupRooms,
      closeEditAlert,
      visibleColumns: ref(['name', 'actions']),
      locationList,
      buildingListNames,
      group: ref([]),
      toDefault,
      rows2,
      searchinput: ref(''),
      columns2,
      val: ref(true),
      prompt: ref(false),
      getPaginationLabel,
      getOldName() {
        this.editName = editGroupName.value;
        this.modelRoomsNew = editGroupRooms.value;
      },
      changed(val) {
        if (val !== null) {
          adjustBuildingList(val.name);
          adaptRoomGroupsToBuilding();
        } else {
          buildingListNames.value = [];
        }
      },
      getBuilding(building) {
        currentBuilding.value = building;
        updateRoomList(building);
        adaptRoomGroupsToBuilding();
      },
      filter: ref({
        search: ''
      }),
      columns,
      rows,
      t,
      //for selecting the building:
      modelForBuilding,
      modelForLocation,
      optionsLocations,
      listOfAllRoomsViaBuilding,
      delGroup(value) {
        setCurrentGroup(value[1].id, value[1].name, value[1].rooms, value[1].building);
      },
      getInfosForEditGroup(value) {
        editGroupName.value = value[1].name;
        editGroupRooms.value = value[1].rooms;
        setCurrentGroup(value[1].id, value[1].name, value[1].rooms, value[1].building);
      },
      filterBuilding(val, update) {
        if (val === '') {
          update(() => {
            buildingListNames.value = buildingListForFilter.value
          })
          return
        }
        update(() => {
          const needle = val.toLowerCase()
          buildingListNames.value = buildingListForFilter.value.filter(v => v.name.toLowerCase().indexOf(needle) > -1)
        })
      },
      filterLocations(val, update) {
        if (val === '') {
          update(() => {
            optionsLocations.value = locationList
          })
          return
        }
        update(() => {
          const needle = val.toLowerCase()
          optionsLocations.value = locationList.filter(v => v.name.toLowerCase().indexOf(needle) > -1)
        })
      },
    }
  },

  methods: {
    useRoomGroupStore,
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
      let configList = {
        doorIds: finalArray,
        doorConfig: allConfig
      }
      roomGroupStore.setGroupConfig(configList)
      finalArray = []
    }
  },


}
</script>

<style scoped>

</style>