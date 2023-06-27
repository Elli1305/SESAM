package com.gpse.sesam.domain.credential.validation;

import com.gpse.sesam.domain.credential.issuing.FormEntryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class ComparisonRule extends ValidationRule {

    @Column
    private ComparisonType comparisonType;

    @Column
    private String content;

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
                LocalDate dateInput = LocalDate.of(Integer.parseInt(inputString[0]), Integer.parseInt(inputString[1]), Integer.parseInt(inputString[2]));
                LocalDate dateContent;
                if ("currentDate".equals(content))
                    dateContent = LocalDate.now();
                else {
                    String[] contentString = content.split("-");
                    dateContent = LocalDate.of(Integer.parseInt(contentString[0]), Integer.parseInt(contentString[1]), Integer.parseInt(contentString[2]));
                }
                return comparisonType.validate(dateInput, dateContent);
            }
            default ->  {
                throw new IllegalArgumentException("Comparison is not possible for given type " + type);
            }
        }
    }

}
