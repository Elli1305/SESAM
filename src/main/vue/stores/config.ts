import {defineStore} from "pinia";
import {PredefinedConfiguration} from "@/main/vue/entity/predefinedConfiguration";
import {ref, Ref} from "vue";
import api from "@/main/vue/api";


export const useConfigStore = defineStore('config', () => {

    const allPreConfigs: Ref<PredefinedConfiguration[]> = ref([])

    function getAllConfigs() {
        return new Promise((resolve, reject) => {
            api.predefinedConfig.getAllConfigs().then((response) => {
                allPreConfigs.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    return {
        getAllConfigs
    }
})