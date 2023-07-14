package com.gpse.sesam.web.cmd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gpse.sesam.domain.credential.issue.issuing.FormEntry;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalCredentialExportCmd {
    @NotEmpty
    protected String name;

    @NotEmpty
    protected String version;

    @NotEmpty
    protected String credentialDefinitionId;

    @JsonIgnoreProperties(value = "id")
    @NotNull
    protected List<FormEntry> attributes;

    public ExternalCredentialExportCmd() {
        attributes = new ArrayList<>();
    }

    public ExternalCredentialExportCmd(String name, String version, String credentialDefinitionId,
                                       List<FormEntry> attributes) {
        this.name = name;
        this.version = version;
        this.credentialDefinitionId = credentialDefinitionId;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCredentialDefinitionId() {
        return credentialDefinitionId;
    }

    public void setCredentialDefinitionId(String credentialDefinitionId) {
        this.credentialDefinitionId = credentialDefinitionId;
    }

    public List<FormEntry> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<FormEntry> attributes) {
        this.attributes = attributes;
    }
}
