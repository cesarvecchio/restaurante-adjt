package br.com.restauranteadjt.domain.entity;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservaDomain(
        LocalDate dataReserva,
        LocalTime horaReserva
) {

}
