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
                            <q-btn dense round flat color="grey" @click="editGroup(Object.values(props)); getOldName(); editAlert = true"
                                   test="props.value" icon="edit"></q-btn>
                            <q-btn dense round flat color="grey" icon="delete" @click="deleteAlert = true"></q-btn>

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
                                v-model="model"
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
                <q-btn flat :label="t('adminEdit.delete')" @click=""/>
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
                        :options="optionsRooms"

                    />
                </div>
            </div>

            <q-card-actions align="right" class="text-primary">
                <q-btn flat label="Speichern" @click="checkName(editName)" />
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

const columns = [
    {
        name: 'groupname',
        required: true,
        label: 'Gruppen',
        align: 'center',
        field: row => row.groupname,
        sortable: true,
        headerStyle: 'max-width: 50px',
        //headerClasses: 'bg-primary text-white',
    },
    {name: 'rooms', align: 'center', label: "Räume", field: row => row.rooms.join(", "),  sortable: true},
    {name: 'actions', label: 'Bearbeiten', style: 'max-width 10px', headerStyle: 'max-width: 20px', align: 'center'}
]

const rows = ref([]);

rows.value = [

    {
        groupname: 'Frozen Yogurt',
        rooms: ['Room 1', 'Room 7'],
        location: 'ExampleLocation',
        building: 'Building 0',
    },
    {
        groupname: 'Labore',
        rooms: ['Room 2', 'Room 5', 'Room 7', 'Room 13'],
        location: 'ExampleLocation',
        building: 'Building 1',
    },
]

const stringOptions = [
    'Gebäude 1', 'Gebäude 2'
]
const optionsRooms = []
for (let i = 0; i <= 100; i++) {
    optionsRooms.push('Room ' + i)
}


export default {
    name: "GroupRooms",
    setup() {

        const {t} = useI18n();
        const $q = useQuasar();
        const locationStore = useLocationStore()
        const options = ref(stringOptions)
        let optionsLocations = ref([])
        let editGroupName = ref(null);
        let editGroupRooms = ref(null);
        let locationList = [];
        let locationListNames = [];
        let buildingList = ref([]);
        let buildingListNames = ref([]);
        let buildingListForFilter=ref([]);
        let listOfAllRoomsViaBuilding = ref([]);

        async function loadLocations() {
            await locationStore.getLocations().then((locations) => {
                for (const loc of locations) {
                    locationList.push(loc);
                }
                console.log(typeof locationList[0].name);
                for(const loc of locationList) {
                    locationListNames.push(loc.name);
                }
                optionsLocations.value = locationList;
                console.log("hallo");
                console.log(optionsLocations);
                console.log(locationListNames);
            }).then(() => {
                console.log(optionsLocations.value);
                console.log("halalal");
                console.log(optionsLocations.value);
            }
            )

            console.log(locationListNames);

            console.log("hier");
            //console.log(locationList);
            //console.log("hier Ende");

        }

        loadLocations();


        function adjustBuildingList(nameLoc) {
            buildingList.value=[];
            for(const loc of locationList){
                if(loc.name===nameLoc) {
                    buildingList.value.push(loc.buildings);
                    console.log("sucess")
                }
                console.log(loc.name);
            }
            for(const building of buildingList.value) {
                buildingListNames.value = building;
                buildingListForFilter.value=building;
            }
            console.log("Buildings");
            console.log(buildingList.value);
            console.log(buildingListNames.value);
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

        return {
            deleteAlert: ref(false),
            editAlert: ref(false),

            newGroup: ref(false),
            newGroupName: ref(''),
            editName:ref(''),
            modelRooms: ref(null),
            modelRoomsNew: ref(editGroupRooms.value),
            optionsRooms,
            checkName,
            loadLocations,
            locationList,
            buildingListNames,
            toDefault,
            adjustBuildingList,
            getOldName() {
              this.editName = editGroupName;
              this.modelRoomsNew= editGroupRooms;
            },
            changed(val){
                if(val !==null) {
                    adjustBuildingList(val.name);
                    console.log("Change", val.name)
                }
                else {
                    buildingListNames.value=[];
                }
            },

            getBuilding(building) {
                console.log(building);
                updateRoomList(building);
            },

            filter: ref({
                search: ''
            }),
            columns,
            rows,
            t,
            //for selecting the building:
            model: ref(null),
            modelForLocation: ref(null),
            stringOptions,
            options,
            optionsLocations,
            listOfAllRoomsViaBuilding,
            editGroup(value) {
              editGroupName = value[1].groupname;
              editGroupRooms = value[1].rooms;
              console.log(editGroupRooms);
              console.log(editGroupName);
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

                    //If search term exists, convert to lower case and see which rows contain it
                    if (lowerSearch !== "") {
                        s1 = false
                        //Get the values
                        let s1_values = Object.values(row)
                        //Convert to lowercase
                        let s1_lower = s1_values.map(x => x.toString().toLowerCase())

                        for (let val = 0; val < s1_lower.length; val++) {
                            if (s1_lower[val].includes(lowerSearch)) {
                                s1 = true
                                break
                            }
                        }
                    }
                    //assume row doesn't match
                    ans = false
                    //check if any of the conditions match
                    if ((s1)) {
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