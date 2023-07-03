import axios, {AxiosResponse} from "axios";
import {Location} from "@/main/vue/entity/location";

export default {
    getLocations(): Promise<AxiosResponse<Location[]>> {
        return axios.get("api/locations")
    },

    save(location: Location): Promise<AxiosResponse<Location>> {
        return axios.post("api/locations/save", location)
    },
    
    getLocationByName(): Promise<AxiosResponse<Location>> {
        return axios.get("api/locations/{name}")
    },

    deleteLocation(param: String): Promise<AxiosResponse<void>> {
        return axios.delete('api/locations/' + param);
    }
}