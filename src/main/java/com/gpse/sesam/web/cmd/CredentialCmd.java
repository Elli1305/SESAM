package com.gpse.sesam.web.cmd;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class CredentialCmd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String categoryName;

    @Column
    private String credentialName;

    @ElementCollection
    private List<String> externalCredential;

    @ElementCollection
    private List<String> issuerName;

    @ElementCollection
    private List<String> room;

    protected CredentialCmd() { }
    public CredentialCmd(String categoryName, String credentialName, List<String> externalCredential,
                         List<String> issuerName, List<String> room) {
        this.categoryName = categoryName;
        this.credentialName = credentialName;
        this.externalCredential = externalCredential;
        this.issuerName = issuerName;
        this.room = room;
    }

    public void setRoom(List<String> room) {
        this.room = room;
    }

    public List<String> getExternalCredential() {
        return externalCredential;
    }

    public List<String> getIssuerName() {
        return issuerName;
    }

    public List<String> getRoom() {
        return room;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCredentialName() {
        return credentialName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCredentialName(String credentialName) {
        this.credentialName = credentialName;
    }

    public void setExternalCredential(List<String> externalCredential) {
        this.externalCredential = externalCredential;
    }

    public void setIssuerName(List<String> issuerName) {
        this.issuerName = issuerName;
    }
}
