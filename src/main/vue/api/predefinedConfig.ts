import axios, {AxiosResponse} from "axios";
import {PredefinedConfiguration} from "@/main/vue/entity/predefinedConfiguration";


export default {

    getAllConfigs(): Promise<AxiosResponse<PredefinedConfiguration[]>> {
        return axios.get("api/preConfig/allConfigs")
    }
}