package com.gpse.sesam.domain.credential.credentials.external;

import com.gpse.sesam.domain.credential.credentials.Credential;
import jakarta.persistence.*;
import com.gpse.sesam.domain.credential.issuing.FormEntry;

import java.util.List;

@Entity
public class ExternalCredential implements Credential {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String credentialDefinitionId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FormEntry> form;

	protected ExternalCredential() {
	}

    public ExternalCredential(final String name, final String credentialDefinitionId, final List<FormEntry> form) {
        this.name = name;
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

    public List<FormEntry> getForm() {
        return form;
    }

    public void setForm(List<FormEntry> form) {
        this.form = form;
    }
}
