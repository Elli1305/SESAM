import {defineStore} from "pinia";
import {Building} from "@/main/vue/entity/location";
import api from "@/main/vue/api";
import {useLocationStore} from "@/main/vue/stores/locations";


export const useBuildingStore = defineStore('building', () => {

    const locationStore = useLocationStore()

    function save(building: Building): Promise<Building> {
        return new Promise((resolve, reject) => {
            api.building.save(building).then((response) => {
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