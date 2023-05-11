package com.gpse.sesam.domain.location;

import com.gpse.sesam.domain.credential.Credential;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Door {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

	@Column
	private String name;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Coordinate> coordinates;

    @OneToMany
    private List<Credential> credentials;

    protected Door() {

    }

	public Door(String name, List<Credential> credentials) {
		this.name =name;
        this.credentials = credentials;
	}

    public Door(String name, List<Coordinate> coordinates) {
        this.name = name;
        this.coordinates = coordinates;
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

	public List<Coordinate> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Coordinate> coordinates) {
		this.coordinates = coordinates;
	}
}
