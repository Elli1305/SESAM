package com.gpse.sesam.domain.location;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Floor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private int floorLevel;

	@Column
	private String floorPlanPath;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Room> rooms;

	protected Floor() {

	}

	public Floor(final int floorLevel, final String floorPlanPath, final List<Room> rooms) {
		this.floorLevel = floorLevel;
		this.floorPlanPath = floorPlanPath;
		this.rooms = rooms;
	}

	public int getFloorLevel() {
		return floorLevel;
	}

	public void setFloorLevel(final int floorLevel) {
		this.floorLevel = floorLevel;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(final List<Room> rooms) {
		this.rooms = rooms;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void addRoom(final Room room) {
		this.rooms.add(room);
	}

	public String getFloorPlanPath() {
		return floorPlanPath;
	}

	public void setFloorPlanPath(final String floorPlanPath) {
		this.floorPlanPath = floorPlanPath;
	}
}
