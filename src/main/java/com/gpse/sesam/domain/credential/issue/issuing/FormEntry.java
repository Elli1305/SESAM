package com.gpse.sesam.domain.credential.issue.issuing;

import com.gpse.sesam.domain.credential.issue.validation.AbstractValidationRule;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class FormEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column(nullable = false)
	@NotEmpty
	private String label;

	@Column(nullable = false)
	@NotNull
	private FormEntryType type;

	@Column(nullable = false)
	@NotEmpty
	private String attributeName;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@NotNull
	private List<AbstractValidationRule> validationRules;

	protected FormEntry() {
	}

	public FormEntry(final String label, final FormEntryType type, final String attributeName) {
		this(label, type, attributeName, new ArrayList<>());
	}

	public FormEntry(final String label, final FormEntryType type,
					 final String attributeName,
					 final List<AbstractValidationRule> validationRules) {
		this.label = label;
		this.type = type;
		this.attributeName = attributeName;
		this.validationRules = validationRules;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public FormEntryType getType() {
		return type;
	}

	public void setType(final FormEntryType type) {
		this.type = type;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public List<AbstractValidationRule> getValidationRules() {
		return validationRules;
	}

	public void setValidationRules(final List<AbstractValidationRule> validationRules) {
		this.validationRules = validationRules;
	}
}
