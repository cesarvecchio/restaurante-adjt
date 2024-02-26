package br.com.restauranteadjt.application.usecases;

import br.com.restauranteadjt.application.gateways.ReservaGateway;
import br.com.restauranteadjt.domain.entity.MesaDomain;
import br.com.restauranteadjt.domain.entity.ReservaDomain;
import br.com.restauranteadjt.domain.entity.RestauranteDomain;

public class ReservaUseCase {
    private final ReservaGateway reservaGateway;

    public ReservaUseCase(ReservaGateway reservaGateway) {
        this.reservaGateway = reservaGateway;
    }

    public ReservaDomain createReserva(ReservaDomain reservaDomain, RestauranteDomain restauranteDomain, MesaDomain mesaDomain){
        return reservaGateway.createReserva(reservaDomain, restauranteDomain, mesaDomain);
    }
}
