package com.gpse.sesam.web.cmd;

import java.util.List;

public class CredentialSchemaCmd {
    private String name;

    private String credentialDefinitionId;

    private String agent;

    private String ver;

    private List<String> attrs;

    public CredentialSchemaCmd(final String name, final String credentialDefinitionId, final String agent, final String ver, final List<String> attrs) {
        this.name = name;
        this.credentialDefinitionId = credentialDefinitionId;
        this.agent = agent;
        this.ver = ver;
        this.attrs = attrs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCredentialDefinitionId() {
        return credentialDefinitionId;
    }

    public void setCredentialDefinitionId(String credentialDefinitionId) {
        this.credentialDefinitionId = credentialDefinitionId;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public List<String> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<String> attrs) {
        this.attrs = attrs;
    }
}