package com.gpse.sesam.web.cmd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DoorHistoryTableCmd {
    Date date;

    Long doorId;

    String doorName;

    java.util.List<String> credential = new ArrayList<>();

    public DoorHistoryTableCmd(Date date, Long doorId, String doorName, java.util.List<String> credential) {
        this.date = date;
        this.doorName = doorName;
        this.credential = credential;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public String getDoorName() {
        return doorName;
    }

    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }

    public List<String> getCredential() {
        return credential;
    }

    public void setCredential(List<String> credential) {
        this.credential = credential;
    }
}
