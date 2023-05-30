import {defineStore} from "pinia";
import {Ref, ref} from "vue";
import {Room} from "@/main/vue/entity/location";

export const useFloorPlanStore = defineStore('floorPlan', () => {
    const selectedFloorPlan: Ref<String> = ref('')
    const selectedFloorId: Ref<BigInt> = ref(BigInt(0))
    const rooms: Ref<Room[]> = ref([])
    const selectedRooms = ref([])

    if (sessionStorage.getItem("floorPlan")) {
        const state = JSON.parse((sessionStorage.getItem("floorPlan") || ''));
        selectedFloorId.value = state.selectedFloorId;
        selectedFloorPlan.value = state.selectedFloorPlan;
        rooms.value = state.rooms;
    }


    return {selectedFloorPlan, selectedFloorId, rooms, selectedRooms}
})