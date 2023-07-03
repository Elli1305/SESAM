import {defineStore} from "pinia";
import {PredefinedConfiguration} from "@/main/vue/entity/predefinedConfiguration";
import {ref, Ref} from "vue";
import api from "@/main/vue/api";


export const useConfigStore = defineStore('config', () => {

    const allPreConfigs: Ref<PredefinedConfiguration[]> = ref([])
    const currentConfig: Ref<PredefinedConfiguration | null> = ref(null)

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

    function getConfig(id: BigInt): Promise<PredefinedConfiguration> {
        return new Promise((resolve, reject) => {
            api.predefinedConfig.getConfig(id).then((response) => {
                currentConfig.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function createConfig(config: PredefinedConfiguration) {
        return new Promise<void>((resolve, reject) => {
            api.predefinedConfig.saveConfig(config).then(() => {
                getAllConfigs()
                resolve()
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function updateConfig(config: PredefinedConfiguration) {
        return new Promise<void>((resolve, reject) => {
            api.predefinedConfig.updateConfig(config).then(() => {
                getAllConfigs()
                resolve()
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function deleteConfig(id: BigInt) {
        return new Promise<void>((resolve, reject) => {
            api.predefinedConfig.deleteConfig(id).then(() => {
                getAllConfigs()
                resolve()
            }).catch((error) => {
                reject(error)
            })
        })
    }

    return {
        allPreConfigs,
        currentConfig,
        getAllConfigs,
        getConfig,
        createConfig,
        updateConfig,
        deleteConfig
    }
})