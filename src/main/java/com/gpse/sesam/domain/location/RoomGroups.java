package com.gpse.sesam.domain.location;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class RoomGroups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Room> rooms;

    protected RoomGroups() {

    }

    public RoomGroups(String name, List<Room> rooms) {
        this.name = name;
        this.rooms = rooms;
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
}
