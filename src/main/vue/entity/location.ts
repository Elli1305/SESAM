interface Room {
    id: bigint
    name: string

}

interface Floor {

    id: bigint
    floorLevel: number
    rooms: Room[]

}

interface Building {
    id: bigint
    name: string
    floors: Floor[]

}

export interface Location {
    id: bigint
    name: string
    buildings: Building[]
}