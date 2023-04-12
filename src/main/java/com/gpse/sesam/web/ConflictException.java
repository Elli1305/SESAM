package com.gpse.sesam.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 42L;

    public ConflictException(Throwable throwable) {
        super(throwable);
    }
}
