package com.gpse.sesam.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST) //<1>
public class LocationNotFoundException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 42L;

	public LocationNotFoundException(final String message) {
		super(message);
	}
}
