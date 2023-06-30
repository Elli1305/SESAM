import {FormEntry} from "@/main/vue/entity/credentialDefinition";


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
    configParts: Config[],

    attributeFilters: AttributeFilter[]

}

export interface Config {

    credentials: Credential[]

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
