package br.com.restauranteadjt.application.gateways;

import br.com.restauranteadjt.domain.entity.MesaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MesaGateway {

    List<MesaDomain> listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
            String idRestaurante, LocalDate dataReserva, LocalTime horaReserva, StatusMesa statusMesa);

    MesaDomain update(String idReserva, StatusMesa statusMesa);
}
