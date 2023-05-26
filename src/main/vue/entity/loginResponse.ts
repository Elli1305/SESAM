export interface User {
    id: number;
    firstName: string;
    lastName: string;
    username: string;
    roles: [];
}

export interface LoginResponse {

    token: string;
    user: User;
}