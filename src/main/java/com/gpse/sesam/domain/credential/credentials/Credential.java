package com.gpse.sesam.domain.credential.credentials;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gpse.sesam.domain.credential.issuing.ChecklistEntry;
import com.gpse.sesam.domain.credential.issuing.FormEntry;
import com.gpse.sesam.domain.credential.category.Category;
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

@Entity
public class Credential {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String credentialDefinitionId;

	@JsonIgnore
	@Column(nullable = false)
	private String agent;

	@JsonBackReference("credential_category")
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	private Category category;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<FormEntry> form;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ChecklistEntry> checklist;

	@ManyToMany(cascade = CascadeType.ALL)
	private List<Issuer> issuers = new ArrayList<>();

	protected Credential() {
	}

	public Credential(final String name, final String credentialDefinitionId, final String agent,
					  final List<FormEntry> form,
					  final List<ChecklistEntry> checklist) {
		this.name = name;
		this.credentialDefinitionId = credentialDefinitionId;
		this.agent = agent;
		this.form = form;
		this.checklist = checklist;
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

	public List<FormEntry> getForm() {
		return form;
	}

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
		issuers.remove(issuer); }
}
