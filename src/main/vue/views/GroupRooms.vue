<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em" >
    <p class="row text-h3 justify-center">{{t("groupRooms.title")}}</p>
    <div class="row self-center">
      <q-table
          style="width: 75vw; height: 25em"
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
              <q-btn
                  dense
                  round
                  flat
                  icon="edit"
                  color="grey"
                  @click="getInfosForEditGroup(Object.values(props)); getOldName(); editAlert = true"
                  test="props.value"/>
              <q-btn
                  dense
                  round
                  flat
                  icon="delete"
                  color="grey"
                  @click="deleteAlert = true; delGroup(Object.values(props));"/>
          </q-td>
        </template>

        <template class="row no-wrap" v-slot:top-right>
          <q-btn
              flat
              rounded
              icon="add"
              color="grey"
              label="Neue Gruppe"
              @click="newGroup = true; toDefault()"
              style="margin-right: 2em"/>
          <q-input borderless dense debounce="300" v-model="filter.search"
                   :placeholder="t( 'adminCurrentUser.search')">
              <template v-slot:append>
                  <q-icon name="search"/>
              </template>
          </q-input>
        </template>


        <template class="row no-wrap" v-slot:top-left >
          <q-select
              :label="t( 'groupRooms.chooseLocation')"
              behavior="menu"
              v-model="modelForLocation"
              borderless
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
              :label="t( 'groupRooms.chooseBuilding')"
              behavior="menu"
              v-model="modelForBuilding"
              borderless
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

    <q-dialog
            v-model="deleteAlert"
    >
        <q-card style="width: 400px">
            <q-card-section>
                <div class="text-h6">{{ t('adminEdit.attention') }}</div>
            </q-card-section>

            <q-card-section class="q-pt-none">
                {{ t('groupRooms.question') }}
            </q-card-section>

            <q-card-actions align="right" class="text-primary">
                <q-btn flat :label="t('adminEdit.delete')" @click="deleteGroup(); deleteAlert=false"/>
                <q-btn flat :label="t('adminEdit.back')" v-close-popup/>
            </q-card-actions>
        </q-card>
    </q-dialog>

    <q-dialog
        v-model="editAlert"
    >
        <q-card style="width: 500px">
            <q-card-section>
                <div class="text-h6">Name der Gruppe</div>
            </q-card-section>
            <q-card-section class="q-pt-none">
                <q-input dense v-model="editName" autofocus @keyup.enter="prompt = false"/>
            </q-card-section>

            <q-card-section class="a-pt-none">
                <div class="text-h6">{{t('groupRooms.chooseRooms')}}</div>
            </q-card-section>

            <div class="q-pl-md q-pr-md q-pb-md" style="max-width: 500px">
                <div class="q-gutter-md">
                    <q-select
                        filled
                        v-model="modelRoomsNew"
                        multiple
                        :options="listOfAllRoomsViaBuilding"
                        option-label="name"

                    />
                </div>
            </div>

            <q-card-actions align="right" class="text-primary">
                <q-btn flat label="Speichern" @click="updateCurrentGroup(editName,modelRoomsNew);"
                v-close-popup="closeEditAlert"/>
                <q-btn flat :label="t('adminEdit.back')" v-close-popup/>
            </q-card-actions>
        </q-card>


    </q-dialog>

  <!--    the dialog for adding a new group-->
    <div class="q-pa-md q-gutter-sm">

        <q-dialog v-model="newGroup">
            <q-card style="width: 500px">


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

                    <div class="q-pa-md" style="max-width: 500px">
                        <div class="q-gutter-md">
                            <q-select
                                filled
                                v-model="modelRooms"
                                multiple
                                :options="listOfAllRoomsViaBuilding"
                                option-label="name"

                            />
                        </div>
                    </div>

                </q-card-section>

                <q-card-actions align="right" class="text-primary">
                    <q-btn flat label="Speichern"
                           @click="checkName(newGroupName); makeNewGroup(newGroupName,modelRooms);"/>
                    <q-btn flat :label="t('adminEdit.back')" @click="toDefault()" v-close-popup/>
                </q-card-actions>
            </q-card>
        </q-dialog>
    </div>
  </q-page>
