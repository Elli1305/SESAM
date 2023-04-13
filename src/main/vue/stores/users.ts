import {defineStore} from 'pinia'
import {ref} from 'vue'
import axios from "axios";

export const useUserStore = defineStore('users', () => {
    const authenticated = ref(null)
    const validPassword = ref(null)
    const comparePassword = ref(null)


    function authenticate(group: string[], prename: string, lastname: string, password: string, passwordRepeat: string, email: string) {
        return axios.post('/api/signup', {
            firstName: prename,
            lastName: lastname,
            email: email,
            password: password,
            roles: group,
        });
        //authenticated.value = (email != 'admin@gmail.com')
    }

    function validatePassword(password, passwordRepeat){
        var passwordRegEx = /^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,120}$/;
        validPassword.value = password.match(passwordRegEx)
        comparePassword.value = passwordRepeat==password
    }

    return {authenticated, authenticate, validatePassword, validPassword, comparePassword}
})
