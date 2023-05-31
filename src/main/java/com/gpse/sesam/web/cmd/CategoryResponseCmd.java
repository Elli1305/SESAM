package com.gpse.sesam.web.cmd;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gpse.sesam.domain.credential.Credential;
import com.gpse.sesam.domain.credential.ExternalCredential;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class CategoryResponseCmd {


    private String name;

    private List<Credential> credentials = new ArrayList<>();

    private List<ExternalCredential> externalCredentials = new ArrayList<>();


    public CategoryResponseCmd(final String name, final List<Credential> credentials, final List<ExternalCredential> externalCredentials) {
        this.name = name;
        this.credentials = credentials;
        this.externalCredentials = externalCredentials;
    }


    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<ExternalCredential> getExternalCredentials() {
        return externalCredentials;
    }

    public List<Credential> getCredentials() {
        return credentials;
    }

    public List<ExternalCredential> getExternalCredentialList() {
        return externalCredentials;
    }

    public void setExternalCredentialList(final List<ExternalCredential> externalCredentials) {
        this.externalCredentials = externalCredentials;
    }


    public void setExternalCredentials(final List<ExternalCredential> externalCredentials) {
        this.externalCredentials = externalCredentials;
    }

    public void setCredentials(final List<Credential> credentials) {
        this.credentials = credentials;
    }
}
