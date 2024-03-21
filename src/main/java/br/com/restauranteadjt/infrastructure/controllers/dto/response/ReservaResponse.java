package br.com.restauranteadjt.infrastructure.controllers.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservaResponse(
    LocalDate dataReserva,
    LocalTime horaReserva,
    String nome,
    String email,
    String telefone
) {
}
