package com.gpse.sesam.domain.location.building;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.floor.Floor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Building {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private String name;

	@JsonManagedReference("building_floor")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "id", fetch = FetchType.EAGER)
	private List<Floor> floors = new ArrayList<>();

	@JsonBackReference("location_building")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Location location;

	protected Building() {

	}

	public Building(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public List<Floor> getFloors() {
		return floors;
	}

	public void setFloors(final List<Floor> floors) {
		this.floors = floors;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void addFloor(final Floor floor) {
		floors.add(floor);
		floor.setBuilding(this);
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(final Location location) {
		this.location = location;
	}

	public void removeFloor(final Floor floor) {
		floors.remove(floor);
		floor.setBuilding(null);
	}
}
