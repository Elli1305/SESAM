import {defineStore} from "pinia";
import {Room} from "@/main/vue/entity/location";
import api from "@/main/vue/api";
import {useLocationStore} from "@/main/vue/stores/locations";


export const useRoomStore = defineStore('room', () => {

    const locationStore = useLocationStore()

    function save(room: Room): Promise<Room> {
        return new Promise((resolve, reject) => {
            api.room.save(room).then((response) => {
                locationStore.getLocations();
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function deleteById(id: BigInt): Promise<void> {
        return new Promise((resolve, reject) => {
            api.room.deleteById(id).then(() => {
                locationStore.getLocations();
                resolve()
            }).catch((error) => {
                reject(error)
            })
        })
    }

    return {
        save,
        deleteById
    }
})