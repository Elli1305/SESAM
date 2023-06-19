package com.gpse.sesam.domain.location.door;

import com.gpse.sesam.domain.credential.credentials.Credential;
import com.gpse.sesam.domain.location.Coordinate;
import com.gpse.sesam.domain.location.door.config.ProofConfig;
import com.gpse.sesam.domain.location.room.Room;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<Credential> credentials = new ArrayList<>();

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

	public void setName(final String name) {
		this.name = name;
	}

	public List<Credential> getCredentials() {
		return credentials;
	}

	public void setCredentials(final List<Credential> credentials) {
		this.credentials = credentials;
	}

	public List<Coordinate> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(final List<Coordinate> coordinates) {
		this.coordinates = coordinates;
	}

	public void addCredential(final Credential credential) {
		credentials.add(credential);
	}

	public List<ProofConfig> getProofConfigs() {
		return proofConfigs;
	}
}
