package com.gpse.sesam.domain.credential.issue.issuing;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FormEntryType {
    TEXT("text"),
    DATE("date"),
    NUMBER("number");

    @JsonValue
    private final String value;

    FormEntryType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
