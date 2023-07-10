package com.gpse.sesam.web.cmd;

import jakarta.validation.constraints.NotBlank;

public class UpdateConditionCmd {
    private Long id;

    @NotBlank
    private String label;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
