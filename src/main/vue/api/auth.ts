import {CreateUser} from "@/main/vue/entity/createUser";
import axios from "axios";
import {SignUpResponse} from "@/main/vue/entity/signUpResponse";

export default {
    signUp(user: CreateUser): Promise<SignUpResponse> {
        return axios.post('/api/signup', user);
    }
}