import {User} from "@/main/vue/entity/loginResponse";
import {Room} from "@/main/vue/entity/location"
import {InternalCredential} from "@/main/vue/entity/credentialDefinition";

export interface Issuer extends User {
    room: Room
    credentials: InternalCredential []
}

export interface IssuerCmd {

    issuerId: number
    room: string
    credentials: string[]
}