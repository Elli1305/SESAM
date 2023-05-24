package com.gpse.sesam.domain.credential;

import jakarta.persistence.*;

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

    public ExternalCredential(String name, String credentialDefinitionId) {
        this.name = name;
        this.credentialDefinitionId = credentialDefinitionId;
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
}
