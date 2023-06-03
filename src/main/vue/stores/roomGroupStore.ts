import {defineStore} from "pinia";
import {RoomGroup} from "@/main/vue/entity/roomGroup";
import {ref, Ref} from "vue";
import api from "@/main/vue/api";


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
        deleteGroup,
    }
})