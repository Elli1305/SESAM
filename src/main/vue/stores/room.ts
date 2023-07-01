import {defineStore} from "pinia";
import {Room} from "@/main/vue/entity/location";
import api from "@/main/vue/api";
import {useLocationStore} from "@/main/vue/stores/locations";
import {ref, Ref} from "vue";
import {CredentialCmd} from "@/main/vue/entity/credentialDefinition";
import Floor from "@/main/vue/api/floor";


export const useRoomStore = defineStore('room', () => {

    const locationStore = useLocationStore()
    const rooms: Ref<Room[]|null> = ref(null)
    const floor: Ref<typeof Floor| null> = ref(null);

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

    function getRooms(id: string) {
        return new Promise((resolve, reject) => {
            api.room.getRooms().then((response) => {
                rooms.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }
    function getFloor(id: BigInt) {
        return new Promise((resolve,reject) => {
            api.room.getFloor(id).then((response: { data: any; }) => {
                floor.value = response.data()
                resolve(response.data)
            }).catch((error: any) => {
                reject(error)
            })
        })
    }

    return {
        save,
        deleteById,
        getRooms,
        rooms
    }
})