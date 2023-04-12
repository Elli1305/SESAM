import axios from 'axios';
import {Credentials} from "@/main/vue/entity/credentials";

export default {
    login(credentials: Credentials) {
        return axios.post('/api/authenticate', credentials); //<2>
    }
}
