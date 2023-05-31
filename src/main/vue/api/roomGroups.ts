import axios, {AxiosResponse} from "axios";
import {RoomGroup} from "@/main/vue/entity/roomGroup"

export default {
    getRoomGroups(): Promise<AxiosResponse<RoomGroup[]>> {
        return axios.get("api/roomGroups")
    },
    save(roomGroup: RoomGroup): Promise<AxiosResponse<RoomGroup>> {
        return axios.post("api/roomGroups/save")
    },
    getRoomGroupByName(): Promise<AxiosResponse<RoomGroup>> {
        return axios.get("api/roomGroups/{name}")
    }
}