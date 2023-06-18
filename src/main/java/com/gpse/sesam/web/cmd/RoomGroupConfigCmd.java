package com.gpse.sesam.web.cmd;

import java.util.ArrayList;
import java.util.List;

public class RoomGroupConfigCmd {
    Long room;
    java.util.List<Long> doors = new ArrayList<>();

    String description;

    private List<ConfigPartsCmd> configParts;

    public RoomGroupConfigCmd(Long room, List<Long> doors, String description, List<ConfigPartsCmd> configParts) {
        this.room = room;
        this.doors = doors;
        this.description = description;
        this.configParts = configParts;
    }

    public List<ConfigPartsCmd> getConfigParts() {
        return configParts;
    }

    public Long getRoom() {
        return room;
    }

    public void setConfigParts(List<ConfigPartsCmd> configParts) {
        this.configParts = configParts;
    }

    public void setRoom(Long room) {
        this.room = room;
    }

    public List<Long> getDoors() {
        return doors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDoors(List<Long> doors) {
        this.doors = doors;
    }
}
