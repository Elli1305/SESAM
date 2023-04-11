import {defineStore} from 'pinia'
import {ref} from 'vue'

export const useUserStore = defineStore('users', () => {
    const authenticated = ref(null)

    function authenticate(email) {
        authenticated.value = (email != 'admin@gmail.com')
    }

    return {authenticated, authenticate}
})
