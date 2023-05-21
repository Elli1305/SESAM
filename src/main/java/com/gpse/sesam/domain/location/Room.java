package com.gpse.sesam.domain.location;

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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "id")
	private List<Door> doors = new ArrayList<>();

	@ManyToOne(cascade = CascadeType.ALL)
	private Floor floor;

	protected Room() {

	}

	public Room(String name) {
		this.name = name;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Door> getDoors() {
		return doors;
	}

	public void setDoors(List<Door> doors) {
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
	}

	public void removeDoor(Door door) {
		doors.remove(door);
		door.setRoom(null);
	}
}
