package com.gpse.sesam.domain.credential.validation;

import com.gpse.sesam.domain.credential.issuing.FormEntryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class LengthRule extends ValidationRule {

    @Column
    private ComparisonType comparisonType;
    @Column
    private int length;

    @Override
    public boolean validate(String input, FormEntryType type) {
        if (type == FormEntryType.NUMBER) {
            return comparisonType.validate(input.length(), length);
        }
        throw new IllegalArgumentException("RegEx validation is not possible for type " + type);
    }

}
