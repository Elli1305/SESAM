package com.gpse.sesam.domain.location.building;

import com.gpse.sesam.domain.location.floor.Floor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Building {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private String name;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Floor> floors;

	protected Building() {

	}

	public Building(final String name, final List<Floor> floors) {
		this.name = name;
		this.floors = floors;
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
		this.floors.add(floor);
	}
}
