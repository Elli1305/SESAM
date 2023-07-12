import axios, {AxiosResponse} from "axios";
import {Floor} from "@/main/vue/entity/location";

export default {
    save(floor: Floor): Promise<AxiosResponse<Floor>> {
        return axios.post("api/floor/save", floor)
    },

    saveWithFile(floor: Floor, file: File): Promise<AxiosResponse<Floor>> {
        let formData = new FormData()
        formData.append("file", file)
        formData.append("floor", JSON.stringify(floor))
        return axios.post('/api/floor/uploadImage', formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        })
    },

    deleteFloor(param: String): Promise<AxiosResponse<void>> {
        return axios.delete('api/floor/' + param);
    }
}