package com.gpse.sesam.domain.credential.issue.validation;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gpse.sesam.domain.credential.issue.issuing.FormEntryType;
import jakarta.persistence.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "kind")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ComparisonRule.class, name = "comparison"),
        @JsonSubTypes.Type(value = RangeRule.class, name = "range"),
        @JsonSubTypes.Type(value = RegExRule.class, name = "regEx"),
        @JsonSubTypes.Type(value = LengthRule.class, name = "length")
})
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractValidationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public abstract boolean validate(final String input, final FormEntryType type);
}
