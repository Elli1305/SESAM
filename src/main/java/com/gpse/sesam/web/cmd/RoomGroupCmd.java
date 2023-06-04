package com.gpse.sesam.web.cmd;

import com.gpse.sesam.domain.location.building.Building;
import com.gpse.sesam.domain.location.room.Room;

import java.util.List;

public class RoomGroupCmd {
    private String name;
    private Building building;
    private List<Room> rooms;

    public RoomGroupCmd() {

    }

    public RoomGroupCmd(final String name, final Building building, final List<Room> rooms) {

        this.name = name;
        this.building = building;
        this.rooms = rooms;
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

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
