package com.gpse.sesam.domain.credential.issuing;

import com.gpse.sesam.domain.credential.validation.ValidationRule;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class FormEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column(nullable = false)
	private String label;

	@Column(nullable = false)
	private FormEntryType type;

	@Column(nullable = false)
	private String attributeName;

	@OneToMany(fetch = FetchType.EAGER)
	private List<ValidationRule> validationRules;

	protected FormEntry() {
	}

	public FormEntry(final String label, final FormEntryType type, final String attributeName) {
		this(label, type, attributeName, new ArrayList<>());
	}

	public FormEntry(final String label, final FormEntryType type, final String attributeName, final List<ValidationRule> validationRules) {
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

	public List<ValidationRule> getValidationRules() {
		return validationRules;
	}

	public void setValidationRules(List<ValidationRule> validationRules) {
		this.validationRules = validationRules;
	}
}
