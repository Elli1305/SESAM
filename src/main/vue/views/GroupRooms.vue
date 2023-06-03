<template>
    <div class="q-pa-md">

        <div class="q-mb-xl">
            <div class="items-center justify-center row">
                <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">
                    {{ t("groupRooms.title") }}</h1>
            </div>

        </div>
        <div class="items-center justify-center" style="display: flex">
            <div class="center" style="max-width: 80em; min-width: 60em">

                <q-table
                        :rows="rows"
                        :columns="columns"
                        row-key="username"
                        :separator="'cell'"
                        :filter="filter"
                        :filter-method="customFilter"
                >

                    <template v-slot:body-cell-actions="props">
                        <q-td :props="props">
                            <q-btn dense round flat color="grey"
                                   @click="editGroup(Object.values(props)); getOldName(); editAlert = true"
                                   test="props.value" icon="edit"></q-btn>
                            <q-btn dense round flat color="grey" icon="delete"
                                   @click="deleteAlert = true; delGroup(Object.values(props))"></q-btn>

                        </q-td>
                    </template>

                    <template v-slot:top-right>
                        <q-input borderless dense debounce="300" v-model="filter.search"
                                 :placeholder="t( 'adminCurrentUser.search')">
                            <template v-slot:append>
                                <q-icon name="search"/>
                            </template>
                        </q-input>
                    </template>


                    <template v-slot:top-left >
                        <q-select
                                filled
                                v-model="modelForLocation"
                                use-input
                                input-debounce="0"
                                :label="t( 'groupRooms.chooseLocation')"
                                :options="optionsLocations"
                                option-label="name"

                                @filter="filterLocations"
                                @update:model-value="changed"
                                style="width: 280px"
                                behavior="menu"
                        >
                            <template v-slot:no-option>
                                <q-item>
                                    <q-item-section class="text-grey">
                                        No results
                                    </q-item-section>
                                </q-item>
                            </template>
                        </q-select>
                        <q-space style="width: 30px"></q-space>


                        <q-select
                                filled
                                v-model="modelForBuilding"
                                use-input
                                input-debounce="0"
                                :label="t( 'groupRooms.chooseBuilding')"
                                :options="buildingListNames"
                                option-label="name"
                                @filter="filterBuilding"
                                @update:model-value="getBuilding"
                                style="width: 280px"
                                behavior="menu"
                        >
                            <template v-slot:no-option>
                                <q-item>
                                    <q-item-section class="text-grey">
                                        No results
                                    </q-item-section>
                                </q-item>
                            </template>
                        </q-select>

                        <q-space style="width: 30px"></q-space>


                        <q-btn dense round icon="add" label="Neue Gruppe" flat color="primary" stretch
                               @click="newGroup = true"></q-btn>
                    </template>
                </q-table>


            </div>
        </div>

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
                <q-btn flat :label="t('adminEdit.delete')" @click="deleteGroup()"/>
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
                <q-btn flat label="Speichern" @click="checkName(editName); updateCurrentGroup(editName,modelRoomsNew)" />
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
                    <q-btn flat label="Speichern" @click="checkName(newGroupName);"/>
                    <q-btn flat :label="t('adminEdit.back')" @click="toDefault()" v-close-popup/>
                </q-card-actions>
            </q-card>
        </q-dialog>
    </div>
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
            await roomGroupStore.getRoomGroups().then(() => {
                console.log(roomGroupStore.allRoomGroups);
                for (const roomG of roomGroupStore.allRoomGroups) {
                    roomGroupList.push(roomG);
                }
                console.log("Groups of rooms");

                //rows.value.push(roomGroupList);
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
            if(newName.trim() === ""){
                $q.notify({
                    type:'negative',
                    message: 'Name darf nicht leer sein',
                    caption: 'Mindestens ein Buchstabe, eine Ziffer oder ein Zeichen.'
                })
            }
        }

        function toDefault(){
            this.newGroupName=ref('');
            this.modelRooms=ref(null);

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

        function setCurrentGroup(GroupId, GroupName, GroupRooms, GroupBuilding) {
            currentGroup.value.name = GroupName;
            currentGroup.value.id = GroupId;
            currentGroup.value.building = GroupBuilding
            currentGroup.value.rooms = GroupRooms;
            console.log("currentGroup: ", currentGroup.value);
        }
        function updateCurrentGroup(GroupName, GroupRooms) {
            currentGroup.value.name = GroupName;
            currentGroup.value.rooms = GroupRooms;
            console.log("currentGroup: ", currentGroup.value);
            saveGroup();
        }
        function deleteGroup(Group) {
            roomGroupStore.deleteGroup(Group.id);
        }
        function saveGroup() {
            roomGroupStore.save(currentGroup.value);
        }

        return {
            deleteAlert: ref(false),
            editAlert: ref(false),

            newGroup: ref(false),
            newGroupName: ref(''),
            editName:ref(''),
            modelRooms: ref(null),
            modelRoomsNew: ref(editGroupRooms.value),
            checkName,
            updateCurrentGroup,
            deleteGroup,
            saveGroup,
            locationList,
            buildingListNames,
            toDefault,
            getOldName() {
              this.editName = editGroupName;
              this.modelRoomsNew= editGroupRooms;
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
            editGroup(value) {
              editGroupName = value[1].name;
              editGroupRooms = value[1].rooms;
              console.log(editGroupRooms);
              console.log(editGroupName);
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