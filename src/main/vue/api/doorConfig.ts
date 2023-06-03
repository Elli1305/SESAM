import axios, {AxiosResponse} from "axios";
import {DoorConfiguration} from "@/main/vue/entity/doorConfiguration";

export default {
    save(doorConfig: DoorConfiguration): Promise<AxiosResponse<void>> {
        return axios.post("api/doorConfig/save", doorConfig)
    }
}