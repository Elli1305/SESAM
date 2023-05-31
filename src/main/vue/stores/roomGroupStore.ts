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

    return {
        allRoomGroups,
        getRoomGroups,
    }
})