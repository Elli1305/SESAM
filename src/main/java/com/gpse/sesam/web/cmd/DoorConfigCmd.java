package com.gpse.sesam.web.cmd;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

public class DoorConfigCmd {
	private String doorId;
	private String description;
	private List<ConfigPartsCmd> configParts;

	public List<ConfigPartsCmd> getConfigParts() {
		return configParts;
	}

	private LocalTime startTime;
	private LocalTime endTime;

	public void setConfigParts(final List<ConfigPartsCmd> configParts) {
		this.configParts = configParts;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getDoorId() {
		return doorId;
	}

	public void setDoorId(final String doorId) {
		this.doorId = doorId;
	}


	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
}
