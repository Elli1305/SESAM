package com.gpse.sesam.web.cmd;

import com.gpse.sesam.domain.credential.FormEntryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateAttributeCmd {
    @NotBlank
    private String name;

    @NotNull
    private FormEntryType type;

    @NotBlank
    private String attributeName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FormEntryType getType() {
        return type;
    }

    public void setType(FormEntryType type) {
        this.type = type;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
}
