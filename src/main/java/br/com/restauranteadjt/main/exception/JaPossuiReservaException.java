package br.com.restauranteadjt.main.exception;

public class JaPossuiReservaException extends RuntimeException{
    public JaPossuiReservaException(String message) {
        super(message);
    }

    public JaPossuiReservaException(String message, Throwable cause) {
        super(message, cause);
    }
}
