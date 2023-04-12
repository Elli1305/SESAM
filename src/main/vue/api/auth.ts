import axios from 'axios';
import {Credentials} from "@/main/vue/entity/credentials";
import {LoginResponse} from "@/main/vue/entity/loginResponse";

export default {
    login(credentials: Credentials): Promise<LoginResponse> {
        return axios.post('/api/authenticate', credentials); //<2>
    }
}
