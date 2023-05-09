package com.gpse.sesam.domain.location;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Door {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@OneToOne(cascade = CascadeType.ALL)
	private Coordinate coordinate;
	@Column
	private String name;

	protected Door() {

	}

	public Door(String name) {
		this.name = name;
	}

	public Door(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public void setName(String name) {
		this.name = name;
	}
}
