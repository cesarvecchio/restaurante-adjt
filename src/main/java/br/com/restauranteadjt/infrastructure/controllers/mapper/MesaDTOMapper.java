package br.com.restauranteadjt.infrastructure.controllers.mapper;

import br.com.restauranteadjt.domain.entity.MesaDomain;
import br.com.restauranteadjt.infrastructure.controllers.dto.MesaResponse;

public class MesaDTOMapper {
    public MesaResponse toResponse(MesaDomain mesaDomain) {
        return new MesaResponse(
                mesaDomain.numero(),
                mesaDomain.statusMesa()
        );
    }

    public MesaDomain toDomain(MesaResponse mesaResponse) {
        return new MesaDomain(
                mesaResponse.numero(),
                mesaResponse.statusMesa()
        );
    }
}
