import {defineStore} from "pinia";
import {Ref, ref} from "vue";
import api from "@/main/vue/api";
import {Category, CategoryCmd, CredentialCmd} from "@/main/vue/entity/credentialDefinition";

export const useCredentialStore = defineStore('credential', () =>{
    const credentials: Ref<CredentialCmd[]|null> = ref(null)
    const categories: Ref<CategoryCmd[]|null> = ref(null)


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

    function getCategory() {
        return new Promise((resolve, reject) => {
            api.credential.getCategories().then((response) => {
                categories.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    return {
        getCredentialsByLocation,
        credentials,
        getCategory,
        categories
    }

})