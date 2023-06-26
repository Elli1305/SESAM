package com.gpse.sesam.web.cmd;

import com.gpse.sesam.domain.location.door.Door;

import java.util.ArrayList;
import java.util.List;

public class RoomGroupDoorConfigCmd {

    Long room;
    String roomName;

    java.util.List<Door> doors = new ArrayList<>();

    public RoomGroupDoorConfigCmd(Long room, String roomName, List<Door> doors) {
        this.room = room;
        this.roomName = roomName;
        this.doors = doors;
    }

    public void setDoors(List<Door> doors) {
        this.doors = doors;
    }

    public List<Door> getDoors() {
        return doors;
    }

    public void setRoom(Long room) {
        this.room = room;
    }

    public Long getRoom() {
        return room;
    }


    public String getRoomName() {
        return roomName;
    }


    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
