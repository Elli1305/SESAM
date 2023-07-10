package com.gpse.sesam.web.cmd;

import java.util.List;

public class DoorGroupConfigCmd {

    private List<Long> doorIds;

    private List<TwoWayDoorConfigCmd> doorConfig;

    public DoorGroupConfigCmd(List<Long> ids, List<TwoWayDoorConfigCmd> config) {
        this.doorIds = ids;
        this.doorConfig = config;
    }

    public DoorGroupConfigCmd() {

    }

    public List<Long> getDoorIds() {
        return doorIds;
    }

    public void setDoorIds(List<Long> doorIds) {
        this.doorIds = doorIds;
    }

    public List<TwoWayDoorConfigCmd> getDoorConfig() {
        return doorConfig;
    }

    public void setDoorConfig(List<TwoWayDoorConfigCmd> doorConfig) {
        this.doorConfig = doorConfig;
    }
}
