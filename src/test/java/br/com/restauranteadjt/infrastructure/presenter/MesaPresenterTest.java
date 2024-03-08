package br.com.restauranteadjt.infrastructure.presenter;

import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.controllers.dto.MesaResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MesaPresenterTest {
    private final MesaPresenter mesaPresenter = new MesaPresenter();

    @Test
    void deveRetornarResponseMesa(){
        var mesa = gerarMesa();
        var status = HttpStatusCode.valueOf(200);

        var response = mesaPresenter.toResponseEntity(mesa, status);

        assertEquals(mesa, response.getBody());
        assertEquals(status, response.getStatusCode());
    }

    @Test
    void deveRetornarResponseMesaLista(){
        var mesa = List.of(gerarMesa());
        var status = HttpStatusCode.valueOf(200);

        var response = mesaPresenter.toResponseEntity(mesa, status);

        assertEquals(mesa, response.getBody());
        assertEquals(status, response.getStatusCode());
    }

    private MesaResponse gerarMesa(){
        return new MesaResponse(
                "1",
                "teste@teste.com",
                StatusMesa.OCUPADA
        );
    }
}
