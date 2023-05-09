import axios, {AxiosResponse} from "axios";
import {IssueCredentialAttribute, SesamCredential} from "../entity/credential";

export default {
    get(id: string): Promise<AxiosResponse<SesamCredential>> {
        return axios.get<SesamCredential>(`/api/credentials/${id}`);
    },
    issue(id: string, attributes: IssueCredentialAttribute[]): Promise<AxiosResponse<string>> {
        return axios.post<string>(`/api/credentials/${id}/issue`, attributes);
    },
};
