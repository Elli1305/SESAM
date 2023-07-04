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

    private boolean compareWithAttribute;

    private String attributeName;

    public LengthRule(ComparisonType comparisonType, int length) {
        this.comparisonType = comparisonType;
        this.length = length;
    }

    protected LengthRule() {

    }

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
