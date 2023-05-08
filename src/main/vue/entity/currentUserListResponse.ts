import {User} from "@/main/vue/entity/loginResponse";

export interface CurrentUserListResponse {

    data: {
        users: User [];
    }
}