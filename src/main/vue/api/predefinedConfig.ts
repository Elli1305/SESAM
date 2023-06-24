import axios, {AxiosResponse} from "axios";
import {PredefinedConfiguration} from "@/main/vue/entity/predefinedConfiguration";


export default {

    getAllConfigs(): Promise<AxiosResponse<PredefinedConfiguration[]>> {
        return axios.get("api/preConfig/allConfigs")
    },

    getConfig(id: BigInt): Promise<AxiosResponse<PredefinedConfiguration>> {
        return axios.get(`api/preConfig/${id}`)
    },

    saveConfig(config: PredefinedConfiguration): Promise<AxiosResponse<void>> {
        return axios.post("api/preConfig/create", config)
    },

    updateConfig(config: PredefinedConfiguration): Promise<AxiosResponse<void>> {
        return axios.post("api/preConfig/update", config)
    },

    deleteConfig(id: BigInt): Promise<AxiosResponse<void>> {
        return axios.delete(`api/preConfig/delete/${id}`)
    }

}