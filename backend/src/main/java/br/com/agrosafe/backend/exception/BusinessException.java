package br.com.agrosafe.backend.exception;

import org.springframework.http.HttpStatus;

/** Violação de regra de negócio (por padrão HTTP 422; use 409 para conflitos de estado). */
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    public BusinessException(String message) {
        this(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}