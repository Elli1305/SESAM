package com.gpse.sesam.domain.location;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gpse.sesam.domain.credential.Credential;
import jakarta.persistence.*;

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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Coordinate> coordinates;

	@JsonManagedReference
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "doors", fetch = FetchType.EAGER)
    private List<Credential> credentials = new ArrayList<>();

	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Room room;

    protected Door() {

    }

    public Door(final String name, final List<Coordinate> coordinates) {
        this.name = name;
        this.coordinates = coordinates;
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

    public void setCredentials(List<Credential> credentials) {
        this.credentials = credentials;
    }

	public List<Coordinate> getCoordinates() {
		return coordinates;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public void addCredential(Credential credential) {
		credentials.add(credential);
		List<Door> doors = new ArrayList<>();
		doors.add(this);
		credential.setDoors(doors);
	}

	public void removeCredential(Credential credential) {
		credentials.remove(credential);
		List<Door> doors = new ArrayList<>();
		credential.setDoors(doors);
	}

	public void setCoordinates(final List<Coordinate> coordinates) {
		this.coordinates = coordinates;
	}
}
