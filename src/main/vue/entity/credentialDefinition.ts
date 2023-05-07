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
    label: String
}

export interface Category {
    id: bigint
    name: string
    credentials: Credential []
    externalCredentials: ExternalCredential[]
}

export interface ExternalCredential {
    id: bigint
    name: string
    credentialDefinitionId: string
}