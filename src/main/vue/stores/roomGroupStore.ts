import {defineStore} from "pinia";
import {RoomGroup, RoomsAndDoors} from "@/main/vue/entity/roomGroup";
import {ref, Ref} from "vue";
import api from "@/main/vue/api";
import {Building} from "@/main/vue/entity/roomGroup";
import {Category} from "@/main/vue/entity/credentialDefinition";
import {TwoWayDoorConfiguration} from "@/main/vue/entity/doorConfiguration";


export const useRoomGroupStore = defineStore('roomGroups', () => {

    const allRoomGroups: Ref<RoomGroup[]> = ref([])
    const filteredGroups: Ref<RoomGroup[]> = ref([])
    let roomGroupByName: Ref<RoomGroup | null> = ref(null)
    const roomsAndDoors: Ref<RoomsAndDoors[] | null> = ref(null)

    function getRoomGroups(): Promise<RoomGroup[]> {
        return new Promise((resolve, reject) => {
            api.roomGroups.getRoomGroups().then((response) => {
                allRoomGroups.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function getGroupByBuilding(buildingID: bigint): Promise<RoomGroup[]> {
        return new Promise((resolve,reject) => {
            api.roomGroups.getGroupByBuilding(buildingID).then((response) => {
                filteredGroups.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function save(roomGroup: RoomGroup): Promise<RoomGroup> {
        return new Promise((resolve, reject) => {

            api.roomGroups.save(roomGroup).then((response) => {
                getRoomGroups();
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }
    function makeNewGroup(name: string, building: Building, rooms: []) {
        return new Promise<void>((resolve, reject) => {
            console.log("Name, Geb, Räume", name, building, rooms);
            api.roomGroups.saveNewGroup({

                name: name,
                rooms: rooms,
                building: building,

            }).then(_ => resolve())
                .catch(reject);
        });
    }
    function editGroup(editGroup: RoomGroup) {
        return new Promise<void>((resolve, reject) => {
            console.log("Store editGroup:", editGroup);
            api.roomGroups.editGroup( {
                id: editGroup.id,
                name: editGroup.name,
                building: editGroup.building,
                rooms: editGroup.rooms,
            }).then(_=> {
                resolve()
            })
                .catch(reject);
        });
    }

    function deleteGroup(id: bigint) {
        return new Promise<void>((resolve, reject) => {
            api.roomGroups.deleteGroup(id).then(_ => {
                getRoomGroups();

                resolve()
            })
                .catch(reject);
        })
    }

    function getRoomsAndDoorsByGroupId(id: bigint): Promise<RoomsAndDoors[]> {
        return new Promise((resolve, reject) => {
            api.roomGroups.getDoorsAndRooms(id).then((response) => {
                roomsAndDoors.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }


    function setGroupConfig(doorId: bigint, twoWayConfig: TwoWayDoorConfiguration){
        return new Promise<void>((resolve, reject) => {
            api.roomGroups.setGroupConfig({
                doorId: doorId,
                twowaydoorconfig: twoWayConfig
            }).then(_ => resolve())
                .catch(reject);
        });
    }

    return {
        allRoomGroups,
        filteredGroups,
        editGroup,
        getRoomGroups,
        getGroupByBuilding,
        save,
        makeNewGroup,
        deleteGroup,
        roomsAndDoors,
        getRoomsAndDoorsByGroupId,
        setGroupConfig
    }
})