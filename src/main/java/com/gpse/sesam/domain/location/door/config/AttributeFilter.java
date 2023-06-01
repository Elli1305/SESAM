package com.gpse.sesam.domain.location.door.config;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gpse.sesam.util.AttributeFilterSerializer;
import jakarta.persistence.*;

@JsonSerialize(using = AttributeFilterSerializer.class)
@Entity
public class AttributeFilter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private String credentialDefinitionId;

	@OneToOne
	private AttributeValue attributeValue;

	protected AttributeFilter() {

	}

	public Long getId() {
		return id;
	}

	public String getCredentialDefinitionId() {
		return credentialDefinitionId;
	}

	public void setCredentialDefinitionId(final String credentialDefinitionId) {
		this.credentialDefinitionId = credentialDefinitionId;
	}

	public AttributeValue getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(final AttributeValue attributeValue) {
		this.attributeValue = attributeValue;
	}
}
