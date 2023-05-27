interface Coordinates {
    lat: number
    lng: number
}

interface Door {
    id: number
    name: string
    coordinates: Coordinates[]
    credentials: Credential[]
}

export interface Room {
    id: bigint
    name: string

    coordinates: Coordinates[]

    doors: Door[]
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