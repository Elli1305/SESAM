import {DoorConfiguration} from "@/main/vue/entity/doorConfiguration";


export interface PredefinedConfiguration {

    name: string,

    doorConfigIn: DoorConfiguration,

    doorConfigOut: DoorConfiguration
}