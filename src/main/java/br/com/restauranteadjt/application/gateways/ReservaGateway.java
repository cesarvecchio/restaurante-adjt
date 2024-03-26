package br.com.restauranteadjt.application.gateways;

import br.com.restauranteadjt.domain.entity.ReservaDomain;

public interface ReservaGateway {
    ReservaDomain create(String idRestaurante, ReservaDomain reservaDomain);

}
