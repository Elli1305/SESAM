import axios, {AxiosResponse} from "axios";
import {DoorConfiguration} from "@/main/vue/entity/doorConfiguration";

export default {
    save(doorConfig: DoorConfiguration): Promise<AxiosResponse<void>> {
        return axios.post("api/doorConfig/save", doorConfig)
    },

    get(doorId: string): Promise<AxiosResponse<DoorConfiguration>> {
        return axios.get(`api/doorConfig/${doorId}`)
    }
}