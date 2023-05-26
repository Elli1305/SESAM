type CredentialFormEntryType = "text" | "number" | "date";

export interface Credential {
    id: bigint;
    name: string;
    agent: string;
    credentialDefinitionId: string;
    form: FormEntry[];
    checklist: ChecklistEntry[];
}

export interface IssueCredential {
    id: bigint;
    name: string;
    form: {
        id: bigint;
        label: string;
        type: CredentialFormEntryType;
        value: string | null;
    }[];
    checklist: ChecklistEntry[];
}

export interface FormEntry {
    id: bigint;
    label: string;
    type: CredentialFormEntryType;
}

export interface ChecklistEntry {
    id: bigint;
    label: string;
}

export interface Category {
    id: bigint;
    name: string;
    credentials: Credential[];
    externalCredentials: ExternalCredential[];
}

export interface ExternalCredential {
    id: bigint;
    name: string;
    credentialDefinitionId: string;
}

export interface IssueCredentialAttribute {
    id: bigint;
    value: string | null;
}


export interface CredentialCmd {
    id: bigint;
    categoryName: string;
    credentialName: string;
    externalCredential: string[];
    issuerName: string[];
    issuerRoom: string[];
}