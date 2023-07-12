import {Issuer} from "@/main/vue/entity/issuer";

type CredentialFormEntryType = "text" | "number" | "date";

export type ComparisonType = "EQUAL" | "NOT_EQUAL" | "LESS_THAN" | "GREATER_THAN" | "LESS_EQUAL" | "GREATER_EQUAL";

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
        validationRules: (ComparisonRule | RangeRule | RegExRule | LengthRule)[]
    }[];
    checklist: ChecklistEntry[];
}

export interface ComparisonRule {
    kind: "comparison"
    comparisonType: ComparisonType
    content: string
    currentDay: boolean
    compareWithAttribute: boolean
    attributeName: string
}

export interface RangeRule {
    kind: "range"
    valueFrom: string
    valueTo: string
    compareWithAttribute: boolean
    attributeNameFrom: string,
    attributeNameTo: string
}

export interface RegExRule {
    kind: "regEx"
    regEx: string
    description: string
}

export interface LengthRule {
    kind: "length"
    comparisonType: ComparisonType
    length: number
    compareWithAttribute: boolean
    attributeName: string
}

export interface FormEntry {
    id: bigint;
    attributeName: string
    label: string;
    type: CredentialFormEntryType;
    validationRules: (ComparisonRule | RangeRule | RegExRule | LengthRule)[]
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

export interface CreateAttribute {
    type: string;
    name: string;
    attributeName: string;
    validationRules: (ComparisonRule | RangeRule | RegExRule | LengthRule)[]
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

export interface AllCredentialCmd {
    categoryName: string;
    credentialName: string;
    type: string;
    externalCredential: string[];
    issuerName: string[];
    issuerRoom: string[];
}

export interface CredentialSchema {
    name: string;
    credentialDefinitionId: string;
    agent?: string;
    ver: string;
    attrs: string[];
}