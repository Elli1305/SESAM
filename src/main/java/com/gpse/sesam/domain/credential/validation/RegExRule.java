package com.gpse.sesam.domain.credential.validation;

import com.gpse.sesam.domain.credential.issuing.FormEntryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.regex.Pattern;

@Entity
public class RegExRule extends ValidationRule {

    @Column
    private String regEx;
    @Column
    private String description;

    public RegExRule(String regEx, String description) {
        this.regEx = regEx;
        this.description = description;
    }

    protected RegExRule() {

    }

    @Override
    public boolean validate(String input, FormEntryType type) {
        if (type == FormEntryType.TEXT) {
            return Pattern.compile(regEx).matcher(input).matches();
        }
        throw new IllegalArgumentException("RegEx validation is not possible for type " + type);
    }

    public String getRegEx() {
        return regEx;
    }

    public void setRegEx(String regEx) {
        this.regEx = regEx;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
