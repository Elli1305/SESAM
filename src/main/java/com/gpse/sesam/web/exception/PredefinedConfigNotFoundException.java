package com.gpse.sesam.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PredefinedConfigNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 42L;

    public PredefinedConfigNotFoundException(final String message) {
        super(message);
    }
}
