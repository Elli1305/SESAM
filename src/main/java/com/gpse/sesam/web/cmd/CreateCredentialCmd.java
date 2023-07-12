package com.gpse.sesam.web.cmd;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CreateCredentialCmd {
    @NotBlank
    private String name;

    @NotBlank
    private String credentialDefinitionId;

    @NotBlank
    private String agent;

    @Valid
    @NotEmpty
    private List<CreateAttributeCmd> attributes;

    @Valid
    @NotEmpty
    private List<CreateConditionCmd> conditions;

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

    public List<CreateAttributeCmd> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<CreateAttributeCmd> attributes) {
        this.attributes = attributes;
    }

    public List<CreateConditionCmd> getConditions() {
        return conditions;
    }

    public void setConditions(List<CreateConditionCmd> conditions) {
        this.conditions = conditions;
    }
}
