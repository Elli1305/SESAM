import {defineStore} from "pinia";
import {Location} from "@/main/vue/entity/location";
import {computed, ref, Ref} from "vue";
import api from "@/main/vue/api";


export const useLocationStore = defineStore('locations', () => {

    const allLocations: Ref<Location[]> = ref([])
    let locationByName: Ref<Location | null> = ref(null)
    const getLocationTreeStructure = computed(() => {
            return (locationId: bigint, buildingId: bigint) => {
                return allLocations.value.map((location) => ({
                    id: location.id,
                    title: location.name,
                    level: 0,
                    expanded: location.id === locationId,
                    children: location.buildings.map((building) => ({
                        id: building.id,
                        title: building.name,
                        level: 1,
                        expanded: building.id === buildingId,
                        children: building.floors.map(floor => ({
                            id: floor.id,
                            level: 2,
                            floorPlan: floor.floorPlanPath,
                            title: floor.floorLevel === 0 ? "Erdgeschoss" : "Etage " + floor.floorLevel,
                            rooms: floor.rooms
                        }))
                    }))
                }))
            }
        }
    )

    const getFloorById = computed(() => {
            return (floorId: bigint) => {
                return allLocations
                    .value
                    .flatMap((location) =>
                        location
                            .buildings
                            .flatMap(building => building.floors))
                    .find(floor => floor.id === floorId)
            }
        }
    )


    const getRoomById = computed(() => {
            return (roomId: bigint) => {
                return allLocations
                    .value
                    .flatMap((location) =>
                        location
                            .buildings
                            .flatMap(building => building.floors))
                    .flatMap(floor => floor.rooms)
                    .find(room => room.id === roomId)
            }
        }
    )


    function getLocations(): Promise<Location[]> {
        return new Promise((resolve, reject) => {
            api.location.getLocations().then((response) => {
                allLocations.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function getLocationsByName() {
        return new Promise((resolve, reject) => {
            api.location.getLocationByName().then((response) => {
                locationByName.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function save(location: Location): Promise<Location> {
        return new Promise((resolve, reject) => {
            api.location.save(location).then((response) => {
                getLocations();
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    return {
        allLocations,
        getLocations,
        locationByName,
        getLocationTreeStructure,
        save,
        getFloorById,
        getRoomById
    }
})