package com.gpse.sesam.web.cmd;

import jakarta.persistence.Column;

import java.time.LocalTime;

public class TwoWayDoorConfigCmd {

	private Long id;
	private DoorConfigCmd doorConfigIn;
	private DoorConfigCmd doorConfigOut;
	private LocalTime startTime;

	private LocalTime endTime;

	private boolean baseConfig;


	public TwoWayDoorConfigCmd(final DoorConfigCmd doorConfigIn, final DoorConfigCmd doorConfigOut) {
		this.doorConfigIn = doorConfigIn;
		this.doorConfigOut = doorConfigOut;
	}

	public TwoWayDoorConfigCmd() {
	}

	public DoorConfigCmd getDoorConfigIn() {
		return doorConfigIn;
	}

	public void setDoorConfigIn(final DoorConfigCmd doorConfigIn) {
		this.doorConfigIn = doorConfigIn;
	}

	public DoorConfigCmd getDoorConfigOut() {
		return doorConfigOut;
	}

	public void setDoorConfigOut(final DoorConfigCmd doorConfigOut) {
		this.doorConfigOut = doorConfigOut;
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

	public boolean isBaseConfig() {
		return baseConfig;
	}

	public void setBaseConfig(boolean baseConfig) {
		this.baseConfig = baseConfig;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
