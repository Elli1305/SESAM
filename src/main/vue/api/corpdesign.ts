import axios, {AxiosResponse} from "axios"
import {Colors} from "@/main/vue/entity/colors"
import {setCssVar} from "quasar"

export default {

    saveLogo(file: File): Promise<AxiosResponse<void>> {
        let formData = new FormData()
        formData.append("file", file)
        return axios.post('/api/corpdesign/save/logo', formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        })
    },

    saveFavicon(file: File): Promise<AxiosResponse<void>> {
        let formData = new FormData()
        formData.append("file", file)
        return axios.post('/api/corpdesign/save/favicon', formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        })
    },

    saveColors(colors: Colors): Promise<AxiosResponse<Colors>> {
        return axios.post('/api/corpdesign/save/colors', colors)
    },

    getColors(): Promise<AxiosResponse<Colors>> {
        return axios.get('/api/corpdesign/get/colors')
    },

    reset(): Promise<AxiosResponse<Colors>> {
        return axios.post('/api/corpdesign/reset')
    },

    setColors(): void {
        this.getColors().then(colors => {
            setCssVar('primary', colors.data.primaryColor)
            setCssVar('secondary', colors.data.secondary)
            setCssVar('accent', colors.data.accent)
            setCssVar('dark', colors.data.dark)
            setCssVar('dark-separator', colors.data.lightBlue)
            setCssVar('positive', colors.data.positive)
            setCssVar('negative', colors.data.negative)
            setCssVar('info', colors.data.info)
            setCssVar('warning', colors.data.warning)
        })
    }

}
