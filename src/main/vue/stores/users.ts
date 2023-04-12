import {defineStore} from 'pinia'
import {Ref, ref} from 'vue'
import axios from "axios";
import api from "../api";
import {Credentials} from "@/main/vue/entity/credentials";
import {LoginResponse} from "@/main/vue/entity/loginResponse";

export const useUserStore = defineStore('users', () => {
    const authenticated = ref(false)
    const validPassword: Ref<RegExpMatchArray | null> = ref(null)
    const comparePassword = ref(false)

    function validatePassword(password :string, passwordRepeat :string){
        var passwordRegEx = /^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,120}$/;
        validPassword.value = password.match(passwordRegEx)
        comparePassword.value = passwordRepeat===password
    }

    function authenticate(token?: string) {
        console.log(token)
        if (token) {
            authenticated.value = true
            localStorage.setItem('token', token);
            axios.defaults.headers['Authorization'] = token
        } else {
            authenticated.value = false
        }
    }

    function requestToken(credentials: Credentials): Promise<void> { //<2>
        return new Promise((resolve, reject) => {
            api.auth.login(credentials).then((res: LoginResponse) => {
                authenticate(res.data.token)
                resolve()
            }).catch(() => {
                console.log("here2")
                authenticate()
                reject()
            })
        })
    }

    function logout() {
        authenticated.value = false;
    }

    return {authenticated, authenticate, validatePassword, validPassword, comparePassword, requestToken, logout}
})
