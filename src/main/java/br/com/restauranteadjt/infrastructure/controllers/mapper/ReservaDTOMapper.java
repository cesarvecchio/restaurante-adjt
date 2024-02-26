package br.com.restauranteadjt.infrastructure.controllers.mapper;

import br.com.restauranteadjt.domain.entity.ReservaDomain;
import br.com.restauranteadjt.infrastructure.controllers.dto.request.CreateReservaRequest;
import br.com.restauranteadjt.infrastructure.controllers.dto.response.ReservaResponse;

public class ReservaDTOMapper {
    public ReservaResponse toResponse(ReservaDomain domain, String id){
        return new ReservaResponse(
                id,
                domain.dataReserva(),
                domain.horaReserva()
        );
    }

    public ReservaDomain toDomain(CreateReservaRequest request){
        return new ReservaDomain(
                request.dataReserva(),
                request.horaReserva()
        );
    }
}
