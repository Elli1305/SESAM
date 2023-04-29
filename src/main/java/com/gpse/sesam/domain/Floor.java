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

    @Column
    private String floorPlanPath;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Room> rooms;

    protected Floor() {

    }

    public Floor(int floorLevel, String floorPlanPath, List<Room> rooms) {
        this.floorLevel = floorLevel;
        this.floorPlanPath = floorPlanPath;
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

    public String getFloorPlanPath() {
        return floorPlanPath;
    }

    public void setFloorPlanPath(String floorPlanPath) {
        this.floorPlanPath = floorPlanPath;
    }
}
