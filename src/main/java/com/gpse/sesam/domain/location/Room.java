package com.gpse.sesam.domain.location;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private String name;

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "id", fetch = FetchType.EAGER)
	private List<Door> doors = new ArrayList<>();

	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Floor floor;

	protected Room() {

	}

	public Room(final String name) {
		this.name = name;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public List<Door> getDoors() {
		return doors;
	}

	public void setDoors(final List<Door> doors) {
		this.doors = doors;
	}

	public void addDoor(Door door) {
		doors.add(door);
		door.setRoom(this);
	}

	public Floor getFloor() {
		return floor;
	}

	public void setFloor(Floor floor) {
		this.floor = floor;
	}

	public void removeDoor(Door door) {
		doors.remove(door);
		door.setRoom(null);
	}
}
