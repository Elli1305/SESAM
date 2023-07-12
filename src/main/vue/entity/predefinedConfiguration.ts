import {TwoWayDoorConfiguration} from "@/main/vue/entity/doorConfiguration";


export interface PredefinedConfiguration {

    id?: bigint

    name: string,

    doorConfig: TwoWayDoorConfiguration[]
}