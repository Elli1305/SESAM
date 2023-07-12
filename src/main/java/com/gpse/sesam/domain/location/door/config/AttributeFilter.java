package com.gpse.sesam.domain.location.door.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gpse.sesam.util.AttributeFilterSerializer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@JsonSerialize(using = AttributeFilterSerializer.class)
@Entity
public class AttributeFilter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@JsonIgnore
	private Long id;

	@Column
	private String credentialDefinitionId;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private AttributeValue attributeValue;

	public AttributeFilter() {

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
