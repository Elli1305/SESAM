import axios from "axios";

export default {
    getCredential(){
        return axios.get("api/credentialview")
    },

    getCategories(){
        return axios.get("api/credentialview")
    }
}