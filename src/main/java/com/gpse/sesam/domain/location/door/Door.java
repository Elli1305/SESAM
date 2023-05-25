package com.gpse.sesam.domain.location.door;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gpse.sesam.domain.credential.Credential;
import com.gpse.sesam.domain.location.Coordinate;
import com.gpse.sesam.domain.location.room.Room;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

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
	private List<Coordinate> coordinates = new ArrayList<>();

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

	public void setCredentials(final List<Credential> credentials) {
		this.credentials = credentials;
	}

	public List<Coordinate> getCoordinates() {
		return coordinates;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(final Room room) {
		this.room = room;
	}

	public void addCredential(final Credential credential) {
		credentials.add(credential);
		final List<Door> doors = new ArrayList<>();
		doors.add(this);
		credential.setDoors(doors);
	}

	public void removeCredential(final Credential credential) {
		credentials.remove(credential);
		final List<Door> doors = new ArrayList<>();
		credential.setDoors(doors);
	}

	public void setCoordinates(final List<Coordinate> coordinates) {
		this.coordinates = coordinates;
	}
}
