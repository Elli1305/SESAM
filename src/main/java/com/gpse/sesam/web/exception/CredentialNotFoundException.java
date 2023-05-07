package com.gpse.sesam.web.exception;

import com.gpse.sesam.domain.credential.Credential;

import java.io.Serial;

public class CredentialNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 42L;

    public CredentialNotFoundException(final String message) {
        super(message);
    }
}
