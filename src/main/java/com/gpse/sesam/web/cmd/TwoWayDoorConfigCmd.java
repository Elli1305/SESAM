package com.gpse.sesam.web.cmd;

public class TwoWayDoorConfigCmd {
	private DoorConfigCmd doorConfigIn;
	private DoorConfigCmd doorConfigOut;


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
}
