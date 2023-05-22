package com.gpse.sesam.web.cmd;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AttributeType {
    TEXT("text"),
    DATE("date"),
    NUMBER("number");

    @JsonValue
    private String value;

    AttributeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
