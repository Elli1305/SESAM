package com.gpse.sesam.domain.credential;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column(nullable = false)
	private String name;

	@JsonManagedReference("credential_category")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "category", fetch = FetchType.EAGER)
	private List<Credential> credentials = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "CATEGORY_ID")
	private List<ExternalCredential> externalCredentials = new ArrayList<>();

	protected Category() {
	}

	public Category(final String name) {
		this.name = name;
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

	public List<Credential> getCredentials() {
		return credentials;
	}

	public void setCredentialTypes(final List<Credential> credentials) {
		this.credentials = credentials;
	}

	public List<ExternalCredential> getExternalCredentialList() {
		return externalCredentials;
	}

	public void setExternalCredentialList(final List<ExternalCredential> externalCredentials) {
		this.externalCredentials = externalCredentials;
	}

	public void addCredential(final Credential credential) {
		credentials.add(credential);
		credential.setCategory(this);
	}

	public void removeCredential(final Credential credential) {
		credentials.remove(credential);
		credential.setCategory(null);
	}

	public void setExternalCredentials(final List<ExternalCredential> externalCredentials) {
		this.externalCredentials = externalCredentials;
	}

	public List<ExternalCredential> getExternalCredentials() {
		return externalCredentials;
	}

	public void addExternalCredential(ExternalCredential externalCredential){
		externalCredentials.add(externalCredential);
	}

	public void setCredentials(final List<Credential> credentials) {
		this.credentials = credentials;
	}
}
