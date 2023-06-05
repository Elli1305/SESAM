import axios, {AxiosResponse,} from "axios";
import {
    Category,
    CategoryResponse,
    CreateCredential,
    Credential,
    CredentialCmd,
    ExternalCredential,
    IssueCredentialAttribute
} from "@/main/vue/entity/credentialDefinition";

export default {
    get(id: string): Promise<AxiosResponse<Credential>> {
        return axios.get<Credential>(`/api/credentials/${id}`);
    },

    issue(id: string, attributes: IssueCredentialAttribute[]): Promise<AxiosResponse<string>> {
        return axios.post<string>(`/api/credentials/${id}/issue`, attributes);
    },

    all(): Promise<AxiosResponse<Credential[]>> {
        return axios.get("/api/credentials")
    },
    externalCredentials(): Promise<AxiosResponse<ExternalCredential[]>> {
        return axios.get("/api/external_credentials")
    },
    create(credential: CreateCredential): Promise<AxiosResponse<CreateCredential>> {
        return axios.post(`/api/credentials`, credential);
    },
    delete(id: number): Promise<AxiosResponse<void>> {
        return axios.delete(`/api/credentials/${id}`);
    },
    update(id: number, credential: CreateCredential): Promise<AxiosResponse<void>> {
        return axios.put(`/api/credentials/${id}`, credential);
    },

    getCategories(): Promise<AxiosResponse<Category[]>> {
        return axios.get("api/credentialmapping")
    },

    getCredentialsByLocation(param: string): Promise<AxiosResponse<CredentialCmd[]>> {
        return axios.get("api/credentialview/" + param)
    },

    getCredentialsByIssuer(id: number | undefined): Promise<AxiosResponse<Credential[]>> {
        return axios.get(`api/credentials/getByIssuer/${id}`)
    },
    getExternalCredentials(): Promise<AxiosResponse<ExternalCredential[]>> {
        return axios.get("/api/externalcredentials")
    },

    deleteCategory(param: String): Promise<AxiosResponse<void>> {
        return axios.delete('/api/credentialmapping/delete/' + param);
    },

    createCategory(category: CategoryResponse): Promise<AxiosResponse<CategoryResponse>> {
        return axios.post('/api/category', category)
    },

    updateCategory(param: String, category: CategoryResponse): Promise<AxiosResponse<CategoryResponse>> {
        return axios.put('api/credentialmapping/edit/' + param, category)
    }
}
