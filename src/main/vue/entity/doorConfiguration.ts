import {Credential, FormEntry} from "@/main/vue/entity/credentialDefinition";


export enum Direction {
    IN = "IN",
    OUT = "OUT",
    BOTH = "BOTH"
}

export interface TwoWayDoorConfiguration {
    doorConfigIn: DoorConfiguration,
    doorConfigOut: DoorConfiguration
}

export interface DoorConfiguration {

    description: string
    configParts: Config[],
}

export interface Config {

    credentials: Credential[]

    attributeFilters: AttributeFilter[]

}

export enum PredicateType {
    GREATER = ">",
    GREATER_EQUALS = ">=",
    SMALLER_EQUALS = "<=",
    SMALLER = "<",
    EQUALS = "=",
}

export interface AttributeFilter {
    attribute: FormEntry
    value: string
    predicateType: PredicateType

    currentDate?: boolean

}

