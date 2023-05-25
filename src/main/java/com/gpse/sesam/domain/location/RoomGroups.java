package com.gpse.sesam.domain.location;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class RoomGroups {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private String name;

    @Column
    @OneToMany(cascade = CascadeType.ALL)
    private List<Room> rooms;


    @ManyToOne(cascade = CascadeType.ALL)
    private Building building;

    protected RoomGroups() {

    }

    public RoomGroups(String name, List<Room> rooms, Building building) {
        this.name = name;
        this.rooms = rooms;
        this.building = building;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}
