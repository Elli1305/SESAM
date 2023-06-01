import axios, {
    AxiosResponse,
} from "axios";
import {
    Category, CategoryResponse,
    Credential,
    CredentialMappingCmd,
    ExternalCredential,
    IssueCredentialAttribute
} from "@/main/vue/entity/credentialDefinition";
import {CredentialCmd} from "@/main/vue/entity/credentialDefinition";
import {CategoryCmd} from "@/main/vue/entity/credentialDefinition";
import {SignUpResponse} from "@/main/vue/entity/signUpResponse";

export default {
    get(id: string): Promise<AxiosResponse<Credential>> {
        return axios.get<Credential>(`/api/credentials/${id}`);
    },
    issue(id: string, attributes: IssueCredentialAttribute[]): Promise<AxiosResponse<string>> {
        return axios.post<string>(`/api/credentials/${id}/issue`, attributes);
    },
    all(): Promise<AxiosResponse<Credential[]>>{
        return axios.get("/api/credentials")
    },
    getCategories(): Promise<AxiosResponse<CategoryCmd[]>>{
        return axios.get("api/credentialmapping")
    },
    getCredentialInfos(): Promise<AxiosResponse<Credential[]>>{
        return axios.get("api/credentialview/{locationname}")
    },
    getCredentialsByLocation(param: string): Promise<AxiosResponse<CredentialCmd[]>>{
        return axios.get("api/credentialview/" + param)
    },

    getExternalCredentials(): Promise<AxiosResponse<ExternalCredential[]>> {
        return axios.get("/api/externalcredentials")
    },

    getCredentialsForMapping(): Promise<AxiosResponse<CredentialMappingCmd[]>> {
        return axios.get("/api/credentiallist")
    },

    deleteCategory(param: String): Promise<AxiosResponse<void>> {
        return axios.delete('/api/credentialmapping/delete/'+param);
    },

    deleteCategoryByName(param: String): Promise<AxiosResponse<void>> {
        return axios.delete('/api/category/delete/'+param);
    },

    createCategory(category: CategoryResponse): Promise<AxiosResponse<CategoryResponse>> {
        return axios.post('/api/category', category)
    },

    updateCategory(param: String, category: CategoryResponse): Promise<AxiosResponse<CategoryResponse>>{
        return axios.post('api/credentialmapping/edit/'+ param, category)
    }
}
