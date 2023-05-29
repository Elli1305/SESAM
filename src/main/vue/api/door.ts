import axios, {AxiosResponse} from "axios";
import {Door} from "@/main/vue/entity/location";

export default {
    save(door: Door): Promise<AxiosResponse<Door>> {
        return axios.post("api/door/save", door)
    },

    deleteById(id: BigInt): Promise<AxiosResponse<void>> {
        return axios.delete(`api/door/${id}`)
    }
}