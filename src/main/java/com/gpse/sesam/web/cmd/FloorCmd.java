package com.gpse.sesam.web.cmd;

import java.util.ArrayList;
import java.util.List;

public class FloorCmd {
	private Long id;

	private int floorLevel;

	private String floorPlanPath;

	private List<RoomCmd> rooms = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getFloorLevel() {
		return floorLevel;
	}

	public void setFloorLevel(int floorLevel) {
		this.floorLevel = floorLevel;
	}

	public List<RoomCmd> getRooms() {
		return rooms;
	}

	public void setRooms(List<RoomCmd> rooms) {
		this.rooms = rooms;
	}

	public String getFloorPlanPath() {
		return floorPlanPath;
	}

	public void setFloorPlanPath(String floorPlanPath) {
		this.floorPlanPath = floorPlanPath;
	}
}
