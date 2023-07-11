import {defineStore} from "pinia"
import {nextTick, ref} from "vue"


export const useAppStore = defineStore('app', () => {

    const reload = ref(true)

    async function forceRender(): Promise<void> {
        reload.value = false
        await nextTick()
        reload.value = true
    }

    return {
        reload,
        forceRender
    }
})