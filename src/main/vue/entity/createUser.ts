export interface CreateUser {
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    requestedRoles: AttainableRole[];
}

export enum AttainableRole { ADMINISTRATOR = 'ADMINISTRATOR', EDITOR = 'EDITOR', ISSUER = 'ISSUER'}
