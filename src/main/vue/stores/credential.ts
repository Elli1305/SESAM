import {defineStore} from "pinia";
import {Ref, ref} from "vue";
import api from "@/main/vue/api";
import {Category, CredentialCmd} from "@/main/vue/entity/credentialDefinition";

export const useCredentialStore = defineStore('credential', () =>{
    const credentials: Ref<CredentialCmd[]|null> = ref(null)


    function getCredentialsByLocation(id: string) {
        return new Promise((resolve, reject) => {
            api.credential.getCredentialsByLocation(id).then((response) => {
                credentials.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }


    return {
        getCredentialsByLocation,
        credentials,
    }

})