import {defineStore} from "pinia";
import {Ref, ref} from "vue";
import api from "@/main/vue/api";
import {HistoryCmd} from "@/main/vue/entity/history";

export const useHistoryStore = defineStore('history', () => {

    const all: Ref<HistoryCmd[] | null> = ref(null)
    const byDoor: Ref<HistoryCmd[] | null> = ref(null)
    const details: Ref<HistoryCmd[] | null> = ref(null)

    function getHistoryForTable() {
        return new Promise((resolve, reject) => {
            api.history.getHistories().then((response) => {
                all.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function getHistoryByDoor(id: string) {
        return new Promise((resolve, reject) => {
            api.history.getHistoryByDoorId(id).then((response) => {
                byDoor.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function getHistoryDetails(id: string) {
        return new Promise((resolve, reject) => {
            api.history.getHistoryDetails(id).then((response) => {
                details.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    return {
        getHistoryDetails,
        getHistoryForTable,
        getHistoryByDoor,
        all,
        byDoor,
        details
    }
})