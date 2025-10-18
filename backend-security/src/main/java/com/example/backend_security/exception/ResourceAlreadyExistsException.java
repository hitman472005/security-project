package com.example.backend_security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando cualquier recurso ya existe.
 * Retorna 409 CONFLICT.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String identifier) {
        super(identifier);
    }
}