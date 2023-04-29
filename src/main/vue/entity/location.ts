interface Room {
    id: bigint
    name: string

}

export interface Floor {

    id: bigint
    floorLevel: number
    floorPlanPath: string;
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