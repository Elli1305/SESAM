package com.gpse.sesam.web.cmd;

import java.util.ArrayList;
import java.util.List;

public class PredefinedConfigCmd {

    private Long id;
    private String name;
    private List<TwoWayDoorConfigCmd> doorConfig = new ArrayList<>();

    public PredefinedConfigCmd() {

    }

    public PredefinedConfigCmd(String name, List<TwoWayDoorConfigCmd> doorConfig) {
        this.name = name;
        this.doorConfig = doorConfig;
    }

    public PredefinedConfigCmd(Long id, String name, List<TwoWayDoorConfigCmd> doorConfig) {
        this.id = id;
        this.name = name;
        this.doorConfig = doorConfig;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TwoWayDoorConfigCmd> getDoorConfig() {
        return doorConfig;
    }

    public void setDoorConfig(List<TwoWayDoorConfigCmd> doorConfig) {
        this.doorConfig = doorConfig;
    }
}
