<template>
  <q-page-container class="no-padding no-margin">
    <q-page style="padding-right: 1em; padding-top: 2em">
      <q-tabs
          v-model="tab"
          class="text-grey"
          active-color="primary"
          indicator-color="primary"
          align="justify">
        <q-tab name="rooms" :label="t('floorPlan.rooms')"/>
        <q-tab name="groups"
               v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted)"
               :label="t('editor.groupRooms.groups')"/>
        <div v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted)"
             class="row justify-center" style="width: 3em">
          <q-icon size="1.25em" fixed-right name="info_outlined" color="info" class="q-pl-xs">
            <q-tooltip style="background-color: var(--bg-color); color: var(--text-color); font-size: 1em"
                       anchor="bottom right" max-width="200px" self="top middle"
                       :offset="[0, 0]">
              {{ t('editor.groupRooms.info') }}
            </q-tooltip>
          </q-icon>
        </div>

      </q-tabs>
      <q-separator/>

      <q-tab-panels v-model="tab" animated
                    style="width: 22em; background-color: var(--bg-color); color: var(--text-color)">
        <q-tab-panel name="rooms">
          <q-tab-panels v-model="roomTab" animated
                        style="background-color: var(--bg-color); color: var(--text-color)">
            <q-tab-panel name="list" class="no-padding" style="width: 100%">
              <div class="column justify-between no-wrap" style="height: 8.5em">
                <q-input
                    style="width: 100%"
                    :placeholder="t('common.search')"
                    v-model="search"
                    @update:model-value="roomFilter"
                    clearable
                    outlined
                    rounded
                    clear-icon="clear">
                  <template v-slot:append>
                    <q-icon name="search"/>
                  </template>
                </q-input>
                <q-select
                    style="width: 100%"
                    :label="t('common.filter')"
                    v-model="filteredCredential"
                    @update:model-value="roomFilter"
                    :display-value="filteredCredential?.name"
                    :options="credentialsStore.credentials.concat(credentialsStore.externalCredentials)"
                    :input-class="''"
                    option-value="credentialDefinitionId"
                    clearable
                    multiple
                    option-label="name"
                    outlined
                    rounded
                    hide-dropdown-icon
                    clear-icon="clear">
                </q-select>
              </div>
              <q-scroll-area style="width: 100%; height: 21em" class="q-mt-sm">
                <q-list>
                  <q-item v-for="room in filteredRooms" style="padding-left: 0">
                    <q-checkbox v-model="selectedRooms" :val="room.id"
                                color="info"/>
                    <q-btn
                        flat
                        class="full-width"
                        :label="room.name"
                        @click="toggleRoomCheckbox(room)">
                    </q-btn>

                    <q-btn
                        flat
                        icon="chevron_right"
                        style="width: 3em; height: 3em"
                        @click="roomClick(room)"/>
                  </q-item>
                </q-list>
              </q-scroll-area>
              <div
                  v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted) && edit">
                <q-separator/>
                <div class="column justify-around no-wrap q-pa-sm" style="height: 6em">
                  <q-btn
                      flat dense rounded
                      color="primary"
                      @click="checkAddRoomsToNewGroup()">
                    <div class="row no-wrap">
                      <q-icon name="add" left/>
                      {{ t('editor.groupRooms.addRoomsToNewGroup') }}
                    </div>
                  </q-btn>
                  <q-btn
                      flat dense rounded
                      color="primary"
                      @click="addRoomsToExistingGroupDialog=checkIfGroupSelected()">
                    <div class="row no-wrap">
                      <q-icon name="add" left/>
                      {{ t('editor.groupRooms.addRooms') }}
                    </div>
                  </q-btn>
                </div>


              </div>
            </q-tab-panel>
            <q-tab-panel class="no-padding" name="info">
              <room-detail-view ref="roomDetail" @doorChanged="$emit('doorChanged')" :room="room"
                                @back-clicked="back()"/>
            </q-tab-panel>
          </q-tab-panels>
        </q-tab-panel>
        <q-tab-panel name="groups" style="width: 100%">
          <q-input
              class="q-mb-xs"
              :placeholder="t('common.search')"
              v-model="searchGroup"
              @update:model-value="groupFilter"
              clearable
              outlined
              rounded
              clear-icon="clear"
              style="width: 100%">
            <template v-slot:append>
              <q-icon name="search"/>
            </template>
          </q-input>
          <q-scroll-area style="width: 100%; height: 28.5em"
                         @scroll="giveLog();dropdown = false">
            <q-list>

              <q-item v-for="group in allGroups" style="padding-left: 0">
                <q-radio @click="filterRoomToGroups(); updateNumRoomsInGroup();"
                         v-model="selectedGroups" :val="group"
                         color="info"/>
                <q-btn
                    flat
                    class="full-width"
                    :label="group.name"
                    @click="selectGroup(group); filterRoomToGroups(); updateNumRoomsInGroup();">
                </q-btn>
                <q-btn-dropdown
                    flat
                    menu-anchor="bottom right"
                    style="width: 3em; height: 3em"
                    dropdown-icon="expand_more"
                    @click="selectGroup(group); filterRoomToGroups(); updateNumRoomsInGroup();">

                  <div class="column no-wrap" style="background-color: var(--bg-color); color: var(--text-color)">
                    <div class="row no-wrap" style="min-width: 18em">
                      <div class="column no-wrap q-pl-lg" style="padding: 0.5em">
                        <q-list>
                          <q-item-label>{{ t("floorPlan.roomAmount") }}:</q-item-label>
                        </q-list>
                      </div>
                      <div class="column no-wrap" style="padding: 0.5em">
                        <q-list>
                          <q-item-label>{{ numRoomsInGroup }}</q-item-label>

                        </q-list>
                      </div>
                    </div>
                    <div
                        v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted) && edit">
                      <q-separator/>

                      <div class="row justify-center" style="padding: 0.5em">
                        <p class="cursor-pointer q-mb-none"
                           :style="{color: getCssVar('primary')}"
                           @click="reloadRoomsBE(); setOldValueG(group); allFloorsForGroup()">
                          {{ t('common.edit') }}
                        </p>
                        <q-dialog v-model="editGroupD" persistent>
                          <q-card style="width: 20em; background-color: var(--bg-color); color: var(--text-color)">
                            <q-card-section>
                              <div class="text-h6">
                                {{ t("floorPlan.editGroup") }}
                              </div>
                            </q-card-section>
                            <q-card-section>
                              <q-input filled v-model="currentGroupName"
                                       :label="t( 'floorPlan.groupName')"
                                       stack-label
                                       style="width: 100%; padding-bottom: 1em"/>
                              <q-list bordered class="rounded-borders">
                                <q-scroll-area
                                    style="height: 20em; max-width: 100%;">
                                  <q-item-label header>
                                    {{ t("floorPlan.rooms") }}
                                  </q-item-label>
                                  <q-item
                                      v-for="(room, i) in group.rooms"
                                      class="row justify-around no-wrap">

                                    <div class="column" style="width: 50%">
                                      <q-item-label class="text-bold">
                                        {{ room.name }}
                                      </q-item-label>
                                      <q-item-label caption>
                                        {{ t('editor.groupRooms.floor') }}
                                        {{ arrayFloors[i] }}
                                      </q-item-label>
                                    </div>
                                    <q-btn
                                        icon="delete"
                                        dense flat round
                                        style="width: 3em; height: 3em; color: var(--light)"
                                        @click="addToDeleteList(room);"/>
                                  </q-item>
                                </q-scroll-area>
                              </q-list>
                              <q-btn
                                  :label="t('editor.groupRooms.doorconfiguration')"
                                  color="primary"
                                  class="q-mt-md"
                                  style="width: 100%"
                                  @click="openGroupDoorDialog"
                                  rounded/>
                            </q-card-section>

                            <q-card-actions align="right" class="row no-wrap text-primary">
                              <q-btn
                                  flat rounded :label="t( 'common.cancel')"
                                  color="primary"
                                  @click="unCheck(); cancelEdit();"
                                  v-close-popup/>
                              <q-btn
                                  flat rounded :label="t( 'common.save')"
                                  color="primary"
                                  @click="editGroupName(); updateNumRoomsInGroup();"/>
                            </q-card-actions>
                          </q-card>
                        </q-dialog>
                      </div>
                    </div>

                  </div>
                </q-btn-dropdown>
              </q-item>
            </q-list>
          </q-scroll-area>
          <div
              v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted) && edit">
            <q-separator/>
            <div class="row justify-evenly no-wrap q-pa-sm">
              <q-btn
                  style="width: 12em"
                  color="primary"
                  @click="delName(); newGroup = true;"
                  rounded flat dense>
                <div class="row no-wrap">
                  <q-icon name="add" left/>
                  {{ t('editor.groupRooms.newGroup') }}
                </div>
              </q-btn>
              <q-btn
                  color="grey"
                  icon="delete"
                  size="0.8em"
                  @click="deleteAlert = checkGroupSelected();"
                  round flat/>
            </div>


          </div>

        </q-tab-panel>
      </q-tab-panels>

      <q-dialog v-model="newGroup">
        <q-card style="background-color: var(--bg-color); color: var(--text-color)">
          <q-card-section>
            <div class="text-h6">{{ t('editor.groupRooms.nameOfGroup') }}</div>
          </q-card-section>
          <q-card-section class="q-pt-none">
            <q-input dense v-model="newGroupName" autofocus @keyup.enter="prompt = false"/>
          </q-card-section>

          <q-card-actions align="right" class="text-primary">
            <q-btn flat :label="t('common.cancel')" @click="" v-close-popup/>
            <q-btn flat :label="t('common.save')" @click="makeANewGroup(newGroupName);"/>
          </q-card-actions>
        </q-card>
      </q-dialog>
      <q-dialog v-model="addRoomsToExistingGroupDialog">
        <q-card style="background-color: var(--bg-color); color: var(--text-color)">
          <q-card-section>
            <div class="text-h6">{{ t('editor.groupRooms.addRooms') }}</div>
          </q-card-section>
          <q-card-section>
            {{ t('editor.groupRooms.addRoomsToSelected', [selectedGroups.name]) }}
          </q-card-section>
          <q-card-actions align="right" class="text-primary">
            <q-btn flat :label="t('common.cancel')" @click="" v-close-popup/>
            <q-btn flat :label="t('common.save')" @click="addRoomsToGroups();" v-close-popup/>
          </q-card-actions>
        </q-card>
      </q-dialog>

      <q-dialog v-model="addRoomsToNewGroupDialog">
        <q-card style="background-color: var(--bg-color); color: var(--text-color)">
          <q-card-section>
            <div class="text-h6">Name der Gruppe</div>
          </q-card-section>
          <q-card-section class="q-pt-none">
            <q-input dense v-model="newGroupName" autofocus @keyup.enter="prompt = false"/>
          </q-card-section>

          <q-card-actions align="right" class="text-primary">
            <q-btn flat :label="t('common.cancel')" @click="" v-close-popup/>
            <q-btn flat :label="t('common.save')" @click="addRoomsToNewGroup();"/>
          </q-card-actions>
        </q-card>
      </q-dialog>
    </q-page>
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

  </q-page-container>
