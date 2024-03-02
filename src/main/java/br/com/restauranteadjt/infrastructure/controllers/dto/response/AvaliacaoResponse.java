package br.com.restauranteadjt.infrastructure.controllers.dto.response;

import br.com.restauranteadjt.domain.enums.PontuacaoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvaliacaoResponse {
    private PontuacaoEnum pontuacao;
    private String comentario;
}
