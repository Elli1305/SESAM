package com.gpse.sesam.domain.credential.validation;

import com.gpse.sesam.domain.credential.issuing.FormEntryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class RegExRule extends ValidationRule {

    @Column
    private String regEx;
    @Column
    private String description;

    @Override
    public boolean validate(String input, FormEntryType type) {
        if (type == FormEntryType.TEXT) {
            return input.matches(regEx);
        }
        throw new IllegalArgumentException("RegEx validation is not possible for type " + type);
    }

    public String getDescription() {
        return description;
    }

}
