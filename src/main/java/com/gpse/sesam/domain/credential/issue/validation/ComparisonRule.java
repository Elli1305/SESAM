package com.gpse.sesam.domain.credential.issue.validation;

import com.gpse.sesam.domain.credential.issue.issuing.FormEntryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class ComparisonRule extends AbstractValidationRule {

    @Column
    private ComparisonType comparisonType;

    @Column
    private String content;

    private boolean currentDay;

    private boolean compareWithAttribute;

    private String attributeName;


    public ComparisonRule(ComparisonType comparisonType, String content) {
        this.comparisonType = comparisonType;
        this.content = content;
        this.currentDay = false;
        this.compareWithAttribute = false;
    }

    public ComparisonRule(ComparisonType comparisonType, boolean currentDay) {
        this.comparisonType = comparisonType;
        this.currentDay = currentDay;
        this.content = "";
        this.compareWithAttribute = false;
    }

    public ComparisonRule(ComparisonType comparisonType, boolean compareWithAttribute, String attributeName) {
        this.comparisonType = comparisonType;
        this.content = "";
        this.attributeName = attributeName;
        this.compareWithAttribute = compareWithAttribute;
        this.currentDay = false;
    }

    protected ComparisonRule() {

    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public void setComparisonType(ComparisonType comparisonType) {
        this.comparisonType = comparisonType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(boolean currentDay) {
        this.currentDay = currentDay;
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
        switch (type) {
            case NUMBER -> {
                int integerInput = Integer.parseInt(input);
                int integerContent = Integer.parseInt(content);
                return comparisonType.validate(integerInput, integerContent);
            }
            case DATE -> {
                String[] inputString = input.split("-");
                LocalDate dateInput = LocalDate.of(Integer.parseInt(inputString[0]), Integer.parseInt(inputString[1]),
                        Integer.parseInt(inputString[2]));
                LocalDate dateContent;
                if (currentDay) {
                    dateContent = LocalDate.now();
                } else {
                    String[] contentString = content.split("-");
                    dateContent = LocalDate.of(Integer.parseInt(contentString[0]), Integer.parseInt(contentString[1]),
                            Integer.parseInt(contentString[2]));
                }
                return comparisonType.validate(dateInput, dateContent);
            }
            default -> {
                throw new IllegalArgumentException("Comparison is not possible for given type " + type);
            }
        }
    }

}
