package com.gpse.sesam.domain.location.door;

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
import com.gpse.sesam.domain.location.room.Room;
import jakarta.persistence.*;
import com.gpse.sesam.domain.location.door.config.ProofConfig;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Coordinate> coordinates = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ProofConfig> proofConfigIn = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ProofConfig> proofConfigOut = new ArrayList<>();

	public Door() {

	}
	@OneToMany(cascade =  CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ProofConfig> proofConfigs = new ArrayList<>();

	protected Door() {

	}

	public Door(final String name, final List<Coordinate> coordinates) {
		this.name = name;
		this.coordinates = coordinates;
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

	public List<ProofConfig> getProofConfigIn() {
		return proofConfigIn;
	}

	public void setProofConfigIn(final List<ProofConfig> proofConfigIn) {
		this.proofConfigIn = proofConfigIn;
	}

	public List<ProofConfig> getProofConfigOut() {
		return proofConfigOut;
	}

	public void setProofConfigOut(final List<ProofConfig> proofConfigOut) {
		this.proofConfigOut = proofConfigOut;
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


	public void addProofConfigIn(final ProofConfig proofConfig) {
		proofConfigIn.add(proofConfig);
	}

	public void addProofConfigOut(final ProofConfig proofConfig) {

		proofConfigOut.add(proofConfig);
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

	public List<ProofConfig> getProofConfigs() {
		return proofConfigs;
	}
}
