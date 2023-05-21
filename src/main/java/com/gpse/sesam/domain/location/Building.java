package com.gpse.sesam.domain.location;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "id")
    private List<Floor> floors = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private Location location;

    protected Building() {

    }

    public Building(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public void setFloors(List<Floor> floors) {
        this.floors = floors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addFloor(Floor floor) {
        floors.add(floor);
        floor.setBuilding(this);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void removeFloor(Floor floor) {
        floors.remove(floor);
        floor.setBuilding(null);
    }
}
