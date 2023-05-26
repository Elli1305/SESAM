package com.gpse.sesam.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidTokenException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 45L;

	public InvalidTokenException(final String message) {
		super(message);
	}
}
