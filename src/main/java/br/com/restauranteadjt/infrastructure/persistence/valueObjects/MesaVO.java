package br.com.restauranteadjt.infrastructure.persistence.valueObjects;

import br.com.restauranteadjt.domain.enums.StatusMesa;

public record MesaVO(
        Integer identificador,
        StatusMesa statusMesa
) {
}
