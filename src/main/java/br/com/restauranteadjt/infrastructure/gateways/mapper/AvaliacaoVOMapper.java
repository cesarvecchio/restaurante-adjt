package br.com.restauranteadjt.infrastructure.gateways.mapper;

import br.com.restauranteadjt.domain.entity.AvaliacaoDomain;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.AvaliacaoVO;

public class AvaliacaoVOMapper {
    public AvaliacaoDomain toDomain(AvaliacaoVO vo){
        return new AvaliacaoDomain(
                vo.getPontos(),
                vo.getComentarios()
        );
    }

    public AvaliacaoVO toVO(AvaliacaoDomain domain){
        return new AvaliacaoVO(
                domain.pontos(),
                domain.comentarios()
        );
    }
}
