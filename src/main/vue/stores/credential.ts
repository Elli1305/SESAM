import {defineStore} from "pinia";
import {Ref, ref} from "vue";
import api from "@/main/vue/api";
import {CredentialCmd, Credential} from "@/main/vue/entity/credentialDefinition";

export const useCredentialStore = defineStore('credential', () =>{
    const credentials: Ref<CredentialCmd[]|null> = ref(null)
    const allCredentials: Ref<Credential[] | null> = ref(null)
    const credsByIssuer: Ref<Credential[] | null> = ref(null)


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

    function getAllCredentials() {
        return new Promise((resolve, reject) => {
            api.credential.all().then((response) => {
                allCredentials.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function getCredentialsByIssuer(id: number | undefined): Promise<Credential[]> {
        return new Promise((resolve, reject) => {
            api.credential.getCredentialsByIssuer(id).then((response) => {
                credsByIssuer.value = response.data
                resolve(response.data)
            }).catch((error) => {
              reject(error)
            })

        })
    }


    return {
        getCredentialsByLocation,
        getAllCredentials,
        getCredentialsByIssuer,
        credentials,
        allCredentials,
        credsByIssuer
    }

})