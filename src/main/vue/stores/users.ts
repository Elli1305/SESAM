import {defineStore} from 'pinia'
import {ref} from 'vue'
import axios from "axios";

export const useUserStore = defineStore('users', () => {
    const authenticated = ref(null)
    const validPassword = ref(null)
    const comparePassword = ref(null)


        function authenticate(group, prename, lastname, password, passwordRepeat, email) {
        //the new part
        const credentials = new URLSearchParams();
        credentials.append('prename', prename);
        credentials.append('lastname', lastname);
        credentials.append('email', email);
        credentials.append('password', password);
        credentials.append('passwordRepeat', passwordRepeat);
        credentials.append('roles', group); // ??????????????????????????????????????????
        return axios.post('/api/authenticate', credentials);
        //authenticated.value = (email != 'admin@gmail.com')
    }

    function validatePassword(password, passwordRepeat){
        var passwordRegEx = /^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,120}$/;
        validPassword.value = password.match(passwordRegEx)
        comparePassword.value = passwordRepeat==password
    }

    return {authenticated, authenticate, validatePassword, validPassword, comparePassword}
})
