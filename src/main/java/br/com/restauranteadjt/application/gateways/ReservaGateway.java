package br.com.restauranteadjt.application.gateways;

import br.com.restauranteadjt.domain.entity.ReservaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservaGateway {
    ReservaDomain create(String idRestaurante, ReservaDomain reservaDomain);

}
