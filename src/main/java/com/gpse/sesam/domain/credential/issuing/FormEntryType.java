package com.gpse.sesam.domain.credential.issuing;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FormEntryType {
    TEXT("text"),
    DATE("date"),
    NUMBER("number");

    @JsonValue
    private String value;

    FormEntryType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
