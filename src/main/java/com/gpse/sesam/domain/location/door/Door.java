package com.gpse.sesam.domain.location.door;

import com.gpse.sesam.domain.credential.credentials.Credential;

import com.gpse.sesam.domain.location.Coordinate;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import com.gpse.sesam.domain.location.door.config.ProofConfig;



import java.sql.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Door {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private String name;

	@Column
	private Date createdAt;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Coordinate> coordinates = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<TwoWayDoorConfig> doorConfigs = new ArrayList<>();

	//@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)

	//private List<Credential> credentials = new ArrayList<>();


	public Door() {

	}

	public Door(final String name, final List<Coordinate> coordinates) {
		this.name = name;
		this.coordinates = coordinates;
		this.createdAt = new Date(System.currentTimeMillis());
	}

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

	public List<Coordinate> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(final List<Coordinate> coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Door door = (Door) o;

		if (!Objects.equals(name, door.name)) {
			return false;
		}
		return Objects.equals(coordinates, door.coordinates);
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
		return result;
	}


	public List<TwoWayDoorConfig> getDoorConfigs() {
		return doorConfigs;
	}

	public void setDoorConfigs(List<TwoWayDoorConfig> doorConfigs) {
		this.doorConfigs = doorConfigs;
	}

	public Date getCreatedAt() {

		return createdAt;

	}

	public void setCreatedAt(Date createdAt) {

		this.createdAt = createdAt;
	}
}
