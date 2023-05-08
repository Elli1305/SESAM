package com.gpse.sesam.domain.location;

import jakarta.persistence.*;
import java.util.List;


@Entity
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Door> doors;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Coordinate> coordinates;

	protected Room() {

	}

	public Room(String name, List<Door> doors) {
		this.name = name;
		this.doors = doors;
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

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
	}

	public List<Door> getDoors() {
		return doors;
	}

	public void setDoors(List<Door> doors) {
		this.doors = doors;
	}

	public void addDoor(Door door) {
		this.doors.add(door);
	}
}
