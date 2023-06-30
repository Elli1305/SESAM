package com.gpse.sesam.web.cmd;

public class TwoWayDoorConfigCmd {
	private DoorConfigCmd doorConfigIn;
	private DoorConfigCmd doorConfigOut;

	public DoorConfigCmd getDoorConfigIn() {
		return doorConfigIn;
	}

	public void setDoorConfigIn(DoorConfigCmd doorConfigIn) {
		this.doorConfigIn = doorConfigIn;
	}

	public DoorConfigCmd getDoorConfigOut() {
		return doorConfigOut;
	}

	public void setDoorConfigOut(DoorConfigCmd doorConfigOut) {
		this.doorConfigOut = doorConfigOut;
	}
}
