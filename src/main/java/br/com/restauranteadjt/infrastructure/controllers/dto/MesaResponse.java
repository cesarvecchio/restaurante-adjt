package br.com.restauranteadjt.infrastructure.controllers.dto;

import br.com.restauranteadjt.domain.enums.StatusMesa;

public record MesaResponse(
        Integer numero,
        StatusMesa statusMesa
) {
}
