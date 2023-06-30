import axios, {AxiosResponse} from "axios";
import {Building} from "@/main/vue/entity/location";

export default {
    save(building: Building): Promise<AxiosResponse<Building>> {
        return axios.post("api/building/save", building)
    },

    deleteBuilding(param: String): Promise<AxiosResponse<void>> {
        return axios.delete('api/building/' + param);
    }
}