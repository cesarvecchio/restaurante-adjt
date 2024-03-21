package br.com.restauranteadjt.infrastructure.gateways.mapper;

import br.com.restauranteadjt.domain.entity.ReservaDomain;
import br.com.restauranteadjt.infrastructure.persistence.collection.ReservaCollection;

public class ReservaColletionMapper {
    public ReservaCollection toCollection(ReservaDomain reservaDomain) {
        return new ReservaCollection(
            reservaDomain.dataReserva(),
            reservaDomain.horaReserva(),
            reservaDomain.nome(),
            reservaDomain.email(),
            reservaDomain.telefone()
        );
    }

    public ReservaDomain toDomain(ReservaCollection reservaCollection) {
        return new ReservaDomain(
            reservaCollection.getDataReserva(),
            reservaCollection.getHoraReserva(),
            reservaCollection.getNome(),
            reservaCollection.getEmail(),
            reservaCollection.getTelefone()
        );
    }
}
