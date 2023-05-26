package com.gpse.sesam.domain.credential;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gpse.sesam.domain.location.Door;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gpse.sesam.domain.user.Issuer;
import jakarta.persistence.*;

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

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Category category;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FormEntry> form;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ChecklistEntry> checklist;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Issuer> issuer;

    @JsonBackReference
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Door> doors = new ArrayList<>();

    protected Credential() {
    }

    public Credential(String name, String credentialDefinitionId, String agent, List<FormEntry> form,
                      List<ChecklistEntry> checklist, List<Issuer> issuer) {
        this.name = name;
        this.credentialDefinitionId = credentialDefinitionId;
        this.agent = agent;
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

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public Category getCategory() {
        return category;
    }


    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDoors(List<Door> doors) {
        this.doors = doors;
    }

    public List<Door> getDoors() {
        return doors;
    }

    @PreRemove
    public void removeDoor(){
        for (Door door : this.doors) {
            door.getCredentials().remove(this);
        }
    }
}
