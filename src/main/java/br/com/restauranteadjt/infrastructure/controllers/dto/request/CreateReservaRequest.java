package br.com.restauranteadjt.infrastructure.controllers.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateReservaRequest(
        LocalDate dataReserva,
        LocalTime horaReserva,
        String nome,
        String email,
        String telefone
) {
}
