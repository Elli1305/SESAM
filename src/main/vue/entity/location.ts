
interface Room {
    id: bigint
    name: string

}

interface Floor {

    id: bigint
    name: string
    rooms: Room[]

}

interface Building {
    id: bigint
    name: string
    floors: Floor[]

}

export interface Location {

    data: {
        id: bigint
        name: string
        buildings: Building[]
    }
}