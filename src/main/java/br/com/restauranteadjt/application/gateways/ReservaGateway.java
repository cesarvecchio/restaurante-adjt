package br.com.restauranteadjt.application.gateways;

import br.com.restauranteadjt.domain.entity.ReservaDomain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservaGateway {
    ReservaDomain create(String idRestaurante, ReservaDomain reservaDomain);

    List<ReservaDomain> findByIdRestauranteAndHorarioReservaAndDataReserva(
            String idRestaurante, LocalTime horarioReserva, LocalDate dataReserva);
}
