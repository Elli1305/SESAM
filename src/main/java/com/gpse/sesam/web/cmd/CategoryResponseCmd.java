package com.gpse.sesam.web.cmd;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gpse.sesam.domain.credential.Credential;
import com.gpse.sesam.domain.credential.ExternalCredential;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class CategoryResponseCmd {


    private String name;

    private List<Long> credentials = new ArrayList<>();

    private List<Long> externalCredentials = new ArrayList<>();


    public CategoryResponseCmd() {
    }
    public CategoryResponseCmd(final String name, final List<Long> credentials, final List<Long> externalCredentials) {
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

    public List<Long> getExternalCredentials() {
        return externalCredentials;
    }

    public List<Long> getCredentials() {
        return credentials;
    }

    public void setExternalCredentials(final List<Long> externalCredentials) {
        this.externalCredentials = externalCredentials;
    }

    public void setCredentials(final List<Long> credentials) {
        this.credentials = credentials;
    }
}
