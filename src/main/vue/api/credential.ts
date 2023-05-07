import axios from "axios";
import {Location} from "@/main/vue/entity/location";

export default {
    getCredential(){
        return axios.get("api/credentialview")
    },

    getCategories(){
        return axios.get("/api/credentialview")
    }
}