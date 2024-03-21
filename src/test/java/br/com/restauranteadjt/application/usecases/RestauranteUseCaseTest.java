package br.com.restauranteadjt.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.restauranteadjt.application.gateways.RestauranteGateway;
import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RestauranteUseCaseTest {
    @Mock
    private RestauranteGateway restauranteGateway;

    private RestauranteUseCase restauranteUseCase;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        restauranteUseCase = new RestauranteUseCase(restauranteGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void deveCriarRestaurante() {
        var restauranteDomain = new RestauranteDomain(
            "Teste",
            "TipoLocalizacao",
            "localizacao",
            List.of(LocalTime.of(20, 0, 0, 0)),
            2
        );
        restauranteDomain.setId("65f252a447277444c60898ae");

        when(restauranteGateway.create(restauranteDomain)).thenReturn(restauranteDomain);

        var resposta = restauranteUseCase.create(restauranteDomain);

        assertEquals(restauranteDomain, resposta);

        verify(restauranteGateway, times(1)).create(restauranteDomain);
    }

    @Test
    void deveListarPorFiltro() {
        var nome = "Teste";
        var tipoCozinha = "TipoLocalizacao";
        var localizacao = "localizacao";
        var restauranteDomainLista = List.of(new RestauranteDomain(
            nome,
            tipoCozinha,
            localizacao,
            List.of(LocalTime.of(20, 0, 0, 0)),
            2
        ));

        when(restauranteGateway.findByNomeOrTipoCozinhaOrLocalizacao(nome, tipoCozinha, localizacao))
            .thenReturn(restauranteDomainLista);

        var resposta = restauranteUseCase.findByNomeOrTipoCozinhaOrLocalizacao(nome, tipoCozinha, localizacao);

        assertEquals(restauranteDomainLista, resposta);

        verify(restauranteGateway, times(1))
            .findByNomeOrTipoCozinhaOrLocalizacao(nome, tipoCozinha, localizacao);
    }

    @Test
    void deveEncontrarPorId() {
        var id = "65f252a447277444c60898ae";
        var restauranteDomain = new RestauranteDomain();
        restauranteDomain.setId("65f252a447277444c60898ae");

        when(restauranteGateway.findById(id)).thenReturn(restauranteDomain);

        var resposta = restauranteUseCase.findById(id);

        assertEquals(restauranteDomain, resposta);

        verify(restauranteGateway, times(1)).findById(id);
    }
}
