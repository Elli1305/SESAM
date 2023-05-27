import {defineStore} from "pinia";
import {Location} from "@/main/vue/entity/location";
import {ref, Ref} from "vue";
import api from "@/main/vue/api";


export const useLocationStore = defineStore('locations', () => {

    const allLocations: Ref<Location[] | null> = ref(null)
    let locationByName: Ref<Location | null> = ref(null)

    function getLocations() {
        return new Promise ((resolve, reject) => {
            api.location.getLocations().then((response) => {
                allLocations.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function getLocationsByName(){
        return new Promise ((resolve, reject) => {
            api.location.getLocationByName().then((response) => {
                locationByName.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    return{
        allLocations,
        getLocations,
        locationByName
    }
})