package com.gpse.sesam.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private int floorLevel;

    @OneToMany
    private List<Room> rooms;

    protected Floor() {

    }

    public Floor(int floorLevel, List<Room> rooms) {
        this.floorLevel = floorLevel;
        this.rooms = rooms;
    }

    public int getFloorLevel() {
        return floorLevel;
    }

    public void setFloorLevel(int floorLevel) {
        this.floorLevel = floorLevel;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }
}
