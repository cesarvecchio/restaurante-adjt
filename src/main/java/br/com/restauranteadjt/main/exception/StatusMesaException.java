package br.com.restauranteadjt.main.exception;

public class StatusMesaException extends RuntimeException {
    public StatusMesaException(String message) {
        super(message);
    }

    public StatusMesaException(String message, Throwable cause) {
        super(message, cause);
    }
}
