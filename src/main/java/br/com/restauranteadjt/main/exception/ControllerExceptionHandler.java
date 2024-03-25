package br.com.restauranteadjt.main.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

    @ExceptionHandler(StatusReservaException.class)
    public ResponseEntity<StandardError> statusReserva(StatusReservaException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        StandardError standardError = StandardError.builder()
            .timestamp(Instant.now())
            .status(status.value())
            .error("Status Reserva!")
            .message(e.getMessage())
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(ReservaException.class)
    public ResponseEntity<StandardError> reserva(ReservaException e, HttpServletRequest request) {
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
    public ResponseEntity<StandardError> jaPossuiReserva(JaPossuiReservaException e, HttpServletRequest request) {
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

    @ExceptionHandler(DataInvalidaException.class)
    public ResponseEntity<StandardError> dataInvalida(DataInvalidaException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;

        StandardError standardError = StandardError.builder()
            .timestamp(Instant.now())
            .status(status.value())
            .error("Data Invalida!")
            .message(e.getMessage())
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(status).body(standardError);
    }
}
