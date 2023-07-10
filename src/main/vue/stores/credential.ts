import {defineStore} from "pinia";
import {Ref, ref} from "vue";
import api from "@/main/vue/api";
import {
    AllCredentialCmd,
    Category,
    CredentialCmd,
    ExternalCredential,
    ExternalCredentialCmd,
    InternalCredential
} from "@/main/vue/entity/credentialDefinition";

export const useCredentialsStore = defineStore('credentials', {
    state: () => {
        return {
            credentials: new Array<InternalCredential>(),
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
    const allCredentials: Ref<ExternalCredential[] | null> = ref(null)
    const credsByIssuer: Ref<InternalCredential[] | null> = ref(null)
    const categories: Ref<Category[] | null> = ref(null)
    const external: Ref<ExternalCredential[] | null> = ref(null)
    const credentialsForView: Ref<CredentialCmd[] | null> = ref(null)
    const externalForView: Ref<ExternalCredentialCmd[] | null> = ref(null)
    const externalByLocation: Ref<ExternalCredentialCmd[] | null> = ref(null)
    const all: Ref<AllCredentialCmd[] | null> = ref(null)
    const allByLocation: Ref<AllCredentialCmd[] | null> = ref(null)



        function getCredentialsByLocation(id: bigint) {
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
                api.credential.getAllCredentials().then((response) => {
                    allCredentials.value = response.data
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

    function getCredentialsByIssuer(id: number | undefined): Promise<InternalCredential[]> {
        return new Promise((resolve, reject) => {
            api.credential.getCredentialsByIssuer(id).then((response) => {
                credsByIssuer.value = response.data
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


        function deleteCategory(id: string) {
            return new Promise<void>((resolve, reject) => {
                api.credential.deleteCategory(id).then(() => {
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

        function getCredentialsForView() {
            return new Promise((resolve, reject) => {
                api.credential.getAllCredentialsForView().then((response) => {
                    credentialsForView.value = response.data
                    resolve(response.data)
                }).catch((error) => {
                    reject(error)
                })
            })
        }

    function getExternalsForView() {
        return new Promise((resolve, reject) => {
            api.credential.getAllExternalCredentialsForView().then((response) => {
                externalForView.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function getAllForView() {
        return new Promise((resolve, reject) => {
            api.credential.getAllForView().then((response) => {
                all.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function getExternalByLocation(id: bigint) {
        return new Promise((resolve, reject) => {
            api.credential.getExternalCredentialByLocation(id).then((response) => {
                externalByLocation.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function getAllByLocation(id: bigint) {
        return new Promise((resolve, reject) => {
            api.credential.getAllByLocation(id).then((response) => {
                allByLocation.value = response.data
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
            credsByIssuer,
            credentialsForView,
            getCredentialsForView,
            getCategory,
            categories,
            external,
            getExternalCredentials,
            deleteCategory,
            allCredentials,
            getCredentials,
            createCategory,
            updateCredentials,
            externalForView,
            getExternalsForView,
            getAllForView,
            all,
            externalByLocation,
            allByLocation,
            getExternalByLocation,
            getAllByLocation,
        }
    }
)