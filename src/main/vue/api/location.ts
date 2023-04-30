import axios, {AxiosResponse} from "axios";
import {Location} from "@/main/vue/entity/location";

export default {
    getLocations(): Promise<AxiosResponse<Location[]>> {
        return axios.get("api/locations")
    }
}