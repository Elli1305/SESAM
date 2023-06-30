import axios, {AxiosResponse} from "axios";
import {GroupConfigResponse, RoomGroup, RoomsAndDoors} from "@/main/vue/entity/roomGroup"
import {CategoryResponse} from "@/main/vue/entity/credentialDefinition";
import {TwoWayDoorConfiguration} from "@/main/vue/entity/doorConfiguration";
import axios from "axios";

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
    },
    getDoorsAndRooms(param: bigint): Promise<AxiosResponse<RoomsAndDoors[]>> {
        return axios.get("/api/roomGroups/doorsandrooms/" + param)
    },

    setGroupConfig(config: GroupConfigResponse[]): Promise<AxiosResponse<GroupConfigResponse[]>> {
        return axios.post("/api/roomGroups/saveconfigs", config)
    }
}