package com.gpse.sesam.web.cmd;

public class TwoWayDoorConfigCmd {

	private Long id;
	private DoorConfigCmd doorConfigIn;
	private DoorConfigCmd doorConfigOut;
	private String startTime;

	private String endTime;

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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
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
