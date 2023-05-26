import {defineStore} from "pinia";
import {Ref, ref} from "vue";
import api from "@/main/vue/api";
import {Credential, CredentialCmd} from "@/main/vue/entity/credentialDefinition";

export const useCredentialsStore = defineStore('credentials', {
    state: () => {
        return {
            credentials: new Array<Credential>(),
        };
    }, actions: {
        async fetch(): Promise<void> {
            this.credentials = await api.credential.all().then(response => response.data);
        },
        async update(credential: Credential): Promise<void> {
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