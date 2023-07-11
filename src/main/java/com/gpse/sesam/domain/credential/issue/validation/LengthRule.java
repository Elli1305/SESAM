package com.gpse.sesam.domain.credential.issue.validation;

import com.gpse.sesam.domain.credential.issue.issuing.FormEntryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class LengthRule extends AbstractValidationRule {

    @Column
    private ComparisonType comparisonType;
    @Column
    private int length;

    private boolean compareWithAttribute;

    private String attributeName;

    public LengthRule(ComparisonType comparisonType, int length) {
        this.comparisonType = comparisonType;
        this.length = length;
    }

    protected LengthRule() {

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
            return comparisonType.validate(input.length(), length);
        }
        throw new IllegalArgumentException("Length validation is not possible for type " + type);
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public void setComparisonType(ComparisonType comparisonType) {
        this.comparisonType = comparisonType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isCompareWithAttribute() {
        return compareWithAttribute;
    }

    public void setCompareWithAttribute(boolean compareWithAttribute) {
        this.compareWithAttribute = compareWithAttribute;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
}
