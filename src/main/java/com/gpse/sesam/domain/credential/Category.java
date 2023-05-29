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

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "id", fetch = FetchType.EAGER)
    private List<Credential> credentials = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<ExternalCredential> externalCredentials;

    protected Category() {
    }

    public Category(String name, List<ExternalCredential> externalCredentials) {
        this.name = name;
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

    public void addCredential(Credential credential) {
        credentials.add(credential);
        credential.setCategory(this);
    }

    public void removeCredential(Credential credential) {
        credentials.remove(credential);
        credential.setCategory(null);
    }

    public void setExternalCredentials(List<ExternalCredential> externalCredentials) {
        this.externalCredentials = externalCredentials;
    }

    public List<ExternalCredential> getExternalCredentials() {
        return externalCredentials;
    }

    public void setCredentials(List<Credential> credentials) {
        this.credentials = credentials;
    }
}
