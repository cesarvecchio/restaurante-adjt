package br.com.restauranteadjt.application.gateways;

import br.com.restauranteadjt.domain.entity.ReservaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservaGateway {
    ReservaDomain create(String idRestaurante, ReservaDomain reservaDomain);

    List<ReservaDomain> findByIdRestauranteAndHorarioReservaAndDataReservaAndStatusMesa(
            String idRestaurante, LocalTime horarioReserva, LocalDate dataReserva, StatusMesa statusMesa);
}
