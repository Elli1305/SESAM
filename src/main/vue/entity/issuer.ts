import {User} from "@/main/vue/entity/loginResponse";
import {Room} from "@/main/vue/entity/location"
import {Credential} from "@/main/vue/entity/credentialDefinition";

export interface Issuer extends User {
    room: Room
    credentials: Credential []
}