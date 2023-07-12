import axios, {AxiosResponse} from "axios";
import {Room, Floor} from "@/main/vue/entity/location";

export default {
    save(room: Room): Promise<AxiosResponse<Room>> {
        return axios.post("api/room/save", room)
    },

    deleteById(id: BigInt): Promise<AxiosResponse<void>> {
        return axios.delete(`api/room/${id}`)
    },

    getRooms(): Promise<AxiosResponse<Room[]>> {
        return axios.get("api/room/rooms")
    },
    getFloor(id: BigInt): Promise<AxiosResponse<Floor>>{
        return axios.get(`api/room/floor/${id}`)
    },


    getRoomDetails(id: BigInt): Promise<AxiosResponse<Room>> {
        return axios.get(`api/room/${id}`)
    }
}