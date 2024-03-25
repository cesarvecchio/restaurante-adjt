package br.com.restauranteadjt.infrastructure.controllers.mapper;

import br.com.restauranteadjt.domain.entity.ReservaDomain;
import br.com.restauranteadjt.infrastructure.controllers.dto.request.CreateReservaRequest;
import br.com.restauranteadjt.infrastructure.controllers.dto.response.ReservaResponse;

public class ReservaDTOMapper {
    public ReservaResponse toResponse(ReservaDomain domain){
        return new ReservaResponse(
                domain.id(),
                domain.dataReserva(),
                domain.horaReserva(),
                domain.nome(),
                domain.email(),
                domain.telefone()
        );
    }

    public ReservaDomain toDomain(CreateReservaRequest request){
        return new ReservaDomain(
                null,
                request.dataReserva(),
                request.horaReserva(),
                request.nome(),
                request.email(),
                request.telefone()
        );
    }
}
