package com.gpse.sesam.domain.location.door;

import com.gpse.sesam.domain.credential.credentials.Credential;

import com.gpse.sesam.domain.location.Coordinate;

import jakarta.persistence.CascadeType;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.FetchType;

import jakarta.persistence.GeneratedValue;

import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;

import jakarta.persistence.ManyToMany;

import jakarta.persistence.OneToMany;

import java.sql.Date;

import java.util.ArrayList;

import java.util.List;

@Entity

public class Door {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column

    private Long id;

    @Column
    private String name;

    @Column
    private Date createdAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)

    private List<Coordinate> coordinates = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)

    private List<Credential> credentials = new ArrayList<>();

    protected Door() {

    }

    public Door(final String name, final List<Coordinate> coordinates) {

        this.name = name;

        this.coordinates = coordinates;

        this.createdAt = new Date(System.currentTimeMillis());

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

    public void setCredentials(final List<Credential> credentials) {

        this.credentials = credentials;

    }

    public List<Coordinate> getCoordinates() {

        return coordinates;

    }

    public void setCoordinates(final List<Coordinate> coordinates) {

        this.coordinates = coordinates;

    }

    public void addCredential(final Credential credential) {

        credentials.add(credential);

    }

    public Date getCreatedAt() {

        return createdAt;

    }

    public void setCreatedAt(Date createdAt) {

        this.createdAt = createdAt;
    }
}
