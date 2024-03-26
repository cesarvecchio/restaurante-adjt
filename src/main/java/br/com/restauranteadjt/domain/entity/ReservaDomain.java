package br.com.restauranteadjt.domain.entity;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservaDomain(
        String id,
        LocalDate dataReserva,
        LocalTime horaReserva,
        String nome,
        String email,
        String telefone
) {

}
