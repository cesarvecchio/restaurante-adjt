package br.com.restauranteadjt.main.exception;

public class CampoVazioException extends RuntimeException{
    public CampoVazioException(String message) {
        super(message);
    }

    public CampoVazioException(String message, Throwable cause) {
        super(message, cause);
    }
}
