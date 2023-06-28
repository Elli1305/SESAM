package com.gpse.sesam.web.cmd;

public class PredefinedConfigCmd {

    private Long id;
    private String name;

    private DoorConfigCmd doorConfigIn;

    private DoorConfigCmd doorConfigOut;

    public PredefinedConfigCmd() {

    }

    public PredefinedConfigCmd(String name, DoorConfigCmd in, DoorConfigCmd out) {
        this.name = name;
        this.doorConfigIn = in;
        this.doorConfigOut = out;
    }

    public PredefinedConfigCmd(Long id, String name, DoorConfigCmd in, DoorConfigCmd out) {
        this.id = id;
        this.name = name;
        this.doorConfigIn = in;
        this.doorConfigOut = out;
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

    public DoorConfigCmd getDoorConfigIn() {
        return doorConfigIn;
    }

    public void setDoorConfigIn(DoorConfigCmd doorConfigIn) {
        this.doorConfigIn = doorConfigIn;
    }

    public DoorConfigCmd getDoorConfigOut() {
        return doorConfigOut;
    }

    public void setDoorConfigOut(DoorConfigCmd doorConfigOut) {
        this.doorConfigOut = doorConfigOut;
    }
}
