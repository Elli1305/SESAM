import axios, {AxiosResponse} from "axios";
import {HistoryCmd, HistoryDetailCmd} from "@/main/vue/entity/history";

export default {
    get(id: string): Promise<AxiosResponse<History[]>> {
        return axios.get(`/api/history/${id}`);
    },
    all(): Promise<AxiosResponse<History[]>> {
        return axios.get("/api/history")
    },
    getByDoorId(id: string): Promise<AxiosResponse<History[]>> {
        return axios.get(`/api/historiesByDoor/${id}`);
    },

    getHistoryByDoorId(id: string): Promise<AxiosResponse<HistoryCmd[]>> {
        return axios.get(`/historiesByDoorCmd/${id}`);
    },

    getHistories(): Promise<AxiosResponse<HistoryCmd[]>> {
        return axios.get(`/historiesCmd`);
    },
    getHistoryDetails(id: string): Promise<AxiosResponse<HistoryDetailCmd[]>> {
        return axios.get(`/historiesByDoorCmd/${id}`);
    },
}