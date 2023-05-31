package com.gpse.sesam.web.cmd;

import org.hibernate.mapping.List;

public class CredentialMappingCmd {
    Long id;

    String name;

    public CredentialMappingCmd(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
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
}
