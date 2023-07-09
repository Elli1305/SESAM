package com.gpse.sesam.web.cmd;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class DeployCredentialTemplateCmd {
    @NotBlank
    private String name;

    @NotBlank
    private String version;

    @NotEmpty
    private List<String> attributes;

    public DeployCredentialTemplateCmd(String name, String version, List<String> attributes) {
        this.name = name;
        this.version = version;
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

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }
}
