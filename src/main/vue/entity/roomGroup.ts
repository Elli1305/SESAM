import {Coordinates, Door, Floor} from "@/main/vue/entity/location";

export interface Room {
    id: bigint
    name: string

    coordinates: Coordinates[]

    doors: Door[]
}

export interface Building {
    id: bigint
    name: string
    floors: Floor[]

}

export interface RoomGroup {
    id?: bigint
    name: string
    building: Building
    rooms: Room[]
}