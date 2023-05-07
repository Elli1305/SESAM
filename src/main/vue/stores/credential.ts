import {defineStore} from "pinia";
import {Ref, ref} from "vue";
import api from "@/main/vue/api";
import {Category} from "@/main/vue/entity/credentialDefinition";

export const useCredentialStore = defineStore('credential', () =>{
    const allCategories: Ref<Category[] | null> = ref(null)

    function getCategories() {
        return new Promise ((resolve, reject) => {
            api.credential.getCategories().then((response) => {
                allCategories.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    return {
        allCategories,
        getCategories
    }

})