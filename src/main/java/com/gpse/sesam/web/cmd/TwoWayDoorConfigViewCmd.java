package com.gpse.sesam.web.cmd;

public class TwoWayDoorConfigViewCmd {
	private DoorConfigViewCmd doorConfigIn;
	private DoorConfigViewCmd doorConfigOut;


	public TwoWayDoorConfigViewCmd(final DoorConfigViewCmd doorConfigIn, final DoorConfigViewCmd doorConfigOut) {
		this.doorConfigIn = doorConfigIn;
		this.doorConfigOut = doorConfigOut;
	}

	public TwoWayDoorConfigViewCmd() {
	}

	public DoorConfigViewCmd getDoorConfigIn() {
		return doorConfigIn;
	}

	public void setDoorConfigIn(final DoorConfigViewCmd doorConfigIn) {
		this.doorConfigIn = doorConfigIn;
	}

	public DoorConfigViewCmd getDoorConfigOut() {
		return doorConfigOut;
	}

	public void setDoorConfigOut(final DoorConfigViewCmd doorConfigOut) {
		this.doorConfigOut = doorConfigOut;
	}
}
