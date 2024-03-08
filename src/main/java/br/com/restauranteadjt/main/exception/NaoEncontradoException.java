package br.com.restauranteadjt.main.exception;

public class NaoEncontradoException extends RuntimeException {
    public NaoEncontradoException(String message) {
        super(message);
    }
}
