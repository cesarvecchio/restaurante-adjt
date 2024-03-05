package br.com.restauranteadjt.application.gateways;

import br.com.restauranteadjt.domain.entity.AvaliacaoDomain;

import java.util.List;

public interface AvaliacaoGateway {
    AvaliacaoDomain create(String idReserva, AvaliacaoDomain avaliacaoDomain);

    public List<AvaliacaoDomain> listByIdRestaurante(String idRestaurante);
}
