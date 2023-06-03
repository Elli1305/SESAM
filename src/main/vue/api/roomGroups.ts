import axios, {AxiosResponse} from "axios";
import {RoomGroup} from "@/main/vue/entity/roomGroup"
import {SignUpResponse} from "@/main/vue/entity/signUpResponse";

export default {
    getRoomGroups(): Promise<AxiosResponse<RoomGroup[]>> {
        return axios.get("api/roomGroups")
    },
    save(roomGroup: RoomGroup): Promise<AxiosResponse<RoomGroup>> {
        return axios.post('api/roomGroups/save', roomGroup)
    },
    getRoomGroupByName(): Promise<AxiosResponse<RoomGroup>> {
        return axios.get("api/roomGroups/{name}")
    },
    deleteGroup(param: bigint): Promise<AxiosResponse<SignUpResponse>> {
        return axios.delete('/api/roomGroups/'+param);
    }
}