import axios, {AxiosResponse} from "axios";
import {Floor} from "@/main/vue/entity/location";

export default {
    save(floor: Floor): Promise<AxiosResponse<Floor>> {
        return axios.post("api/floor/save", floor)
    }
}