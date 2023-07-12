package com.gpse.sesam.web.cmd;

import com.gpse.sesam.domain.location.door.Door;

import java.util.Date;

public class DoorHistoryDetailsCmd {
    Door door;
    String roomName;
    Date date;

    public DoorHistoryDetailsCmd (Door door, String roomName, Date date) {
        this.roomName = roomName;
        this.date = date;
        this.door = door;
    }

    public Door getDoor() {
        return door;
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }
}
