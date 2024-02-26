package br.com.restauranteadjt.domain.entity;

import br.com.restauranteadjt.domain.enums.PontuacaoEnum;

public record AvaliacaoDomain(
        PontuacaoEnum pontos,
        String comentarios
) {
}
