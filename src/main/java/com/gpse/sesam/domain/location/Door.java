package com.gpse.sesam.domain.location;

import com.gpse.sesam.domain.credential.Credential;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Door {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;


    @OneToMany
    private List<Credential> credentials;

    protected Door() {

    }

    public Door(List<Credential> credentials) {
        this.credentials = credentials;
    }

    public Door(String name) {
        this.name = name;
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

    public void setCredentials(List<Credential> credentials) {
        this.credentials = credentials;
    }
}
