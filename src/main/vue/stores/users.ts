import {defineStore} from 'pinia'
import {Ref, ref} from 'vue'
import api from '../api'
import {AttainableRole} from "@/main/vue/entity/createUser"
import axios, {AxiosResponse} from "axios";
import {Credentials} from "@/main/vue/entity/credentials"
import {LoginResponse, User} from "@/main/vue/entity/loginResponse"
import {UserRole} from "@/main/vue/entity/signUpResponse";

export const useUserStore = defineStore('users', () => {
    const authenticated: Ref<boolean> = ref(false)
    const validPassword: Ref<RegExpMatchArray | null> = ref(null)
    const validEmail: Ref<RegExpMatchArray | null> = ref(null)
    const user: Ref<User | null> = ref(null)
    const comparePassword: Ref<boolean> = ref(false)

    function signUp(email: string, password: string, firstName: string, lastName: string, roles: AttainableRole[]): Promise<void> {
        return new Promise<void>((resolve, reject) => {
            api.auth.signUp({
                firstName: firstName,
                lastName: lastName,
                password: password,
                email: email,
                requestedRoles: roles,
            }).then(_ => resolve())
            .catch(reject);
        });
    }

    function validatePassword(password: string, passwordRepeat: string){
        const passwordRegEx = /^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,120}$/;
        validPassword.value = password.match(passwordRegEx)
        comparePassword.value = passwordRepeat===password
    }

    function authenticate(token?: string) {
        if (token) {
            authenticated.value = true
            sessionStorage.setItem('token', token);
            axios.defaults.headers['Authorization'] = 'Bearer ' + token
        } else {
            authenticated.value = false
        }
    }

    function validateEmail(email: string) {
        const emailRegex = /[^@ \t\r\n]+@[^@ \t\r\n]+\.[^@ \t\r\n]+/;
        validEmail.value = email.match(emailRegex)
    }

    function requestToken(credentials: Credentials): Promise<void> {
        return new Promise((resolve, reject) => {
            api.auth.login(credentials).then((res) => {
                authenticate(res.data.token)
                user.value = res.data.user
                resolve()
            }).catch((error) => {
                authenticate()
                reject(error)
            })
        })
    }

    function logout() {
        if(sessionStorage.getItem('token')) {
            sessionStorage.removeItem('token')
            authenticated.value = false;
        }
    }

    function resetPassword (email: string){
        return new Promise<void>((resolve, reject) => {
            api.auth.resetPassword({
                email: email,
            }).then(_ => resolve())
                .catch(reject);
        });
    }

    function changePassword (password: string, token: string){
        return new Promise<void>((resolve, reject) => {
            api.auth.changePassword({
                password: password,
                token: token,
            }).then(_ => resolve())
                .catch(reject);
        });
    }

    return {
        user,
        authenticated,
        signUp,
        authenticate,
        validatePassword,
        validPassword,
        comparePassword,
        validateEmail,
        validEmail,
        logout,
        requestToken,
        resetPassword,
        changePassword,
    };
})
