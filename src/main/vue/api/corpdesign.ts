import axios from "axios"

export default {

    saveLogo(file: File): Promise<void> {
        let formData = new FormData()
        formData.append("file", file)
        return axios.post('/api/corpdesign/save/logo', formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        })
    },

    saveFavicon(file: File): Promise<void> {
        let formData = new FormData()
        formData.append("file", file)
        return axios.post('/api/corpdesign/save/favicon', formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        })
    },

    reset(): Promise<void> {
        return axios.post('/api/corpdesign/reset')
    }

}
