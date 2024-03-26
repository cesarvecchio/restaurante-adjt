package br.com.restauranteadjt.domain.entity;

import br.com.restauranteadjt.domain.enums.StatusMesa;

public record MesaDomain(
    String id,
    String emailCliente,
    StatusMesa statusMesa
) {
}
