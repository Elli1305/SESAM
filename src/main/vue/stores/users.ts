import {defineStore} from 'pinia'
import {Ref, ref} from 'vue'
import axios from "axios";

export const useUserStore = defineStore('users', () => {
    const authenticated = ref(null)
    const validPassword: Ref<RegExpMatchArray | null> = ref(null)
    const comparePassword: Ref<boolean> = ref(false)


    function authenticate(group: string[], prename: string, lastname: string, password: string, passwordRepeat: string, email: string) {
        return axios.post('/api/signup', {
            firstName: prename,
            lastName: lastname,
            email: email,
            password: password,
            requestedRoles: group,
        });
        //authenticated.value = (email != 'admin@gmail.com')
    }

    function validatePassword(password: string, passwordRepeat: string){
        const passwordRegEx = /^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,120}$/;
        validPassword.value = password.match(passwordRegEx)
        comparePassword.value = passwordRepeat==password
    }

    return {authenticated, authenticate, validatePassword, validPassword, comparePassword}
})
