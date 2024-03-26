package br.com.restauranteadjt.infrastructure.presenter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.restauranteadjt.infrastructure.controllers.dto.response.RestauranteResponse;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;

class RestaurantePresenterTest {
    private final RestaurantePresenter restaurantePresenter = new RestaurantePresenter();

    @Test
    void deveGerarResponseRestaurante() {
        var restaurante = gerarRestaurante();
        var status = HttpStatusCode.valueOf(200);

        var response = restaurantePresenter.toResponseEntity(restaurante, status);

        assertEquals(restaurante, response.getBody());
        assertEquals(status, response.getStatusCode());
    }

    @Test
    void deveGerarResponseRestauranteLista() {
        var restaurante = List.of(gerarRestaurante());
        var status = HttpStatusCode.valueOf(200);

        var response = restaurantePresenter.toResponseEntity(restaurante, status);

        assertEquals(restaurante, response.getBody());
        assertEquals(status, response.getStatusCode());
    }

    private RestauranteResponse gerarRestaurante() {
        return new RestauranteResponse(
            "1",
            "Teste",
            "Localizacao",
            "TIpo cozinha",
            List.of(LocalTime.now()),
            2
        );
    }

}
