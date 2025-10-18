package com.example.backend_security.exception;

/**
 * Excepción personalizada para errores en la integración con servicios de Google.
 */
public class GoogleServiceException extends RuntimeException {
    public GoogleServiceException(String message) {
        super(message);
    }
}
