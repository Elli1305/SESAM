package com.gpse.sesam.domain.credential;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;

public record IssueCredentialAttribute(@NotEmpty String name, @NotEmpty String value, @JsonIgnore FormEntryType type) {
    @AssertTrue
    private boolean isNumber() {
        if (type != FormEntryType.NUMBER) {
            return true;
        }

        return value.matches("^[-+]?\\d+$");
    }

    @AssertTrue
    private boolean isDate() {
        if (type != FormEntryType.DATE) {
            return true;
        }

        return value.matches("^\\d{6,7}$");
    }
}
