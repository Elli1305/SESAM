package com.gpse.sesam.domain.credential.validation;

import com.gpse.sesam.domain.credential.issuing.FormEntryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class RangeRule extends AbstractValidationRule {

    @Column
    private String valueFrom;

    @Column
    private String valueTo;

    private boolean compareWithAttribute;

    private String attributeNameFrom;

    private String attributeNameTo;

    public RangeRule(String valueFrom, String valueTo) {
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
    }

    protected RangeRule() {

    }

    @Override
    public boolean validate(String input, FormEntryType type) {
        switch (type) {
            case NUMBER -> {
                int integerInput = Integer.parseInt(input);
                int integerFrom = Integer.parseInt(valueFrom);
                int integerTo = Integer.parseInt(valueTo);
                return integerFrom <= integerInput && integerInput <= integerTo;
            }
            case DATE -> {
                String[] inputString = input.split("-");
                LocalDate dateInput = LocalDate.of(Integer.parseInt(inputString[0]),
                        Integer.parseInt(inputString[1]), Integer.parseInt(inputString[2]));
                String[] fromString = valueFrom.split("-");
                LocalDate dateFrom = LocalDate.of(Integer.parseInt(fromString[0]),
                        Integer.parseInt(fromString[1]), Integer.parseInt(fromString[2]));
                String[] toString = valueTo.split("-");
                LocalDate dateTo = LocalDate.of(Integer.parseInt(toString[0]),
                        Integer.parseInt(toString[1]), Integer.parseInt(toString[2]));
                return (dateInput.isAfter(dateFrom) || dateInput.isEqual(dateFrom))
                        && (dateInput.isBefore(dateTo) || dateInput.isEqual(dateTo));
            }
            default ->  {
                throw new IllegalArgumentException("Range validation is not possible for given type " + type);
            }
        }
    }

    public String getValueFrom() {
        return valueFrom;
    }

    public void setValueFrom(String valueFrom) {
        this.valueFrom = valueFrom;
    }

    public String getValueTo() {
        return valueTo;
    }

    public void setValueTo(String valueTo) {
        this.valueTo = valueTo;
    }

    public boolean isCompareWithAttribute() {
        return compareWithAttribute;
    }

    public void setCompareWithAttribute(boolean compareWithAttribute) {
        this.compareWithAttribute = compareWithAttribute;
    }

    public String getAttributeNameFrom() {
        return attributeNameFrom;
    }

    public void setAttributeNameFrom(String attributeName) {
        this.attributeNameFrom = attributeName;
    }

    public String getAttributeNameTo() {
        return attributeNameTo;
    }

    public void setAttributeNameTo(String attributeNameTo) {
        this.attributeNameTo = attributeNameTo;
    }
}
