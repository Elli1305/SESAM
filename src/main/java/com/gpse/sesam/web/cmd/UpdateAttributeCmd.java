package com.gpse.sesam.web.cmd;

import com.gpse.sesam.domain.credential.issuing.FormEntryType;
import com.gpse.sesam.domain.credential.validation.ValidationRule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class UpdateAttributeCmd {
	private Long id;

	@NotBlank
	private String name;

	@NotNull
	private FormEntryType type;

	@NotBlank
	private String attributeName;

	private List<ValidationRule> validationRules;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
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

	public void setAttributeName(final String attributeName) {
		this.attributeName = attributeName;
	}

	public List<ValidationRule> getValidationRules() {
		return validationRules;
	}

	public void setValidationRules(List<ValidationRule> validationRules) {
		this.validationRules = validationRules;
	}

}

