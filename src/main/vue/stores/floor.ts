import {defineStore} from "pinia";
import {Floor} from "@/main/vue/entity/location";
import api from "@/main/vue/api";
import {useLocationStore} from "@/main/vue/stores/locations";


export const useFloorStore = defineStore('floor', () => {

    const locationStore = useLocationStore()

    function save(floor: Floor): Promise<Floor> {
        return new Promise((resolve, reject) => {
            api.floor.save(floor).then((response) => {
                locationStore.getLocations();
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    return {
        save,
    }
})