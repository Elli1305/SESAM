package com.gpse.sesam.domain.credential.issuing;

public enum FormEntryType {
    TEXT("text"),
    DATE("date"),
    NUMBER("number");

    private String value;

    FormEntryType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
