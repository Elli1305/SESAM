package com.gpse.sesam.web.cmd;

import java.util.ArrayList;
import java.util.List;

public class LocationCmd {
	private Long id;

	private String name;

	private List<BuildingCmd> buildings = new ArrayList<>();

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

	public List<BuildingCmd> getBuildings() {
		return buildings;
	}

	public void setBuildings(List<BuildingCmd> rooms) {
		this.buildings = rooms;
	}

}
