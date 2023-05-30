import {defineStore} from "pinia";
import {Floor} from "@/main/vue/entity/location";
import api from "@/main/vue/api";
import {useLocationStore} from "@/main/vue/stores/locations";


export const useFloorStore = defineStore('floor', () => {

    const locationStore = useLocationStore()

    function save(floor: Floor, file: File): Promise<Floor> {
        if (file) {
            return new Promise<Floor>((resolve, reject) => {
                api.floor.saveWithFile(floor, file).then((response) => {
                    locationStore.getLocations();
                    resolve(response.data)
                }).catch((error) => {
                    reject(error)
                })
            })
        } else {

            return new Promise((resolve, reject) => {
                api.floor.save(floor).then((response) => {
                    locationStore.getLocations();
                    resolve(response.data)
                }).catch((error) => {
                    reject(error)
                })
            })
        }
    }

    return {
        save,
    }
})