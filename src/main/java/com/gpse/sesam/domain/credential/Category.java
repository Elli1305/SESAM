package com.gpse.sesam.domain.credential;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Credential> credentials;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ExternalCredential> externalCredentials;

    protected Category() {
    }

    public Category(String name, List<Credential> credentials, List<ExternalCredential> externalCredentials) {
        this.name = name;
        this.credentials = credentials;
        this.externalCredentials = externalCredentials;
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

    public List<Credential> getCredentials() {
        return credentials;
    }

    public void setCredentialTypes(List<Credential> credentials) {
        this.credentials = credentials;
    }

    public List<ExternalCredential> getExternalCredentialList() {
        return externalCredentials;
    }

    public void setExternalCredentialList(List<ExternalCredential> externalCredentials) {
        this.externalCredentials = externalCredentials;
    }
}
