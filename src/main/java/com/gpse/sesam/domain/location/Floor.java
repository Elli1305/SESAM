package com.gpse.sesam.domain.location;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
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

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "id", fetch = FetchType.EAGER)
	private List<Room> rooms = new ArrayList<>();

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Building building;

	protected Floor() {

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

	public void addRoom(final Room room) {
		rooms.add(room);
        room.setFloor(this);
    }

	public String getFloorPlanPath() {
		return floorPlanPath;
	}

	public void setFloorPlanPath(final String floorPlanPath) {
		this.floorPlanPath = floorPlanPath;
	}

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public void removeRoom(Room room) {
        rooms.remove(room);
        room.setFloor(null);
    }
}
