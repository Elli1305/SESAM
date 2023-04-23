import axios from "axios";
import {Location} from "@/main/vue/entity/location";

export default {
    getLocations(): Promise<Location[]>{
        return axios.get("api/locations")
    }
}