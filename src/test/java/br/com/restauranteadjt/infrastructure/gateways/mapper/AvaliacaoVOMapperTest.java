package br.com.restauranteadjt.infrastructure.gateways.mapper;

import br.com.restauranteadjt.domain.entity.AvaliacaoDomain;
import br.com.restauranteadjt.domain.enums.PontuacaoEnum;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.AvaliacaoVO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AvaliacaoVOMapperTest {
    private final AvaliacaoVOMapper avaliacaoVOMapper = new AvaliacaoVOMapper();

    @Test
    void deveConverterDomainParaVO(){
        var domain = new AvaliacaoDomain(
                PontuacaoEnum.PONTO_1,
                "Comentario"
        );

        var vo = avaliacaoVOMapper.toVO(domain);

        assertEquals(domain.pontos(), vo.getPontos());
        assertEquals(domain.comentarios(), vo.getComentarios());
    }

    @Test
    void deveConverterVOParaDomain(){
        var vo = new AvaliacaoVO(
                PontuacaoEnum.PONTO_1,
                "Comentario"
        );

        var domain = avaliacaoVOMapper.toDomain(vo);

        assertEquals(vo.getPontos(), domain.pontos());
        assertEquals(vo.getComentarios(), domain.comentarios());
    }
}
