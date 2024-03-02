package br.com.restauranteadjt.infrastructure.controllers.dto.request;

import br.com.restauranteadjt.domain.enums.PontuacaoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvaliacaoRequest {
    private PontuacaoEnum pontuacao;
    private String comentario;
}
