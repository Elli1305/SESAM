import {defineStore} from 'pinia'
import {ref} from 'vue'

export const useUserStore = defineStore('users', () => {
    const authenticated = ref(null)
    const validPassword = ref(null)
    const comparePassword = ref(null)


        function authenticate(email) {
        authenticated.value = (email != 'admin@gmail.com')
    }

    function validatePassword(password, passwordRepeat){
        var passwordRegEx = /^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,120}$/;
        validPassword.value = password.match(passwordRegEx)
        comparePassword.value = passwordRepeat==password
    }

    return {authenticated, authenticate, validatePassword, validPassword, comparePassword}
})
