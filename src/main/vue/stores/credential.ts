import {defineStore} from "pinia";
import {Ref, ref} from "vue";
import api from "@/main/vue/api";
import {
    Category,
    CategoryCmd,
    CredentialCmd,
    CategoryResponse,
    CredentialMappingCmd,
    ExternalCredential,
    Credential
} from "@/main/vue/entity/credentialDefinition";
import {User} from "@/main/vue/entity/loginResponse";

export const useCredentialStore = defineStore('credential', () =>{
    const credentials: Ref<CredentialCmd[]|null> = ref(null)
    const allCredentials: Ref<Credential[]|null> = ref(null)
    const categories: Ref<CategoryCmd[]|null> = ref(null)
    const external: Ref<ExternalCredential[]|null> = ref(null)
    const categoryEdit: Ref<Category | null> = ref(null)
    const credentialsForMapping: Ref<CredentialMappingCmd[]|null> = ref(null)


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

    function getExternalCredentials() {
        return new Promise((resolve, reject) => {
            api.credential.getExternalCredentials().then((response) => {
                external.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function getCredentialsForMapping() {
        return new Promise((resolve, reject) => {
            api.credential.getCredentialsForMapping().then((response) => {
                credentialsForMapping.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function deleteCategory(id: string) {
        return new Promise<void>((resolve, reject) => {
            api.credential.deleteCategory(id).then(() => {
                resolve()
            }).catch((error) => {
                reject(error)
            })
        });
    }

    function deleteCategoryByName(name: string) {
        return new Promise<void>((resolve, reject) => {
            api.credential.deleteCategoryByName(name).then(() => {
                resolve()
            }).catch((error) => {
                reject(error)
            })
        });
    }

    function getCredentials() {
        return new Promise((resolve, reject) => {
            api.credential.all().then((response) => {
                allCredentials.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function createCategory(name: string, credential: bigint[], external: bigint[]) {
        return new Promise<void>((resolve, reject) => {
            api.credential.createCategory({
                name: name,
                credentials: credential,
                externalCredentials: external
            }).then(_ => resolve())
                .catch(reject);
        });
    }

    function updateCredentials(id: string, name: string, credential: bigint[], external: bigint[]) {
        return new Promise<void>((resolve, reject) => {
            api.credential.updateCategory(id,
                {
                    name: name,
                    credentials: credential,
                    externalCredentials: external
            }).then(_ => {
                resolve()
            })
                .catch(reject);
        })
    }


    return {
        getCredentialsByLocation,
        credentials,
        getCategory,
        categories,
        external,
        getExternalCredentials,
        credentialsForMapping,
        getCredentialsForMapping,
        deleteCategory,
        allCredentials,
        getCredentials,
        createCategory,
        deleteCategoryByName,
        updateCredentials
    }

})