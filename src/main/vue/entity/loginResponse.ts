interface User {
    firstName: string;
    lastName: string;
    username: string;
    roles: [];

}

export interface LoginResponse {

    data: {
        token: string;
        user: User;
    }
}