</template>

<script>

import {ref} from 'vue'

import {useI18n} from "vue-i18n";
import {useQuasar} from "quasar";
import {useLocationStore} from "@/main/vue/stores/locations";
import {prop} from "vue-class-component";
import {useRoomGroupStore} from "@/main/vue/stores/roomGroupStore";

const columns = [
    {
        name: 'name',
        required: true,
        label: 'Gruppen',
        align: 'center',
        field: row => row.name,
        sortable: true,
        headerStyle: 'max-width: 50px',
        //headerClasses: 'bg-primary text-white',
    },
    {name: 'rooms', align: 'center', label: "Räume", field: row => row.rooms.map(r => r.name).join(", "),  sortable: true},
    {name: 'actions', label: 'Bearbeiten', style: 'max-width 10px', headerStyle: 'max-width: 20px', align: 'center'}
]

const rows = ref([]);

rows.value = [

 /*   {
        name: 'Frozen Yogurt',
        rooms: ['Room 1', 'Room 7'],
        location: 'ExampleLocation',
        building: 'Building 0',
    },
    {
        name: 'Labore',
        rooms: ['Room 2', 'Room 5', 'Room 7', 'Room 13'],
        location: 'ExampleLocation',
        building: 'Building 1',
    },*/
]



export default {
    name: "GroupRooms",
    setup() {

        const {t} = useI18n();
        const $q = useQuasar();
        const locationStore = useLocationStore()
        const roomGroupStore = useRoomGroupStore()
        let editAlert = ref(false);
        let newGroup = ref(false);
        let optionsLocations = ref([])
        let editGroupName = ref(null);
        let editGroupRooms = ref(null);
        let locationList = [];
        let locationListNames = [];
        let roomGroupList = [];
        let buildingList = ref([]);
        const currentBuilding = ref(null);
        let buildingListNames = ref([]);
        let buildingListForFilter=ref([]);
        let listOfAllRoomsViaBuilding = ref([]);
        const checkNameAllowed = ref(false);
        const closeNow = ref(false);
        const closeEditAlert = ref(false);
        let currentGroup=ref({
            id: null,
            name: null,
            building: null,
            rooms: []
        });

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
            console.log("Locations");
            console.log(optionsLocations.value);
            if (optionsLocations.value !== null) {
                modelForLocation.value = optionsLocations.value[0].name;
                adjustBuildingList(modelForLocation.value);
            }
        }
        async function loadRoomGroups() {
            roomGroupList = [];
            await roomGroupStore.getRoomGroups().then(() => {
                console.log(roomGroupStore.allRoomGroups);
                for (const roomG of roomGroupStore.allRoomGroups) {
                    roomGroupList.push(roomG);
                }
                console.log("Groups of rooms");
                console.log(roomGroupList);

                adaptRoomGroupsToBuilding();
            })
        }

        function adaptRoomGroupsToBuilding() {
            rows.value = [];
            for (const roomGs of roomGroupList) {
                console.log("roomGs ", roomGs.building)
                console.log("Current building: " , currentBuilding.value)
                if (currentBuilding.value != null) {
                    if (roomGs.building.id === currentBuilding.value.id) {
                        rows.value.push(roomGs);
                    }
                }
            }
        }

        loadLocations().then(()=>loadRoomGroups());




        function adjustBuildingList(nameLoc) {
            buildingList.value=[];
            for(const loc of locationList){
                if(loc.name===nameLoc) {
                    buildingList.value.push(loc.buildings);
                }
            }
            for(const building of buildingList.value) {
                buildingListNames.value = building;
                buildingListForFilter.value=building;
            }
            console.log("Buildings");
            console.log(buildingListNames.value);
            if(buildingListNames.value !==null) {
                modelForBuilding.value = buildingListNames.value[0];
                currentBuilding.value = buildingListNames.value[0];
                console.log(modelForBuilding.value.id);
                updateRoomList(currentBuilding.value);
            }
        }


        async function checkName(newName) {
            console.log(newName);
            console.log("room Group List:", roomGroupList);
            checkNameAllowed.value = false;
            if(newName.trim() === ""){
                $q.notify({
                    type:'negative',
                    message: 'Name darf nicht leer sein',
                    caption: 'Mindestens ein Buchstabe, eine Ziffer oder ein Zeichen.'
                })
            }

            else {
                checkNameAllowed.value = true;
            }
            for(const theGroup of roomGroupList) {
                console.log("roomGroup:", theGroup)
                console.log("newName: ", newName);
                if (theGroup.name === newName) {
                    $q.notify({
                        type:'negative',
                        message: 'Name für dieses Gebäude bereits vergeben',
                        caption: 'Bitte wählen Sie einen neuen Namen.'
                    })
                    checkNameAllowed.value = false;
                }
            }
        }

        function toDefault(){
            this.newGroupName=ref('');
            this.modelRooms=ref(null);
            console.log("hier default");

        }
        function updateRoomList(building){
            listOfAllRoomsViaBuilding.value=[];
            for(const floor of building.floors) {
                for(const rooms of floor.rooms) {
                    listOfAllRoomsViaBuilding.value.push(rooms)
                }
            }
            console.log("Räume:")
            console.log(listOfAllRoomsViaBuilding.value);
        }

        function setCurrentGroup(groupId, groupName, groupRooms, groupBuilding) {
            currentGroup.value.name = groupName;
            currentGroup.value.id = groupId;
            currentGroup.value.building = groupBuilding
            currentGroup.value.rooms = groupRooms;
            console.log("currentGroup: ", currentGroup.value);
        }
        function updateCurrentGroup(groupName, groupRooms) {
            currentGroup.value.name = groupName;
            currentGroup.value.rooms = groupRooms;
            console.log("currentGroup: ", currentGroup.value);
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
            console.log("currentGroup.value.name: ", currentGroup.value.name, " EditGroupName.value: ", editGroupName.value);
            console.log((currentGroup.value.name === editGroupName.value))
            if(!(currentGroup.value.name === editGroupName.value)) {
                await checkName(currentGroup.value.name);
            }
            else {
                checkNameAllowed.value = true;
            }
            console.log("currentGroup.value.name: ", currentGroup.value.name, " EditGroupName.value: ", editGroupName.value);
            if(checkNameAllowed.value) {
                await roomGroupStore.editGroup(currentGroup.value);
                await loadRoomGroups();
                editAlert.value = false;

                console.log(closeEditAlert.value);
            }
        }
        async function deleteGroup() {
            console.log("Delete Group", currentGroup.value);
            if(currentGroup.value !==null) {
                await roomGroupStore.deleteGroup(currentGroup.value.id).then(() => {
                    loadRoomGroups();
                });

            }
        }

        return {
            deleteAlert: ref(false),
            editAlert,
            closeNow,

            newGroup,
            newGroupName: ref(''),
            editName:ref(''),
            modelRooms: ref(null),
            modelRoomsNew: ref(editGroupRooms.value),
            checkName,
            checkNameAllowed,
            updateCurrentGroup,
            makeNewGroup,
            deleteGroup,
            editGroup,
            editGroupName,
            editGroupRooms,
            closeEditAlert,
            locationList,
            buildingListNames,
            toDefault,
            getOldName() {
              this.editName = editGroupName.value;
              this.modelRoomsNew= editGroupRooms.value;
            },
            changed(val){
                if(val !==null) {
                    adjustBuildingList(val.name);
                    console.log("Change", val.name);
                    adaptRoomGroupsToBuilding();
                }
                else {
                    buildingListNames.value=[];
                }
            },

            getBuilding(building) {
                console.log("Changed building", building);
                currentBuilding.value = building;
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
              console.log("EditGroupRooms in getInfosForEditGroup: ", editGroupRooms.value);
              console.log("EditGroupName in getInfosForEditGroup: ", editGroupName.value);
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
                        optionsLocations.value=locationList
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
        prop,
        customFilter(rows, terms) {
            // rows contain the entire data
            // terms contains whatever you have as filter

            console.log(terms, rows)

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
                    if ((s1)|| s2) {
                        ans = true
                    }
                    return ans
                })
            return filteredRows
        }
    },


}
</script>

<style scoped>

</style>