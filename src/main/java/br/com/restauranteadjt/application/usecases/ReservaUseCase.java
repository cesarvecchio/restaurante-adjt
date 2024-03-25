package br.com.restauranteadjt.application.usecases;

import br.com.restauranteadjt.application.gateways.ReservaGateway;
import br.com.restauranteadjt.domain.entity.ReservaDomain;

public class ReservaUseCase {
    private final ReservaGateway reservaGateway;

    public ReservaUseCase(ReservaGateway reservaGateway) {
        this.reservaGateway = reservaGateway;
    }

    public ReservaDomain create(String idRestaurante, ReservaDomain reservaDomain) {
        return reservaGateway.create(idRestaurante, reservaDomain);
    }
}
