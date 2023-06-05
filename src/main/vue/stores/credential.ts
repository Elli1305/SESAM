import {defineStore} from "pinia";
import {Ref, ref} from "vue";
import api from "@/main/vue/api";
import {Credential, CredentialCmd, ExternalCredential} from "@/main/vue/entity/credentialDefinition";

export const useCredentialsStore = defineStore('credentials', {
    state: () => {
        return {
            credentials: new Array<Credential>(),
            externalCredentials: new Array<ExternalCredential>(),
        };
    }, actions: {
        async fetch(): Promise<void> {
            this.credentials = await api.credential.all().then(response => response.data);
            this.externalCredentials = await api.credential.externalCredentials().then(response => response.data);
        }
    }
},);

export const useCredentialStore = defineStore('credential', () => {
    const credentials: Ref<CredentialCmd[] | null> = ref(null)


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