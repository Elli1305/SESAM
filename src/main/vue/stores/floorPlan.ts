import {defineStore} from "pinia";
import {Ref, ref} from "vue";
import {Floor} from "@/main/vue/entity/location";

export const useFloorPlanStore = defineStore('floorPlan', () => {
    const selectedFloorPlan: Ref<String> = ref('')
    const selectedFloorId: Ref<Number> = ref(0)

    return {selectedFloorPlan, selectedFloorId}
})