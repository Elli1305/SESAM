package com.gpse.sesam.domain.location.room;

import com.gpse.sesam.domain.location.Coordinate;
import com.gpse.sesam.domain.location.door.Door;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "ROOM_ID")
	private List<Door> doors = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL)
	private List<Coordinate> coordinates;

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

	public List<Coordinate> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(final List<Coordinate> coordinates) {
		this.coordinates = coordinates;
	}

	public void addDoor(final Door door) {
		doors.add(door);
	}
}
