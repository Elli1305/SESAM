import {defineStore} from "pinia";
import {Door} from "@/main/vue/entity/location";
import api from "@/main/vue/api";
import {useLocationStore} from "@/main/vue/stores/locations";
import {ref, Ref} from "vue";
import {ExternalCredential} from "@/main/vue/entity/credentialDefinition";


export const useDoorStore = defineStore('door', () => {

    const locationStore = useLocationStore()
    const doors: Ref<Door[] | null> = ref(null)

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


    function create(door: Door): Promise<Door> {
        return new Promise((resolve, reject) => {
            api.door.create(door).then((response) => {
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

    function getByRoomId(id: BigInt): Promise<Door[]> {
        return new Promise((resolve, reject) => {
            api.door.getDoorsByRoomId().then((response) => {
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    return {
        create,
        save,
        deleteById,
        getByRoomId,
        doors,
    }
})