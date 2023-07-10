package com.gpse.sesam.web.cmd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gpse.sesam.domain.credential.issuing.FormEntryType;
import com.gpse.sesam.domain.credential.validation.AbstractValidationRule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateAttributeCmd {
	@NotBlank
	private String name;

	@NotNull
	private FormEntryType type;

	@NotBlank
	private String attributeName;

	private List<AbstractValidationRule> validationRules;

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

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public List<AbstractValidationRule> getValidationRules() {
		return validationRules;
	}

	public void setValidationRules(List<AbstractValidationRule> validationRules) {
		this.validationRules = validationRules;
	}
}
