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
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column(unique = true, nullable = false)
	private String name;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Building> buildings;

	protected Location() {

	}

	public Location(final String name, final List<Building> buildings) {
		this.name = name;
		this.buildings = buildings;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public List<Building> getBuildings() {
		return buildings;
	}

	public void setBuildings(final List<Building> buildings) {
		this.buildings = buildings;
	}

	public void addBuilding(final Building building) {
		this.buildings.add(building);
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}
}
