import axios from "axios";
import {Location} from "@/main/vue/entity/location";

export default {
    getLocations(){
        return axios.get("api/locations")
    }
}