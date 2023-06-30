<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em" >
    <p class="row text-h3 justify-center">{{t("groupRooms.title")}}</p>
    <div class="row self-center">
      <q-table
          style="width: 80vw; height: 50vh"
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
                  color="grey"
                  @click="getInfosForEditGroup(Object.values(props)); getOldName(); updateRoomList(currentBuilding); editAlert = true"
                  test="props.value"
                  icon="edit">
                <span class="material-icons-outlined"></span>
              </q-btn>
              <q-btn
                  dense
                  round
                  flat
                  icon="delete"
                  color="grey"
                  @click="deleteAlert = true; delGroup(Object.values(props));"/>
            </div>
          </q-td>
        </template>
        <template v-slot:body-cell-doorconfig="props">
          <q-td :props="props">
            <q-btn
                dense
                round
                flat
                color="grey"
                @click="prompt=true; openForm(props.row); getRoomsAndDoors(editedRow.id)"
                test="props.value"
                icon="edit">
            </q-btn>
          </q-td>
        </template>

        <template class="row no-wrap" v-slot:top-right>
          <q-btn
              flat
              rounded
              icon="add"
              color="grey"
              :label="t('groupRooms.new')"
              @click="newGroup = true; toDefault(); updateRoomList(currentBuilding);"
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
              :label="t( 'groupRooms.chooseBuilding')"
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
      <q-card>
        <q-card-section>
          <div class="text-h6">{{ t('adminEdit.attention') }}</div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          {{ t('groupRooms.question') }}
        </q-card-section>
        <q-card-actions align="right" class="text-primary">
          <q-btn flat :label="t('adminEdit.back')" v-close-popup/>
          <q-btn flat :label="t('adminEdit.delete')" @click="deleteGroup(); deleteAlert=false"/>
        </q-card-actions>
      </q-card>
    </q-dialog>

    <q-dialog v-model="editAlert">
      <q-card>
        <q-card-section>
          <div class="text-h6">Name der Gruppe</div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <q-input dense v-model="editName" autofocus @keyup.enter="prompt = false"/>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <div class="text-h6">{{t('groupRooms.chooseRooms')}}</div>
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
          <q-btn flat :label="t('adminEdit.back')" v-close-popup/>
          <q-btn flat label="Speichern" @click="updateCurrentGroup(editName,modelRoomsNew);"
                 v-close-popup="closeEditAlert"/>
        </q-card-actions>
      </q-card>


    </q-dialog>

    <q-dialog v-model="newGroup">
      <q-card>
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
          <q-btn flat :label="t('adminEdit.back')" @click="toDefault()" v-close-popup/>
          <q-btn flat :label="t('adminEdit.save')" @click="checkName(newGroupName); makeNewGroup(newGroupName,modelRooms);"/>
        </q-card-actions>
      </q-card>
    </q-dialog>

    <q-dialog v-model="prompt" persistent>
      <q-card style="width: 90%">
        <q-toolbar class="bg-primary text-accent">
          <q-toolbar-title>{{t("groupRooms.groupsConfig")}}</q-toolbar-title>
        </q-toolbar>
        <q-card-section>
          <q-table
              flat bordered
              :rows-per-page-options="[0]"
              :title="t('groupRooms.roomSelection')"
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
                <q-toggle v-model="visibleColumns" val="doorNames" :label="t('groupRooms.doors')" ></q-toggle>
              </div>
              <q-input class="q-ml-xs" borderless dense debounce="250" v-model="searchinput" :placeholder="t('credentialview.search')">
                <template v-slot:append>
                  <q-icon name="search" />
                </template>
              </q-input>
            </template>
            <template v-slot:body-cell-doorNames="props">
              <q-td :props="props" v-if="props.row.name">
                <q-select
                    @popup-show="getDoors(props.row.room)"
                    class="q-my-sm"
                    filled
                    emit-value
                    v-model="model[props.row.room]"
                    multiple
                    :options=options
                    option-value="id"
                    option-label="name"
                    options-cover
                    map-options
                />
              </q-td>
            </template>
            <template v-slot:body-cell-actions="props">
              <q-td :props="props">
                <q-checkbox v-model="props.row.name" @update:model-value="getDoors(props.row.room); checkIfSelected(props.row.name, props.row.room)"/>
              </q-td>
            </template>
          </q-table>
        </q-card-section>
        <door-config ref="configIn" :door-config="doorConfigIn"
                     :direction="JSON.stringify(this.doorConfigIn) !== JSON.stringify(this.doorConfigOut) ? Direction.IN : Direction.BOTH"
                     @changeDirection="changeDirectionOut"></door-config>
        <door-config v-show="$refs.configIn?.direction !== Direction.BOTH" ref="configOut"
                     :direction="JSON.stringify(this.doorConfigIn) !== JSON.stringify(this.doorConfigOut) ? Direction.OUT : Direction.BOTH"
                     :door-config="doorConfigOut" :is-config-out="true"></door-config>
        <q-card-actions align="right" class="text-primary">
          <q-btn flat v-close-popup> {{ t("credentialmapping.cancel")}}</q-btn>
          <q-btn flat v-close-popup @click="saveConfig(model)"> {{ t("credentialmapping.save")}}</q-btn>
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script>

