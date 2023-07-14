package com.gpse.sesam.web.cmd;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class UpdateCredentialCmd {

    @NotBlank
    private String name;

    @NotBlank
    private String version;

    @NotBlank
    private String credentialDefinitionId;

    @NotBlank
    private String agent;

    @Valid
    @NotEmpty
    private List<UpdateAttributeCmd> attributes;

    @Valid
    @NotEmpty
    private List<UpdateConditionCmd> conditions;

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

    public List<UpdateAttributeCmd> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<UpdateAttributeCmd> attributes) {
        this.attributes = attributes;
    }

    public List<UpdateConditionCmd> getConditions() {
        return conditions;
    }

    public void setConditions(List<UpdateConditionCmd> conditions) {
        this.conditions = conditions;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
