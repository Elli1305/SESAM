import {defineStore} from "pinia";
import {RoomGroup} from "@/main/vue/entity/roomGroup";
import {ref, Ref} from "vue";
import api from "@/main/vue/api";
import {Building} from "@/main/vue/entity/roomGroup";


export const useRoomGroupStore = defineStore('roomGroups', () => {

    const allRoomGroups: Ref<RoomGroup[]> = ref([])
    let roomGroupByName: Ref<RoomGroup | null> = ref(null)

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

    function deleteGroup(id: bigint) {
        return new Promise<void>((resolve, reject) => {
            api.roomGroups.deleteGroup(id).then(_ => {
                getRoomGroups();

                resolve()
            })
                .catch(reject);
        })
    }

    return {
        allRoomGroups,
        getRoomGroups,
        save,
        makeNewGroup,
        deleteGroup,
    }
})