package com.gpse.sesam.web.cmd;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class DeployCredentialCmd {
    @NotBlank
    @Pattern(regexp = "^(tlabs|university)$")
    private String agent;

    @NotBlank
    private String tag;

    @Valid
    private DeployCredentialTemplateCmd template;

    public DeployCredentialCmd(String agent, String tag, DeployCredentialTemplateCmd template) {
        this.agent = agent;
        this.tag = tag;
        this.template = template;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public DeployCredentialTemplateCmd getTemplate() {
        return template;
    }

    public void setTemplate(DeployCredentialTemplateCmd template) {
        this.template = template;
    }
}
