package com.gpse.sesam.web.cmd;

import java.util.ArrayList;
import java.util.List;

public class BuildingCmd {
	private Long id;

	private String name;

	private List<FloorCmd> floors = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FloorCmd> getFloors() {
		return floors;
	}

	public void setFloors(List<FloorCmd> floors) {
		this.floors = floors;
	}
}
