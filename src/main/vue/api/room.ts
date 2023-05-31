import axios, {AxiosResponse} from "axios";
import {Room} from "@/main/vue/entity/location";

export default {
    save(room: Room): Promise<AxiosResponse<Room>> {
        return axios.post("api/room/save", room)
    },

    deleteById(id: BigInt): Promise<AxiosResponse<void>> {
        return axios.delete(`api/room/${id}`)
    }
}