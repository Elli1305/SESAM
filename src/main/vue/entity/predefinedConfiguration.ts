import {DoorConfiguration} from "@/main/vue/entity/doorConfiguration";


export interface PredefinedConfiguration {

    id?: bigint

    name: string,

    doorConfigIn: DoorConfiguration,

    doorConfigOut: DoorConfiguration
}