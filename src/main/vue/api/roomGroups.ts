import axios, {AxiosResponse} from "axios";
import {RoomGroup} from "@/main/vue/entity/roomGroup"

export default {
    getRoomGroups(): Promise<AxiosResponse<RoomGroup[]>> {
        return axios.get("api/roomGroups");
    },
    getGroupByBuilding(param: bigint): Promise<AxiosResponse<RoomGroup[]>> {
        return axios.get("api/roomGroups/filter/" + param);
    },
    save(roomGroup: RoomGroup): Promise<AxiosResponse<RoomGroup>> {
        return axios.post('api/roomGroups/save', roomGroup);
    },

    saveNewGroup(roomGroup: RoomGroup): Promise<RoomGroup> {
        return axios.post('/api/roomGroups/newgroup', roomGroup);
    },
    getRoomGroupById(): Promise<AxiosResponse<RoomGroup>> {
        return axios.get("api/roomGroups/{id}");
    },
    editGroup(roomGroup: RoomGroup): Promise<AxiosResponse<RoomGroup>> {
        return axios.post('/api/roomGroups/edit_group', roomGroup);
    },
    deleteGroup(param: bigint): Promise<void> {
        return axios.delete('/api/roomGroups/' + param);
    }
}