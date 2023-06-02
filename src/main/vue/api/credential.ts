import axios, {
    AxiosResponse,
} from "axios";
import {Credential, IssueCredentialAttribute} from "@/main/vue/entity/credentialDefinition";
import {CredentialCmd} from "@/main/vue/entity/credentialDefinition";

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
    getCategories(){
        return axios.get("api/credentialview")
    },
    getCredentialInfos(): Promise<AxiosResponse<Credential[]>>{
        return axios.get("api/credentialview/{locationname}")
    },
    getCredentialsByLocation(param: string): Promise<AxiosResponse<CredentialCmd[]>>{
        return axios.get("api/credentialview/" + param)
    },

    getCredentialsByIssuer(id: number | undefined): Promise<AxiosResponse<Credential[]>> {
        return axios.get(`api/credentials/getByIssuer/${id}`)
    }
}
