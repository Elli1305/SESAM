package com.gpse.sesam.web.cmd;

import java.util.List;

public class IssuerResponseCmd {
    java.util.List<Long> credentials;
    Long room;

    protected IssuerResponseCmd(){}

    public IssuerResponseCmd(java.util.List<Long> credentials, Long room) {
        this.credentials = credentials;
        this.room = room;
    }

    public void setCredentials(List<Long> credentials) {
        this.credentials = credentials;
    }

    public void setRoom(Long room) {
        this.room = room;
    }

    public List<Long> getCredentials() {
        return credentials;
    }

    public Long getRoom() {
        return room;
    }
}
