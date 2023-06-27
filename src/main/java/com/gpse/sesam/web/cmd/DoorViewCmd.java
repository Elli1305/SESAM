package com.gpse.sesam.web.cmd;

import com.gpse.sesam.domain.location.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class DoorViewCmd {

	private Long id;

	private String name;

	private List<Coordinate> coordinates = new ArrayList<>();

	private List<DoorConfigViewCmd> proofConfigIn = new ArrayList<>();

	private List<DoorConfigViewCmd> proofConfigOut = new ArrayList<>();

	public DoorViewCmd() {
	}

	public DoorViewCmd(String name, List<Coordinate> coordinates) {
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

	public List<Coordinate> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(final List<Coordinate> coordinates) {
		this.coordinates = coordinates;
	}

	public List<DoorConfigViewCmd> getProofConfigIn() {
		return proofConfigIn;
	}

	public void setProofConfigIn(final List<DoorConfigViewCmd> proofConfigIn) {
		this.proofConfigIn = proofConfigIn;
	}

	public List<DoorConfigViewCmd> getProofConfigOut() {
		return proofConfigOut;
	}

	public void setProofConfigOut(final List<DoorConfigViewCmd> proofConfigOut) {
		this.proofConfigOut = proofConfigOut;
	}
}
