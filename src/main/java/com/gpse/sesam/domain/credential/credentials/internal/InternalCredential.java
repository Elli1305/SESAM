package com.gpse.sesam.domain.credential.credentials.internal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gpse.sesam.domain.credential.category.Category;
import com.gpse.sesam.domain.credential.credentials.Credential;
import com.gpse.sesam.domain.credential.issue.issuing.ChecklistEntry;
import com.gpse.sesam.domain.credential.issue.issuing.FormEntry;
import com.gpse.sesam.domain.user.issuer.Issuer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class InternalCredential implements Credential {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String version;

	@Column(nullable = false, unique = true)
	private String credentialDefinitionId;

	@Column(nullable = false)
	private String agent;

	@JsonBackReference("credential_category")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Category category;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<FormEntry> form;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ChecklistEntry> checklist;

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "credentials")
	private List<Issuer> issuers = new ArrayList<>();

	protected InternalCredential() {
	}

	public InternalCredential(final String name, final String version, final String credentialDefinitionId,
							  final String agent, final List<FormEntry> form,
							  final List<ChecklistEntry> checklist) {
		this.name = name;
		this.version = version;
		this.credentialDefinitionId = credentialDefinitionId;
		this.agent = agent;
		this.form = form;
		this.checklist = checklist;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String getCredentialDefinitionId() {
		return credentialDefinitionId;
	}

	@Override
	public void setCredentialDefinitionId(final String credentialDefinitionId) {
		this.credentialDefinitionId = credentialDefinitionId;
	}

	@Override
	public List<FormEntry> getForm() {
		return form;
	}

	@Override
	public void setForm(final List<FormEntry> form) {
		this.form = form;
	}

	public List<ChecklistEntry> getChecklist() {
		return checklist;
	}

	public void setChecklist(final List<ChecklistEntry> checklist) {
		this.checklist = checklist;
	}

	public List<Issuer> getIssuer() {
		return issuers;
	}

	public void setIssuer(final List<Issuer> issuers) {
		this.issuers = issuers;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(final String agent) {
		this.agent = agent;
	}

	public Category getCategory() {
		return category;
	}


	public void setCategory(final Category category) {
		this.category = category;
	}

	public void addIssuer(final Issuer issuer) {
		issuers.add(issuer);
	}

	public void removeIssuer(final Issuer issuer) {
		issuers.remove(issuer);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final InternalCredential that = (InternalCredential) o;

		return Objects.equals(credentialDefinitionId, that.credentialDefinitionId);
	}

	@Override
	public int hashCode() {
		return credentialDefinitionId != null ? credentialDefinitionId.hashCode() : 0;
	}
}
