package com.gpse.sesam.web.cmd;

import java.util.ArrayList;
import java.util.List;

public class RoomGroupDoorConfigCmd {

    Long room;
    String roomName;

    java.util.List<String> doorNames = new ArrayList<>();
    java.util.List<Long> doorIds = new ArrayList<>();

    public RoomGroupDoorConfigCmd(Long room, String roomName, List<Long> doorIds, List<String> doorNames) {
        this.room = room;
        this.roomName = roomName;
        this.doorNames = doorNames;
        this.doorIds = doorIds;
    }

    public List<Long> getDoors() {
        return doorIds ;
    }

    public void setRoom(Long room) {
        this.room = room;
    }

    public Long getRoom() {
        return room;
    }

    public void setDoors(List<Long> doorIds ) {
        this.doorIds  = doorIds ;
    }

    public List<String> getDoorNames() {
        return doorNames;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setDoorNames(List<String> doorNames) {
        this.doorNames = doorNames;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
