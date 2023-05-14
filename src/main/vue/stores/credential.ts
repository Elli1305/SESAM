import {defineStore} from "pinia";

export interface CredentialDefinition {
    id: number;
    name: string;
    agent: string;
    credentialDefinitionId: string;
}

export const useCredentialStore = defineStore('credential', {
    state: () => {
        return {
            credentials: new Array<CredentialDefinition>(),
        };
    }, actions: {
        async fetch(): Promise<void> {
            this.credentials = [
                {id: 0, name: 'Telekom Member', agent: 'tlabs', credentialDefinitionId: '$T-MEMBER'},
                {id: 1, name: 'University Member', agent: 'university', credentialDefinitionId: '$U-MEMBER'},
                {id: 2, name: 'Telekom Member', agent: 'tlabs', credentialDefinitionId: '$T-TRAINING'},
                {id: 3, name: 'University Training', agent: 'university', credentialDefinitionId: '$U-TRAINING'},
            ];
        },
        async update(id: number, changes: Partial<Omit<CredentialDefinition, 'id'>>): Promise<void> {
            const index = this.credentials.findIndex(credential => credential.id == id);

            if (index === -1) {
                return;
            }

            this.credentials[index] = {...this.credentials[index], ...changes};
        }
    }
},);