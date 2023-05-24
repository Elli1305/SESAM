import axios from "axios";
import {AxiosResponse} from "axios/index";
import {User} from "@/main/vue/entity/loginResponse";
import {CredentialCmd} from "@/main/vue/entity/credentialDefinition";

export default {
    getCredential(){
        return axios.get("api/credentialview")
    },

    getCategories(){
        return axios.get("api/credentialview")
    },

    getCredentialInfos(): Promise<AxiosResponse<Credential[]>>{
        return axios.get("api/credentialview/{locationname}")
    },

    getCredentialsByLocation(param: string): Promise<AxiosResponse<CredentialCmd[]>>{
        return axios.get("api/credentialview/" + param)
    }
}
