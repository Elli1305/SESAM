import axios, {AxiosResponse} from "axios";
import {TwoWayDoorConfiguration} from "@/main/vue/entity/doorConfiguration";

export default {
    save(doorConfig: TwoWayDoorConfiguration): Promise<AxiosResponse<void>> {
        return axios.post("api/doorConfig/save", doorConfig)
    },

    get(doorId: string): Promise<AxiosResponse<TwoWayDoorConfiguration>> {
        return axios.get(`api/doorConfig/${doorId}`)
    }
}