</template>

<script>
import {useFloorPlanStore} from "@/main/vue/stores/floorPlan";
import {useUserStore} from "@/main/vue/stores/users";
import {ref, watch} from "vue";
import {useI18n} from 'vue-i18n';
import {storeToRefs} from "pinia";
import {useRoomStore} from "@/main/vue/stores/room";
import DoorConfig from "@/main/vue/views/DoorConfig.vue";
import {getCssVar, useQuasar} from "quasar";
import {useRoomGroupStore} from "@/main/vue/stores/roomGroupStore";
import {useLocationStore} from "@/main/vue/stores/locations";
import MultipleRoomsDoorConfigDialog from "@/main/vue/views/MultipleRoomsDoorConfigDialog.vue";
import RoomDetailView from "@/main/vue/views/RoomDetailView.vue";
import {useCredentialsStore} from "@/main/vue/stores/credential";


export default {
  components: {RoomDetailView, DoorConfig},
  props: {
    edit: {
      type: Boolean,
      required: false
    }
  },
  methods: {
    getConfig() {
      return this.$refs.doorConfig.qSelects
    },
    getCssVar,
    roomClick(room) {
      this.roomTab = 'info';
      this.room = room
    },
    back() {
      this.roomTab = 'list';
    },
    refreshDetail() {
      this.$refs?.roomDetail?.load()
    }
  },
  name: "FloorPlanRoomList",
  emits: ["doorChanged"],
  setup(props, context) {
    const {t} = useI18n();
    const floorPlanStore = useFloorPlanStore()
    const {rooms, selectedRooms} = storeToRefs(floorPlanStore)
    const userStore = useUserStore()
    const credentialsStore = useCredentialsStore()
    const roomStore = useRoomStore();
    const filteredRooms = ref([])
    const search = ref()
    const searchGroup = ref()
    const inception = ref();
    const editDoorDialog = ref();
    const editedDoorName = ref();
    const currentRoomName = ref();
    const $q = useQuasar();
    const roomTab = ref("list");
    const room = ref();
    const filteredCredential = ref();

    const roomGroupStore = useRoomGroupStore();
    const selectedGroups = ref([]);
    let filteredGroups = ref([]);
    const newGroup = ref(false);
    const addRoomsToNewGroupDialog = ref(false);
    const currentGroupName = ref();
    const editGroupD = ref(false);
    const numRoomsInGroup = ref();
    const isEditor = ref(false);
    const roomDeleteList = ref([]);

    credentialsStore.fetch()

    function isEditorCheck() {
      if (userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted)) {
        isEditor.value = true;
        console.log("Is Editor Check");
      }

      console.log("Is Editor Check");
      return true;
    }


    isEditorCheck();

    async function start() {
      if (isEditor.value) {
        await roomGroupStore.getRoomGroups();
        console.log("Ist eingeloggt als Editor");
      } else {
        console.log("Ist nicht eingeloggt als Editor");
      }
    }

    start();


    async function loadRoomGroups(buildingID) {
      //filteredGroups.value = [];
      unCheck();
      console.log("loadRoomGroups(): roomGroupStore.filteredGroups", roomGroupStore.filteredGroups);
      roomGroupStore.getGroupByBuilding(buildingID).then(() => {
        filteredGroups.value = [];
        for (const roomG of roomGroupStore.filteredGroups) {
          filteredGroups.value.push(roomG);
        }

      })
    }


    async function makeANewGroup(newGroupName) {
      await checkName(newGroupName);
      //selectedGroups.value = [];
      if (checkNameAllowed.value) {

        await roomGroupStore.makeNewGroup(newGroupName, currentBuilding.value, []);
        await loadRoomGroups(buildingID.value);
        newGroup.value = false;

      }
    }

    const selectedRoomsForGroup = ref([]);

    async function makeIdsToRooms() {
      selectedRoomsForGroup.value = [];
      await roomStore.getRooms();

      selectedRoomsForGroup.value = roomStore.rooms?.filter((room) =>
          selectedRooms.value.includes(room.id)
      )
      console.log("selectedRoomsForGroup: ", selectedRoomsForGroup.value);

    }

    async function addRoomsToNewGroup() {
      //console.log(selectedRooms.value);
      await checkName(newGroupName.value);
      if (checkNameAllowed.value) {


        await makeIdsToRooms();

        await roomGroupStore.makeNewGroup(newGroupName.value, currentBuilding.value, selectedRoomsForGroup.value);
        await loadRoomGroups(buildingID.value);

        addRoomsToNewGroupDialog.value = false;
        newGroupName.value = "";
        selectedRooms.value = [];
        console.log("done makeANewGroup for selected rooms");

      }
    }

    function checkAddRoomsToNewGroup() {
      if (selectedRooms.value.length === 0) {
        $q.notify({
          type: 'negative',
          message: t('editor.groupRooms.noRoomSelected')
        })
      } else {
        addRoomsToNewGroupDialog.value = true;
      }
    }

    function checkIfGroupSelected() {
      console.log("Selected Group for new Group: ", selectedGroups.value);
      if (selectedGroups.value !== null) {
        return true;
      } else {
        $q.notify({
          type: 'negative',
          message: t('editor.groupRooms.noGroupSelected')
        })
        return false;
      }
    }

    async function addRoomsToGroups() {

      if (selectedRooms.value.length === 0) {
        $q.notify({
          type: 'negative',
          message: t('editor.groupRooms.noRoomSelected')
        })
      } else if (selectedGroups.value !== null) {

        await makeIdsToRooms();
        const editedGroup = ref({
          id: selectedGroups.value.id,
          name: selectedGroups.value.name,
          building: selectedGroups.value.building,
          rooms: selectedRoomsForGroup.value
        });
        await roomGroupStore.editGroup(editedGroup.value);
        console.log("makeIsToRooms before loadRoomGroups: selectedGroups.value", selectedGroups.value)
        await loadRoomGroups(buildingID.value);
        console.log("makeIsToRooms after loadRoomGroups: selectedGroups.value", selectedGroups.value)
        unCheck();
        console.log("makeIsToRooms after unCheck: selectedGroups.value", selectedGroups.value)

      } else {
        $q.notify({
          type: 'negative',
          message: t('editor.groupRooms.noGroupSelected')
        })
      }
    }

    async function deleteGroup() {
      if (selectedGroups.value === []) {
        unCheck();
      }
      if (selectedGroups.value !== null) {
        roomGroupStore.deleteGroup(selectedGroups.value.id).then(async () => {
          await loadRoomGroups(buildingID.value);
          unCheck();
        });
      } else {
        $q.notify({
          type: 'negative',
          message: t('groupRooms.noGroupSelected')
        })
      }
    }

    function checkGroupSelected() {
      if (selectedGroups.value !== null) {
        return true;
      } else {
        $q.notify({
          type: 'negative',
          message: t('editor.groupRooms.noGroupSelected')
        })
        return false;
      }
    }

    const checkNameAllowed = ref(false);

    async function checkName(newName) {
      checkNameAllowed.value = false;
      if (newName.trim() === "") {
        $q.notify({
          type: 'negative',
          message: t('editor.groupRooms.checkNameMessage'),
          caption: t('editor.groupRooms.checkNameCaption'),
          color: 'negative',
          textColor: 'positive'
        })
      } else if (newName.length >= 25) {
        $q.notify({
          type: 'negative',
          message: t('issuer.issueCredential.validation.ruleErrors.lessThanLength', ['25']),
          color: 'negative',
          textColor: 'positive'
        })
      } else {
        checkNameAllowed.value = true
        for (const theGroup of filteredGroups.value) {
          if (theGroup.name === newName) {
            $q.notify({
              type: 'negative',
              message: 'Name für dieses Gebäude bereits vergeben',
              caption: 'Bitte wählen Sie einen neuen Namen.',
              color: 'negative',
              textColor: 'positive'
            })
            checkNameAllowed.value = false;
          }
        }
      }
    }

    const locationStore = useLocationStore();
    const currentBuilding = ref(null);

    function getParentIDs(locations, selectFloorId) {
      for (const location of locations) {
        for (const building of location.buildings) {
          if (building.floors.some(floor => floor.id === selectFloorId)) {
            currentBuilding.value = building;
            return building.id
          }
        }
      }
      return null;
    }

    let buildingID = ref();
    locationStore.getLocations().then(async (locations) => {
      if (!floorPlanStore.selectedFloorPlan) {
        buildingID.value = locations[0].buildings[0].id
      } else {
        buildingID.value = getParentIDs(locations, floorPlanStore.selectedFloorId);
      }
      if (isEditor.value) {
        await loadRoomGroups(buildingID.value);
      }
    });


    const {selectedFloorId} = storeToRefs(floorPlanStore)
    let prevBuildingid = null;

    if (isEditor.value) {
      watch(selectedFloorId, async () => {
        locationStore.getLocations().then(async (locations) => {
          if (!floorPlanStore.selectedFloorPlan) {
            buildingID.value = locations[0].buildings[0].id
          } else {
            buildingID.value = getParentIDs(locations, floorPlanStore.selectedFloorId);
            if (prevBuildingid !== buildingID.value) {
              console.log("prev B und current b: ", prevBuildingid, buildingID.value);
              await loadRoomGroups(buildingID.value);
              selectedRooms.value = [];
            }
          }
          prevBuildingid = buildingID.value;


        });
      })
    }


    filteredRooms.value = rooms.value

    watch(rooms, () => {
      filteredRooms.value = rooms.value
    })

    function setOldValueR(room) {
      inception.value = true;
      currentRoomName.value = room.name;
    }

    function setOldValueG(group) {
      editGroupD.value = true;
      currentGroupName.value = group.name;
      selectedGroups.value = group;
    }

      function openGroupDoorDialog() {
          $q.dialog({
              component: MultipleRoomsDoorConfigDialog,
              componentProps: {
                  group: selectedGroups.value.id
              }
          }).onOk(() => {
              console.log("success")
          })
      }

    async function save(room) {
      room.name = currentRoomName.value;
      await roomStore.save(room)
      context.emit('editRoom', room)
    }


    async function editGroupName() {
      let prevName = selectedGroups.value.name;
      if (prevName !== currentGroupName.value) {
        await checkName(currentGroupName.value);
      } else {
        checkNameAllowed.value = true;
      }
      //selectedGroups.value = [];
      if (checkNameAllowed.value) {

        editGroupD.value = false

        const editedGroup = ref({
          id: selectedGroups.value.id,
          name: currentGroupName.value,
          building: selectedGroups.value.building,
          rooms: selectedGroups.value.rooms
        });
        roomGroupStore.editGroup(editedGroup.value).then(async () => {
          await loadRoomGroups(buildingID.value);
          unCheck();
        });
      }

    }

    function addRoom(element) {
      selectedRooms.value.push(element)
    }

    function deleteRoom(element) {
      selectedRooms.value.forEach((item, index) => {
        if (item === element) selectedRooms.value.splice(index, 1);
      });
    }

    function updateNumRoomsInGroup() {
      if (selectedGroups.value !== null) {
        numRoomsInGroup.value = selectedGroups.value.rooms.length;
      }
    }

    function toggleRoomCheckbox(element) {
      if (selectedRooms.value.some(e => e === element.id)) {
        deleteRoom(element.id)
      } else {
        addRoom(element.id)
      }
    }

    async function roomFilter() {
      if (!search.value || search.value.trim() === '') {
        filteredRooms.value = floorPlanStore.rooms
      } else if (search.value) {
        const request = search.value.toLowerCase().trim()
        filteredRooms.value = floorPlanStore.rooms.filter(room => {
          return room.name.toLowerCase().includes(request)
        })
      }

      if (filteredCredential.value?.length > 0) {
        const definitionIds = filteredCredential.value.map(credential => credential.credentialDefinitionId)
        filteredRooms.value = filteredRooms.value.filter(r => {
          let doorContainsCredential = false
          r.doors.forEach((door) => {
            let activeConfig = getActiveBaseConf(door);

            if (activeConfig) {
              let configContains = true;
              for (const attribute in activeConfig?.proofConfigIn.requestedAttributes) {
                configContains = configContains && activeConfig.proofConfigIn
                    .requestedAttributes[attribute]
                    .restrictions
                    .some(res => definitionIds.includes(res.credentialDefinitionId))
              }
              for (const attribute in activeConfig?.proofConfigIn.requestedPredicates) {
                configContains = configContains && activeConfig.proofConfigIn
                    .requestedPredicates[attribute]
                    .restrictions
                    .some(res => definitionIds.includes(res.credentialDefinitionId))
              }
              doorContainsCredential = doorContainsCredential || configContains
            }
          })
          return doorContainsCredential;
        })
      }
    }

    function getActiveBaseConf(door) {
      if (door.doorConfigs.length > 1) {
        const currentDate = new Date();

        for (const doorConfig of door.doorConfigs) {
          if (!doorConfig?.baseConfig) {

            const startTime = new Date();
            startTime.setHours(doorConfig?.startTime?.split(":")[0], doorConfig?.startTime?.split(":")[1])
            const endTime = new Date();
            endTime.setHours(doorConfig?.endTime?.split(":")[0], doorConfig?.startTime?.split(":")[1])
            if (startTime < currentDate && endTime > currentDate) {
              return doorConfig
            }
          }
        }

        for (const doorConfig of door.doorConfigs) {
          if (doorConfig.baseConfig) {
            return doorConfig;
          }
        }
      } else {
        return door.doorConfigs[0]
      }

    }

    async function groupFilter() {
      if (!searchGroup.value || searchGroup.value.trim() === '') {
        filteredGroups.value = roomGroupStore.filteredGroups;
      } else if (searchGroup.value) {
        const request = searchGroup.value.toLowerCase().trim()
        filteredGroups.value = roomGroupStore.filteredGroups.filter(group => {
          return group.name.toLowerCase().includes(request)
        })
      }
    }

    function unCheck() {
      selectedGroups.value = null
      selectedRooms.value = [];
    }

    const prevSelectedGroup = ref();

    function filterRoomToGroups() {
      if (prevSelectedGroup.value === null || prevSelectedGroup.value === undefined
          || (prevSelectedGroup.value !== selectedGroups.value)) {
        selectedGroups.value.rooms.forEach((room) => {
          toggleRoomCheckbox(room);
        })
      } else {
        // to un-toggle the selected rooms
        selectedGroups.value.rooms.forEach((room) => {
          toggleRoomCheckbox(room);
        })
        unCheck();
      }

      prevSelectedGroup.value = selectedGroups.value;
    }

    const newGroupName = ref('');

    const arrayFloors = ref([]);

    async function allFloorsForGroup() { //ab hier schon zu viele Räume drin (Bugfixing)
      const rooms = selectedGroups.value.rooms;
      arrayFloors.value = [];

      for (let roomLength = 0; roomLength < rooms.length; roomLength++) {

        await roomStore.getFloor(rooms[roomLength].id);
        let level = roomStore.floor.floorLevel
        arrayFloors.value.push(level);
        //console.log("Room:", rooms[roomLength], "Floor", level);
      }
      console.log("arrayFloors:", arrayFloors.value);
      console.log("function: allFloorsForGroup()");
    }

    function addToDeleteList(room) {
      roomDeleteList.value.push(room);
      console.log("add to delete list: room:", room);
      let i = 0;
      for (const roomFromGroup of selectedGroups.value.rooms) {
        console.log("For");
        if ((i === 0) && (room.id === roomFromGroup.id)) {
          selectedGroups.value.rooms.shift();
        } else if (room.id === roomFromGroup.id) {
          console.log("i: ", i);
          //selectedGroups.value.rooms.pop(roomFromGroup);
          selectedGroups.value.rooms.splice(i, 1);
          console.log("ohne ", selectedGroups.value.rooms);
        }
        i++;
      }
      console.log("entire delete-list:", roomDeleteList.value);
      console.log("selected Group rooms:", selectedGroups.value.rooms);
    }

    const dropdown = ref(false);

    return {
        openGroupDoorDialog,
      floorPlanStore,
      selectedRooms,
      toggleRoomCheckbox,
      userStore,
      search,
      searchGroup,
      filteredRooms,
      roomFilter,
      groupFilter,
      t,
      inception,
      editedDoorName,
      editDoorDialog,
      roomStore,
      save,
      editGroupName,
      currentRoomName,
      currentGroupName,
      setOldValueR,
      setOldValueG,
      room,
      roomTab,
      tab: ref('rooms'),
      allGroups: filteredGroups,
      selectedGroups,
      newGroup,
      addRoomsToNewGroupDialog,
      addRoomsToExistingGroupDialog: ref(false),
      newGroupName,
      makeANewGroup,
      addRoomsToGroups,
      checkAddRoomsToNewGroup,
      checkIfGroupSelected,
      addRoomsToNewGroup,
      unCheck,
      deleteGroup,
      checkGroupSelected,
      editGroupD,
      deleteAlert: ref(false),
      filterRoomToGroups,
      numRoomsInGroup,
      updateNumRoomsInGroup,
      delName() {
        newGroupName.value = "";
      },
      selectGroup(group) {
        selectedRooms.value = [];
        selectedGroups.value = group;
      },
      giveLog() {
        dropdown.value = false;
        //console.log("hiwerhoiehfowgiohowhoifwohfoweihfiosiohfi");
        //console.log(dropdown.value);
      },
      allFloorsForGroup,
      credentialsStore,
      filteredCredential,
      dropdown,
      arrayFloors,
      addToDeleteList,
      async reloadRoomsBE() {
        if (selectedGroups.value !== null) {
          console.log("sel. Id", selectedGroups.value.id);
          await roomGroupStore.getRoomsByGroupId(selectedGroups.value.id);
          console.log("hier", roomGroupStore.rooms);
          selectedGroups.value.rooms = roomGroupStore.rooms;
          console.log("reloadRoomsBE(), selectedGroups", selectedGroups.value);
        }
      },
      async cancelEdit() {
        await loadRoomGroups(buildingID.value);
      }
    }
  },

}
</script>

<style scoped>

</style>