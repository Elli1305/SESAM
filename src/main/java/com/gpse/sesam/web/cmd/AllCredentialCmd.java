package com.gpse.sesam.web.cmd;

import java.util.List;

public class AllCredentialCmd {
    private String categoryName;

    private String credentialName;

    private String type;

    private List<String> otherCredentials;

    private List<String> issuerName;

    private List<String> room;

    public AllCredentialCmd (String categoryName, String credentialName, String type, List<String> otherCredentials, List<String> issuerName, List<String> room) {
        this.credentialName = credentialName;
        this.categoryName = categoryName;
        this.otherCredentials = otherCredentials;
        this.type = type;
        this.issuerName = issuerName;
        this.room = room;

    }

    public void setCredentialName(String credentialName) {
        this.credentialName = credentialName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCredentialName() {
        return credentialName;
    }

    public void setRoom(List<String> room) {
        this.room = room;
    }

    public List<String> getOtherCredentials() {
        return otherCredentials;
    }

    public List<String> getIssuerName() {
        return issuerName;
    }

    public List<String> getRoom() {
        return room;
    }

    public String getType() {
        return type;
    }

    public void setOtherCredentials(List<String> otherCredentials) {
        this.otherCredentials = otherCredentials;
    }

    public void setIssuerName(List<String> issuerName) {
        this.issuerName = issuerName;
    }

    public void setType(String type) {
        this.type = type;
    }
}
