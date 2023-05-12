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
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Coordinate> coordinates;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Credential> credentials;

    protected Door() {

    }

    public Door(String name, List<Coordinate> coordinates, List<Credential> credentials) {
        this.name = name;
        this.coordinates = coordinates;
        this.credentials = credentials;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
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
