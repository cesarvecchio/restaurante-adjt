package br.com.restauranteadjt.application.gateways;

import br.com.restauranteadjt.domain.entity.MesaDomain;
import br.com.restauranteadjt.domain.entity.ReservaDomain;
import br.com.restauranteadjt.domain.entity.RestauranteDomain;

public interface ReservaGateway {
    ReservaDomain createReserva(ReservaDomain reservaDomain, RestauranteDomain restauranteDomain, MesaDomain mesaDomain);
}
