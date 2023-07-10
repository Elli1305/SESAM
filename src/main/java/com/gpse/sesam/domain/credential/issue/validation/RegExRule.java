package com.gpse.sesam.domain.credential.issue.validation;

import com.gpse.sesam.domain.credential.issue.issuing.FormEntryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.regex.Pattern;

@Entity
public class RegExRule extends AbstractValidationRule {

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

    /**
     * Validiert die Eingabe basierend auf dem angegebenen FormEntryType.
     *
     * @param input für die zu validierende Eingabe
     * @param type  der FormEntryType, der die Validierungskriterien festlegt
     * @return boolean, wenn die Eingabe gemäß dem angegebenen Typ gültig ist, andernfalls false
     * @throws IllegalArgumentException wenn der angegebene Typ nicht für die Bereichsvalidierung unterstützt wird
     */
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
