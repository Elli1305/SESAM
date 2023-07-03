package com.gpse.sesam.web.cmd;

import com.gpse.sesam.domain.location.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class DoorCmd {

	private Long id;

	private String name;

	private List<Coordinate> coordinates = new ArrayList<>();

	private List<DoorConfigCmd> proofConfigIn = new ArrayList<>();

	private List<DoorConfigCmd> proofConfigOut = new ArrayList<>();
	private Long roomId;

	public DoorCmd() {
	}

	public DoorCmd(String name, List<Coordinate> coordinates) {
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

	public List<DoorConfigCmd> getProofConfigIn() {
		return proofConfigIn;
	}

	public void setProofConfigIn(final List<DoorConfigCmd> proofConfigIn) {
		this.proofConfigIn = proofConfigIn;
	}

	public List<DoorConfigCmd> getProofConfigOut() {
		return proofConfigOut;
	}

	public void setProofConfigOut(final List<DoorConfigCmd> proofConfigOut) {
		this.proofConfigOut = proofConfigOut;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(final Long roomId) {
		this.roomId = roomId;
	}
}
