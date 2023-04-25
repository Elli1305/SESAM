import axios from "axios";
import {CreateUser} from "@/main/vue/entity/createUser";
import {SignUpResponse} from "@/main/vue/entity/signUpResponse";
import {Credentials} from "@/main/vue/entity/credentials";
import {LoginResponse} from "@/main/vue/entity/loginResponse";
import {ResetPassword} from "@/main/vue/entity/resetPassword";
import {ChangePassword} from "@/main/vue/entity/changePassword";

export default {
    signUp(user: CreateUser): Promise<SignUpResponse> {
        return axios.post('/api/signup', user);
    },
    login(credentials: Credentials): Promise<LoginResponse> {
        return axios.post('/api/authenticate', credentials); //<2>
    },
    resetPassword(param: ResetPassword) {
        return axios.post('/api/password_reset', param);
    },
    changePassword(param: ChangePassword) {
        return axios.post('/api/update_password', param);
    }
}
