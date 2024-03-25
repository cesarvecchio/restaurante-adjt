package br.com.restauranteadjt.infrastructure.presenter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.restauranteadjt.domain.enums.PontuacaoEnum;
import br.com.restauranteadjt.infrastructure.controllers.dto.response.AvaliacaoResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;

class AvaliacaoPresenterTest {
    private final AvaliacaoPresenter avaliacaoPresenter = new AvaliacaoPresenter();

    @Test
    void deveRetornarResponseAvaliacao() {
        var avaliacao = gerarAvaliacaoResponse();
        var statusHttp = HttpStatusCode.valueOf(200);

        var response = avaliacaoPresenter.toResponseEntity(avaliacao, statusHttp);

        assertEquals(avaliacao, response.getBody());
        assertEquals(statusHttp, response.getStatusCode());
    }

    @Test
    void deveRetornarResponseAvaliacaoLista() {
        var avaliacao = List.of(gerarAvaliacaoResponse());
        var statusHttp = HttpStatusCode.valueOf(200);

        var response = avaliacaoPresenter.toResponseEntity(avaliacao, statusHttp);

        assertEquals(avaliacao, response.getBody());
        assertEquals(statusHttp, response.getStatusCode());
    }

    private AvaliacaoResponse gerarAvaliacaoResponse() {
        return new AvaliacaoResponse(
            PontuacaoEnum.PONTO_1,
            "Comentario"
        );
    }
}
