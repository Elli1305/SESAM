package com.gpse.sesam.web.cmd;

import jdk.dynalink.linker.LinkerServices;
import jdk.jfr.Category;

import java.util.List;

public class CategoryCmd {
    String name;
    List<String> credentials;
    List<String> externalCredentials;

    public CategoryCmd(String name, List<String> credentials, List<String> externalCredentials) {
        this.name = name;
        this.credentials = credentials;
        this.externalCredentials = externalCredentials;
    }

    public void setCredentials(List<String> credentials) {
        this.credentials = credentials;
    }

    public void setExternalCredentials(List<String> externalCredentials) {
        this.externalCredentials = externalCredentials;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> getCredentials() {
        return credentials;
    }

    public List<String> getExternalCredentials() {
        return externalCredentials;
    }
}
