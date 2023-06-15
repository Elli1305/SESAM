package com.gpse.sesam.domain.credential.credentials;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ExternalCredential {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String credentialDefinitionId;

	protected ExternalCredential() {
	}

	public ExternalCredential(final String name, final String credentialDefinitionId) {
		this.name = name;
		this.credentialDefinitionId = credentialDefinitionId;
	}

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

	public String getCredentialDefinitionId() {
		return credentialDefinitionId;
	}

	public void setCredentialDefinitionId(final String credentialDefinitionId) {
		this.credentialDefinitionId = credentialDefinitionId;
	}
}
