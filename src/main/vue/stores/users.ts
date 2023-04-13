import {defineStore} from 'pinia'
import {Ref, ref} from 'vue'
import api from '../api'
import {AttainableRole} from "@/main/vue/entity/createUser";

export const useUserStore = defineStore('users', () => {
    const authenticated: Ref<boolean> = ref(false)
    const validPassword: Ref<RegExpMatchArray | null> = ref(null)
    const validEmail: Ref<RegExpMatchArray | null> = ref(null)
    const comparePassword: Ref<boolean> = ref(false)


    function authenticate(group: AttainableRole[], prename: string, lastname: string, password: string, email: string): Promise<void> {
        return new Promise<void>((resolve, reject) => {
            api.auth.signup({
                firstName: prename,
                lastName: lastname,
                password: password,
                email: email,
                requestedRoles: group,
            }).then(_ => {
                console.log('successfully registered')
                authenticated.value = true;
                console.log(authenticated.value);
                resolve();
            })
            .catch(_ => {
                console.log('failed to register')
                authenticated.value = false;
                reject();
            });
        });
    }

    function validatePassword(password: string, passwordRepeat: string){
        const passwordRegEx = /^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,120}$/;
        validPassword.value = password.match(passwordRegEx)
        comparePassword.value = passwordRepeat==password
    }

    function validateEmail(email: string) {
        const emailRegex = /[^@ \t\r\n]+@[^@ \t\r\n]+\.[^@ \t\r\n]+/;
        validEmail.value = email.match(emailRegex)
    }

    return {authenticated, authenticate, validatePassword, validPassword, comparePassword, validateEmail, validEmail}
})
