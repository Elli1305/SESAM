package com.gpse.sesam.domain.credential.credentials.external;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gpse.sesam.domain.credential.category.Category;
import com.gpse.sesam.domain.credential.credentials.Credential;
import com.gpse.sesam.domain.credential.issue.issuing.FormEntry;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class ExternalCredential implements Credential {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String credentialDefinitionId;

	@Column(nullable = false)
	private String version;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<FormEntry> form;

	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Category category;

	protected ExternalCredential() {
	}

	public ExternalCredential(final String name, final String version, final String credentialDefinitionId,
							  final List<FormEntry> form) {
		this.name = name;
		this.version = version;
		this.credentialDefinitionId = credentialDefinitionId;
		this.form = form;
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

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public void setVersion(String version) {
		this.version = version;
	}

	public List<FormEntry> getForm() {
        return form;
    }

	@Override
	public void setForm(List<FormEntry> form) {
		this.form = form;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
