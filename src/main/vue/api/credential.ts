import axios from "axios";
import {AxiosResponse} from "axios/index";
import {Location} from "@/main/vue/entity/location";

export default {
    getCredential(){
        return axios.get("api/credentialview")
    },

    getCategories(){
        return axios.get("api/credentialview")
    },

    getCredentialInfos(): Promise<AxiosResponse<Credential[]>>{
        return axios.get("api/credentialview/{locationname}")
    }
}