import {AttainableRole} from "@/main/vue/entity/createUser";

interface UserRole {
    role: AttainableRole;
    granted: boolean;
}

export interface SignUpResponse {
    data: {
        firstName: string;
        lastName: string;
        username: string;
        roles: UserRole[];
    }
}