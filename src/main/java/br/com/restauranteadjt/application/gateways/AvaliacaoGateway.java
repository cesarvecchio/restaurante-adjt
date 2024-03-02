package br.com.restauranteadjt.application.gateways;

import br.com.restauranteadjt.domain.entity.AvaliacaoDomain;

public interface AvaliacaoGateway {
    AvaliacaoDomain create(String idReserva, AvaliacaoDomain avaliacaoDomain);
}
