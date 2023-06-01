package com.gpse.sesam.web.cmd;

import jdk.dynalink.linker.LinkerServices;
import jdk.jfr.Category;

import java.util.List;

public class CategoryCmd {

    Long id;
    String name;
    List<Long> credentials;
    List<Long> externalCredentials;

    public CategoryCmd(Long id, String name, List<Long> credentials, List<Long> externalCredentials) {
        this.id = id;
        this.name = name;
        this.credentials = credentials;
        this.externalCredentials = externalCredentials;
    }

    public void setCredentials(List<Long> credentials) {
        this.credentials = credentials;
    }

    public void setExternalCredentials(List<Long> externalCredentials) {
        this.externalCredentials = externalCredentials;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Long> getCredentials() {
        return credentials;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<Long> getExternalCredentials() {
        return externalCredentials;
    }
}
