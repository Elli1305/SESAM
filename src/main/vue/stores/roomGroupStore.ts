import {defineStore} from "pinia";
import {RoomGroup} from "@/main/vue/entity/roomGroup";
import {ref, Ref} from "vue";
import api from "@/main/vue/api";
import GroupRooms from "@/main/vue/views/GroupRooms.vue";


export const useRoomGroupStore = defineStore('roomGroups',() => {

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

            console.log("hier1")
            api.roomGroups.save(roomGroup).then((response) => {

                console.log("hier2")
                getRoomGroups();

                console.log("hier3")
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }
    function deleteGroup(id: bigint) {
        new Promise<void>((resolve, reject) => {
            api.roomGroups.deleteGroup(id).then(_=> resolve())
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