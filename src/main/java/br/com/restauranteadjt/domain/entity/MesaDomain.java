package br.com.restauranteadjt.domain.entity;

import br.com.restauranteadjt.domain.enums.StatusMesa;

public record MesaDomain(
        Integer numero,
        StatusMesa statusMesa
) {
}
