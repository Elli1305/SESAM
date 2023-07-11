import axios, {AxiosResponse} from "axios";
import {GroupConfig, RoomGroup, RoomsAndDoors} from "@/main/vue/entity/roomGroup"
import {Room} from "@/main/vue/entity/location";

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
    getRooms(param: bigint): Promise<AxiosResponse<Room[]>> {
        return axios.get("/api/roomGroups/rooms/" + param)
    },

    setGroupConfig(config: GroupConfig): Promise<AxiosResponse<void>> {
        return axios.post("/api/roomGroups/saveconfigs", config)
    }
}