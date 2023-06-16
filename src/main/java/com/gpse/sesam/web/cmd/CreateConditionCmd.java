package com.gpse.sesam.web.cmd;

import jakarta.validation.constraints.NotBlank;

public class CreateConditionCmd {
    @NotBlank
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
