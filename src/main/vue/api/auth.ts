import axios, {AxiosResponse} from "axios";
import {CreateUser} from "@/main/vue/entity/createUser";
import {SignUpResponse} from "@/main/vue/entity/signUpResponse";
import {Credentials} from "@/main/vue/entity/credentials";
import {LoginResponse, User} from "@/main/vue/entity/loginResponse";
import {CurrentUserListResponse} from "@/main/vue/entity/currentUserListResponse";
import {ResetPassword} from "@/main/vue/entity/resetPassword";
import {ChangePassword} from "@/main/vue/entity/changePassword";

export default {
    signUp(user: CreateUser): Promise<SignUpResponse> {
        return axios.post('/api/signup', user);
    },
    login(credentials: Credentials): Promise<AxiosResponse<LoginResponse>> {
        const param = new URLSearchParams();
        param.append('eMail', credentials.eMail);
        param.append('password', credentials.password);
        return axios.post('/api/authenticate', param, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        });
    },

    adminCurrentUser(): Promise<CurrentUserListResponse> {
        return axios.post('/api/admin/currentUserlist');

    },

    resetPassword(param: ResetPassword) {
        return axios.post('/api/password_reset', param);
    },
    changePassword(param: ChangePassword) {
        return axios.post('/api/update_password', param);
    },
    getEditUser(param: string): Promise<AxiosResponse<User>> {
        return axios.get('/api/user/edit/' + param);
    },
    editUser(param: User): Promise<AxiosResponse<SignUpResponse>> {
        return axios.post('/api/edit_user', param);
    }
}
