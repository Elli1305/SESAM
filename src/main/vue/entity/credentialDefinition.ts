import {Issuer} from "@/main/vue/entity/issuer";

type CredentialFormEntryType = "text" | "number" | "date";

export interface Credential {
    id: bigint;
    name: string;
    agent: string;
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
    attributeName: string
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

interface CreateAttribute {
    type: string;
    name: string;
    attributeName: string;
}

interface CreateCondition {
    label: string;
}

export interface CreateCredential {
    name: string;
    agent: string;
    credentialDefinitionId: string;

    attributes: CreateAttribute[];
    conditions: CreateCondition[];
}

export interface CategoryCmd {
    nameCategory: string;
    internalCredentials: string[];
    externalCredentialsCmd: string[];
}

export interface CategoryResponse {
    name: string;
    credentials: bigint[];
    externalCredentials: bigint[];
}

export interface ExternalCredentialCmd {
    categoryName: string;
    credentialName: string;
    internalCredential: string[];
}
