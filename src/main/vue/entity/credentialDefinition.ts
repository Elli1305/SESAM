import {Issuer} from "@/main/vue/entity/issuer";

type CredentialFormEntryType = "text" | "number" | "date";

export interface Credential {
    id: bigint;
    name: string;
    credentialDefinitionId: string;
    form: FormEntry[];
    checklist: ChecklistEntry[];
    issuer: Issuer[];
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

export interface ExternalCredential {
    id: bigint;
    name: string;
    credentialDefinitionId: string;
}

export interface Category {
    id: bigint;
    name: string;
    credentials: Credential[];
    externalCredentials: ExternalCredential[];
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

export interface CategoryCmd {
    nameCategory: string;
    internalCredentials: string[];
    externalCredentialsCmd: string[];
}

export interface CredentialMappingCmd {
    id: bigint;
    name: string;
}

export interface CategoryResponse {
    name: string;
    credentials: Credential[];
    externalCredentials: ExternalCredential[];
}