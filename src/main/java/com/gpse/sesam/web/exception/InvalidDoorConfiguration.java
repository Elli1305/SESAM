package com.gpse.sesam.web.exception;

import java.io.Serial;

public class InvalidDoorConfiguration extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 47L;

	public InvalidDoorConfiguration(final String s) {
		super(s);
	}
}
