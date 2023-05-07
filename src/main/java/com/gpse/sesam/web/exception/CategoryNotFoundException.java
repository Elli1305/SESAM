package com.gpse.sesam.web.exception;

import java.io.Serial;

public class CategoryNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 42L;

    public CategoryNotFoundException(final String message) {
        super(message);
    }
}
