package com.gpse.sesam.web.cmd;

import com.gpse.sesam.domain.location.room.Room;

import java.util.List;

public class IssuerResponseCmd {

	private Long issuerId;
	private List<Long> credentials;
	private Room room;

	protected IssuerResponseCmd() {

	}

	public IssuerResponseCmd(final java.util.List<Long> credentials, final Room room) {
		this.credentials = credentials;
		this.room = room;
	}

	public void setCredentials(final List<Long> credentials) {
		this.credentials = credentials;
	}

	public void setRoom(final Room room) {
		this.room = room;
	}

	public List<Long> getCredentials() {
		return credentials;
	}

	public Room getRoom() {
		return room;
	}

	public Long getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(final Long issuerId) {
		this.issuerId = issuerId;
	}
}
