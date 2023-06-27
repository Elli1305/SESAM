package com.gpse.sesam.domain.location.floor;

import com.gpse.sesam.domain.location.room.Room;
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
import java.util.Objects;

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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "FLOOR_ID")
	private List<Room> rooms = new ArrayList<>();

	public Floor() {

	}

	public Floor(final int floorLevel, final String floorPlanPath) {
		this.floorLevel = floorLevel;
		this.floorPlanPath = floorPlanPath;
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

	public String getFloorPlanPath() {
		return floorPlanPath;
	}

	public void setFloorPlanPath(final String floorPlanPath) {
		this.floorPlanPath = floorPlanPath;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final Floor floor = (Floor) o;

		return Objects.equals(id, floor.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	public void addRoom(final Room room) {
		rooms.add(room);
	}
}
