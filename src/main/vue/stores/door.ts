import {defineStore} from "pinia";
import {Door} from "@/main/vue/entity/location";
import api from "@/main/vue/api";
import {useLocationStore} from "@/main/vue/stores/locations";


export const useDoorStore = defineStore('door', () => {

    const locationStore = useLocationStore()

    function save(door: Door): Promise<Door> {
        return new Promise((resolve, reject) => {
            api.door.save(door).then((response) => {
                locationStore.getLocations();
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function deleteById(id: BigInt): Promise<void> {
        return new Promise((resolve, reject) => {
            api.door.deleteById(id).then(() => {
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