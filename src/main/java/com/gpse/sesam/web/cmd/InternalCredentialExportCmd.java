package com.gpse.sesam.web.cmd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gpse.sesam.domain.credential.issue.issuing.FormEntry;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InternalCredentialExportCmd extends ExternalCredentialExportCmd {
    @NotBlank
    @Pattern(regexp = "^(tlabs|university)$")
    protected String agent;

    @NotNull
    protected List<String> conditions;

    public InternalCredentialExportCmd() {
        conditions = new ArrayList<>();
    }

    public InternalCredentialExportCmd(String name, String version, String agent, String credentialDefinitionId,
                                       List<FormEntry> attributes, List<String> conditions) {
        super(name, version, credentialDefinitionId, attributes);

        this.agent = agent;
        this.conditions = conditions;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public List<String> getConditions() {
        return conditions;
    }

    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }
}
