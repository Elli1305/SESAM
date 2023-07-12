import {Door} from "@/main/vue/entity/location";

export interface History {
    date: string
    door: Door
}

export interface HistoryCmd {
    date: string
    id: bigint
    doorName: string
    credential: string[]
}

export interface HistoryDetailCmd {
    date: string
    id: bigint
    doorName: string
    roomName: string
    credential: string[]
}