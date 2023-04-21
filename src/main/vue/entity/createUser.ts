export interface CreateUser {
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    requestedRoles: AttainableRole[];
}

export type AttainableRole = 'ADMINISTRATOR' | 'EDITOR' | 'ISSUER';