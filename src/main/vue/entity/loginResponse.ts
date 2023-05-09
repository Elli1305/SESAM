export interface User {
    firstName: string;
    lastName: string;
    username: string;
    roles: [];

}

export interface LoginResponse {

    token: string;
    user: User;
}