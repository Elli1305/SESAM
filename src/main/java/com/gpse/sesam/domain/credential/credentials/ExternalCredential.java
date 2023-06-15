package com.gpse.sesam.domain.credential.credentials;

import com.gpse.sesam.domain.credential.issuing.FormEntry;
import jakarta.persistence.*;

import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FormEntry> form;

    protected ExternalCredential() {
    }

    public ExternalCredential(String name, String credentialDefinitionId, List<FormEntry> form) {
        this.name = name;
        this.credentialDefinitionId = credentialDefinitionId;
        this.form = form;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCredentialDefinitionId() {
        return credentialDefinitionId;
    }

    public void setCredentialDefinitionId(String credentialDefinitionId) {
        this.credentialDefinitionId = credentialDefinitionId;
    }

    public List<FormEntry> getForm() {
        return form;
    }

    public void setForm(List<FormEntry> form) {
        this.form = form;
    }
}
