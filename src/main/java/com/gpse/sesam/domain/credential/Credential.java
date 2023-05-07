package com.gpse.sesam.domain.credential;

import com.gpse.sesam.domain.user.Issuer;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Credential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String credentialDefinitionId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<FormEntry> form;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ChecklistEntry> checklist;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Issuer> issuer;

    protected Credential() {}

    public Credential(Long id, String name, String credentialDefinitionId, List<FormEntry> form, List<ChecklistEntry> checklist, List<Issuer> issuer) {
        this.id = id;
        this.name = name;
        this.credentialDefinitionId = credentialDefinitionId;
        this.form = form;
        this.checklist = checklist;
        this.issuer = issuer;
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

    public List<ChecklistEntry> getChecklist() {
        return checklist;
    }

    public void setChecklist(List<ChecklistEntry> checklist) {
        this.checklist = checklist;
    }

    public List<Issuer> getIssuer() {
        return issuer;
    }

    public void setIssuer(List<Issuer> issuer) {
        this.issuer = issuer;
    }
}
