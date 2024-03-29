package br.com.restauranteadjt.infrastructure.controllers.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservaResponse(
        String id,
        LocalDate dataReserva,
        LocalTime horaReserva,
        String nome,
        String email,
        String telefone
) {
}
