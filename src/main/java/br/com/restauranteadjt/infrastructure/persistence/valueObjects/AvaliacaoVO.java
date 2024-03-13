package br.com.restauranteadjt.infrastructure.persistence.valueObjects;

import br.com.restauranteadjt.domain.enums.PontuacaoEnum;
import lombok.Data;

@Data
public class AvaliacaoVO {
    private String idReserva;
    private PontuacaoEnum pontos;
    private String comentarios;

    public AvaliacaoVO(PontuacaoEnum pontos, String comentarios) {
        this.pontos = pontos;
        this.comentarios = comentarios;
    }
}
