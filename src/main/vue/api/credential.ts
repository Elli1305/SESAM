import axios, {AxiosResponse,} from "axios";
import {
    AllCredentialCmd,
    Category,
    CategoryResponse,
    CreateCredential,
    ExternalCredential, ExternalCredentialCmd,
    IssueCredentialAttribute
} from "@/main/vue/entity/credentialDefinition";

export default {
    get(id: string): Promise<AxiosResponse<InternalCredential>> {
        return axios.get<InternalCredential>(`/api/credentials/${id}`);
    },

    getExternalCredential(id: string): Promise<AxiosResponse<ExternalCredential>> {
        return axios.get<ExternalCredential>(`/api/external_credentials/${id}`);
    },

    issue(id: string, attributes: IssueCredentialAttribute[]): Promise<AxiosResponse<string>> {
        return axios.post<string>(`/api/credentials/${id}/issue`, attributes);
    },

    all(): Promise<AxiosResponse<InternalCredential[]>> {
        return axios.get("/api/credentials")
    },
    externalCredentials(): Promise<AxiosResponse<ExternalCredential[]>> {
        return axios.get("/api/external_credentials")
    },
    create(createOnLedger: boolean, credential: CreateCredential): Promise<AxiosResponse<CreateCredential>> {
        return axios.post(`/api/credentials?createOnLedger=${createOnLedger}`, credential);
    },
    createExternalCredential(credential: CreateExternalCredential): Promise<AxiosResponse<CreateCredential>> {
        return axios.post(`/api/external_credentials`, credential);
    },
    delete(id: string): Promise<AxiosResponse<void>> {
        return axios.delete(`/api/credentials/${id}`);
    },
    deleteExternalCredential(id: string): Promise<AxiosResponse<void>> {
        return axios.delete(`/api/external_credentials/${id}`);
    },
    update(id: string, credential: CreateCredential): Promise<AxiosResponse<void>> {
        return axios.put(`/api/credentials/${id}`, credential);
    },
    updateExternalCredential(id: string, credential: CreateExternalCredential): Promise<AxiosResponse<void>> {
        return axios.put(`/api/external_credentials/${id}`, credential);
    },

    getAllCredentials(): Promise<AxiosResponse<ExternalCredential[]>> {
        return axios.get("/api/credentials/getAll")
    },

    getCategories(): Promise<AxiosResponse<Category[]>> {
        return axios.get("api/credentialmapping")
    },

    getCredentialsByLocation(param: bigint): Promise<AxiosResponse<CredentialCmd[]>> {
        return axios.get("api/credentialview/" + param)
    },

    getCredentialsByIssuer(id: number | undefined): Promise<AxiosResponse<InternalCredential[]>> {
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
    },

    getAllCredentialsForView():Promise<AxiosResponse<CredentialCmd[]>> {
        return axios.get("api/allcredentials")
    },

    getAllExternalCredentialsForView(): Promise<AxiosResponse<ExternalCredentialCmd[]>> {
        return axios.get("api/externalcredentialview")
    },

    getAllForView(): Promise<AxiosResponse<AllCredentialCmd[]>> {
        return axios.get("api/allcredentialview")
    },

    getExternalCredentialByLocation(param: bigint): Promise<AxiosResponse<ExternalCredentialCmd[]>> {
        return axios.get("api/externalcredentialsbylocation/" + param)
    },

    getAllByLocation(param: bigint): Promise<AxiosResponse<AllCredentialCmd[]>> {
        return axios.get("api/allbylocation/" + param)
    },

    getCredentialSchema(credentialDefinitionId: string): Promise<AxiosResponse<CredentialSchema>> {
        return axios.get(`/api/credential_schema/${credentialDefinitionId}`);
    },

    exportCredentials(): Promise<AxiosResponse<Map<string, any>>> {
        return axios.get(`/api/export_credentials`);
    },

    importCredentials(content: CredentialExport): Promise<AxiosResponse<void>> {
        return axios.post(`/api/import_credentials`, content);
    },
}
