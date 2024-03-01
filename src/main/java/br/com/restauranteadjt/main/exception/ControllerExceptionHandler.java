package br.com.restauranteadjt.main.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<StandardError> naoEncontradoException(NaoEncontradoException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardError standardError = StandardError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Nao Encontrado Exception")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(CadastradoException.class)
    public ResponseEntity<StandardError> possuiCadastro(CadastradoException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        StandardError standardError = StandardError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Já possui cadastro!")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(CampoVazioException.class)
    public ResponseEntity<StandardError> campoVazio(CampoVazioException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardError standardError = StandardError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Campo vazio!")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(StatusMesaException.class)
    public ResponseEntity<StandardError> statusMesa(StatusMesaException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        StandardError standardError = StandardError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Status Mesa!")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(ReservaException.class)
    public ResponseEntity<StandardError> Reserva(ReservaException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardError standardError = StandardError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Reserva!")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(JaPossuiReservaException.class)
    public ResponseEntity<StandardError> Reserva(JaPossuiReservaException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        StandardError standardError = StandardError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Já Possui Reserva!")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(standardError);
    }
}
