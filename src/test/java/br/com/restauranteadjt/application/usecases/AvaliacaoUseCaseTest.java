package br.com.restauranteadjt.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.restauranteadjt.application.gateways.AvaliacaoGateway;
import br.com.restauranteadjt.domain.entity.AvaliacaoDomain;
import br.com.restauranteadjt.domain.enums.PontuacaoEnum;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AvaliacaoUseCaseTest {
    @Mock
    private AvaliacaoGateway avaliacaoGateway;

    private AvaliacaoUseCase avaliacaoUseCase;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        avaliacaoUseCase = new AvaliacaoUseCase(avaliacaoGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void deveCriarAvaliacao() {
        var idReserva = "65f252a447277444c60898ae";
        var avaliacaoDomain = new AvaliacaoDomain(
            PontuacaoEnum.PONTOS_5,
            "Comentario"
        );

        when(avaliacaoGateway.create(idReserva, avaliacaoDomain)).thenReturn(avaliacaoDomain);

        var avaliacaoDomainResposta = avaliacaoUseCase.create(idReserva, avaliacaoDomain);

        assertEquals(avaliacaoDomain, avaliacaoDomainResposta);

        verify(avaliacaoGateway, times(1)).create(idReserva, avaliacaoDomain);
    }

    @Test
    void deveListByIdRestaurante() {
        var idRestaurante = "65f252a447277444c60898ae";
        var avaliacaoDomainList = List.of(new AvaliacaoDomain(
            PontuacaoEnum.PONTOS_5,
            "Comentario"
        ));

        when(avaliacaoGateway.listByIdRestaurante(idRestaurante)).thenReturn(avaliacaoDomainList);

        var avaliacaoDomainResposta = avaliacaoUseCase.listByIdRestaurante(idRestaurante);

        assertEquals(avaliacaoDomainList, avaliacaoDomainResposta);

        verify(avaliacaoGateway, times(1)).listByIdRestaurante(idRestaurante);
    }
}
