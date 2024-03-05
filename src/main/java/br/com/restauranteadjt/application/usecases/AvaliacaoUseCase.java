package br.com.restauranteadjt.application.usecases;

import br.com.restauranteadjt.application.gateways.AvaliacaoGateway;
import br.com.restauranteadjt.domain.entity.AvaliacaoDomain;

import java.util.List;

public class AvaliacaoUseCase {
    private final AvaliacaoGateway avaliacaoGateway;

    public AvaliacaoUseCase(AvaliacaoGateway avaliacaoGateway) {
        this.avaliacaoGateway = avaliacaoGateway;
    }

    public AvaliacaoDomain create(String idReserva, AvaliacaoDomain avaliacaoDomain) {
        return avaliacaoGateway.create(idReserva, avaliacaoDomain);
    }

    public List<AvaliacaoDomain> listByIdRestaurante(String idRestaurante){
        return avaliacaoGateway.listByIdRestaurante(idRestaurante);
    }
}
