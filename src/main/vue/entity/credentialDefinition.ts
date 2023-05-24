export interface Credential {
    id: bigint
    name: string
    credentialDefinitionId: string
    form: FormEntry []
    checklist: ChecklistEntry []
}

export interface FormEntry {
    id: bigint
    label: string
    type: string
}

export interface ChecklistEntry {
    id: bigint
    label: string
}

export interface Category {
    id: bigint
    name: string
    credentials: Credential[]
    externalCredentials: ExternalCredential[]
}

export interface ExternalCredential {
    id: bigint
    name: string
    credentialDefinitionId: string
}

export interface CredentialCmd {
    id: bigint
    categoryName: string
    credentialName: string
    externalCredential: string[]
    issuerName: string[]
    issuerRoom: string[]
}