import axios from "axios";
import {CreateUser} from "@/main/vue/entity/createUser";
import {SignUpResponse} from "@/main/vue/entity/signUpResponse";
import {Credentials} from "@/main/vue/entity/credentials";
import {LoginResponse} from "@/main/vue/entity/loginResponse";
import {CurrentUserListResponse} from "@/main/vue/entity/currentUserListResponse";

export default {
    signUp(user: CreateUser): Promise<SignUpResponse> {
        return axios.post('/api/signup', user);
    },
    login(credentials: Credentials): Promise<LoginResponse> {
        return axios.post('/api/authenticate', credentials); //<2>
    },
    adminCurrentUser(): Promise<CurrentUserListResponse>{
        return axios.post('/api/admin/currentUserlist');
    }
}