import {ref} from 'vue'

import {useI18n} from "vue-i18n";
import {useQuasar} from "quasar";
import {useLocationStore} from "@/main/vue/stores/locations";
import {useDoorStore} from "@/main/vue/stores/door";
import {prop} from "vue-class-component";
import {useRoomGroupStore} from "@/main/vue/stores/roomGroupStore";
import {Direction} from "@/main/vue/entity/doorConfiguration";
import DoorConfig from "@/main/vue/views/DoorConfig.vue";




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
    const options =ref([]);
    let buildingList = ref([]);
    const currentBuilding = ref(null);
    let buildingListNames = ref([]);
    let buildingListForFilter=ref([]);
    let listOfAllRoomsViaBuilding = ref([]);
    const checkNameAllowed = ref(false);
    const closeNow = ref(false);
    const closeEditAlert = ref(false);
    const list = [];
    const model = ref([[]])
    const res = []
    let finalArray = []

    const columns = [
      { name: 'name', required: true, label: t('groupRooms.group'), align: 'center', field: row => row.name, sortable: true },
      { name: 'rooms', align: 'center', label: t('groupRooms.rooms'), field: row => row.rooms.map(r => r.name).join(", "),  sortable: true},
      { name: 'actions', label: t('issuermanagement.edit'), style: 'width: 8em', headerStyle: 'width: 8em', align: 'center'},
      { name: 'doorconfig', label: t('groupRooms.doorconfig'), style: 'width: 8em', headerStyle: 'width: 8em', align: 'center'},
    ]

    const rows = ref([]);


    rows.value = []

    const columns2 = [
      { name: 'name', required: true, label: t('groupRooms.room'), align: 'left', field: row => row.roomName, sortable: true },
      { name: 'room', label: t('groupRooms.room'), align: 'left', field: row => row.room, sortable: true },
      { name: 'actions', label: t('groupRooms.select'), style: 'width: 8em', headerStyle: 'width: 8em', align: 'left'},
      { name: 'doorNames', label: t('groupRooms.doors'), field: row => row.doors, format: (val) => val.map(e => e.id).join(', '), style: 'width: 8em', headerStyle: 'width: 8em', align: 'left'},
      { name: 'doors', label: t('groupRooms.doors'), field: row => row.doors, format: (val) => val.map(e => e.id).join(', '), style: 'width: 8em', headerStyle: 'width: 8em', align: 'left'}
    ]

    const rows2 =  ref([]);
    rows2.value = []

    let currentGroup=ref({
      id: null,
      name: null,
      building: null,
      rooms: []
    });

    function getRoomsAndDoors(id) {
      roomGroupStore.getRoomsAndDoorsByGroupId(id).then((rooms) => rows2.value= rooms)
    }

    const editedRow = ref({})
    const openForm = (row) => {
      editedRow.value = {...row};
    }

    function getDoors(id) {
      doorStore.getByRoomId(id).then((doors) =>{
        options.value = doors;
        model.value[id] = options.value.map(obj => (obj['id']))
      })
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
      if(buildingListNames.value !==null) {
        modelForBuilding.value = buildingListNames.value[0];
        currentBuilding.value = buildingListNames.value[0];
        updateRoomList(currentBuilding.value);
      }
    }


    async function checkName(newName) {
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
      for(const theGroup of rows.value) {
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
    }

    function updateRoomList(building){
      listOfAllRoomsViaBuilding.value=[];
      for(const floor of building.floors) {
        for(const rooms of floor.rooms) {
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
      if(!(currentGroup.value.name === editGroupName.value)) {
        await checkName(currentGroup.value.name);
      }
      else {
        checkNameAllowed.value = true;
      }
      if(checkNameAllowed.value) {
        await roomGroupStore.editGroup(currentGroup.value);
        await loadRoomGroups();
        editAlert.value = false;
      }
    }
    async function deleteGroup() {
      if(currentGroup.value !==null) {
        await roomGroupStore.deleteGroup(currentGroup.value.id).then(() => {
          loadRoomGroups();
        });

      }
    }

    return {
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
      newGroupName: ref(''),
      editName:ref(''),
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
      visibleColumns: ref(['name', 'actions', 'doorNames']),
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
        this.modelRoomsNew= editGroupRooms.value;
      },
      changed(val){
        if(val !==null) {
          adjustBuildingList(val.name);
          adaptRoomGroupsToBuilding();
        }
        else {
          buildingListNames.value=[];
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
    useRoomGroupStore,


    changeDirectionOut(direction) {
      if (direction === Direction.IN) {
        this.$refs.configOut.direction = Direction.OUT
      } else if (direction === Direction.OUT) {
        this.$refs.configOut.direction = Direction.IN
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
            if ((s1)|| s2) {
              ans = true
            }
            return ans
          })
      return filteredRows
    },
    saveConfig(model) {
      const roomGroupStore = useRoomGroupStore()
      const res = []
        for (const val in model) {
          if (val && val !=0) {
            res.push(model[val])
          }
        }
      const finalArray = res.flat(1)
      let config = {}
      console.log('qselects', this.$refs.configIn.qSelects)
      const configlist = []
      if (this.$refs.configIn.direction === Direction.BOTH) {
        config.doorConfigIn = JSON.parse(JSON.stringify(this.$refs.configIn.qSelects))
        config.doorConfigOut = JSON.parse(JSON.stringify(this.$refs.configIn.qSelects))
        config.doorConfigIn.description = this.$refs.configIn.configDescription
        config.doorConfigOut.description = this.$refs.configIn.configDescription
      } else if (this.$refs.configIn.direction === Direction.IN) {
        config.doorConfigIn = JSON.parse(JSON.stringify(this.$refs.configIn.qSelects))
        config.doorConfigOut = JSON.parse(JSON.stringify(this.$refs.configOut.qSelects))
        config.doorConfigIn.description = this.$refs.configIn.configDescription
        config.doorConfigOut.description = this.$refs.configOut.configDescription
      } else if (this.$refs.configIn.direction === Direction.OUT) {
        config.doorConfigIn = JSON.parse(JSON.stringify(this.$refs.configOut.qSelects))
        config.doorConfigOut = JSON.parse(JSON.stringify(this.$refs.configIn.qSelects))
        config.doorConfigIn.description = this.$refs.configOut.configDescription
        config.doorConfigOut.description = this.$refs.configIn.configDescription
      }
      config.doorConfigIn?.configParts.forEach(part => part.credentials = part.credentials.map(credential => credential.credentialDefinitionId))
      config.doorConfigOut?.configParts.forEach(part => part.credentials = part.credentials.map(credential => credential.credentialDefinitionId))
      for (const door in finalArray) {
        configlist.push({
          doorId: finalArray[door],
          configuration: config
        })
        console.log(configlist)
        roomGroupStore.setGroupConfig(configlist)
      }
    }
  },


}
</script>

<style scoped>

</style>