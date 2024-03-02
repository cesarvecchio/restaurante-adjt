package br.com.restauranteadjt.infrastructure.controllers.mapper;

import br.com.restauranteadjt.domain.entity.AvaliacaoDomain;
import br.com.restauranteadjt.infrastructure.controllers.dto.request.AvaliacaoRequest;
import br.com.restauranteadjt.infrastructure.controllers.dto.response.AvaliacaoResponse;

public class AvaliacaoDTOMapper {
    public AvaliacaoDomain toDomain(AvaliacaoRequest request) {
        return new AvaliacaoDomain(
                request.getPontuacao(),
                request.getComentario()
        );
    }

    public AvaliacaoResponse toResponse(AvaliacaoDomain domain){
        return new AvaliacaoResponse(
                domain.pontos(),
                domain.comentarios()
        );
    }
}
