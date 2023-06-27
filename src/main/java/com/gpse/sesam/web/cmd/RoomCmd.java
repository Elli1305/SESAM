package com.gpse.sesam.web.cmd;

import com.gpse.sesam.domain.location.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class RoomCmd {

	private Long id;

	private String name;

	private List<DoorViewCmd> doors = new ArrayList<>();

	private List<Coordinate> coordinates = new ArrayList<>();


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

	public List<DoorViewCmd> getDoors() {
		return doors;
	}

	public void setDoors(final List<DoorViewCmd> doors) {
		this.doors = doors;
	}

	public List<Coordinate> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(final List<Coordinate> coordinates) {
		this.coordinates = coordinates;
	}
}
