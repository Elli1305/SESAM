package com.gpse.sesam.web.cmd;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CreateExternalCredentialCmd {
    @NotBlank
    private String name;

    @NotBlank
    private String credentialDefinitionId;

    @Valid
    @NotEmpty
    private List<CreateAttributeCmd> attributes;

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

    public List<CreateAttributeCmd> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<CreateAttributeCmd> attributes) {
        this.attributes = attributes;
    }
}