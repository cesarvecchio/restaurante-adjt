package br.com.restauranteadjt.main.exception;

public class StatusReservaException extends RuntimeException {
    public StatusReservaException(String message) {
        super(message);
    }

    public StatusReservaException(String message, Throwable cause) {
        super(message, cause);
    }
}
