package com.gpse.sesam.web.cmd;

import com.gpse.sesam.domain.location.building.Building;
import com.gpse.sesam.domain.location.room.Room;

import java.util.List;

public class RoomGroupEditCmd {
    private Long id;
    private String name;
    private Building building;
    private List<Room> rooms;

    public RoomGroupEditCmd() {

    }

    public RoomGroupEditCmd(final Long id, final String name, final Building building, final List<Room> rooms) {

        this.id = id;
        this.name = name;
        this.building = building;
        this.rooms = rooms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public final List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(final List<Room> rooms) {
        this.rooms = rooms;
    }
}
