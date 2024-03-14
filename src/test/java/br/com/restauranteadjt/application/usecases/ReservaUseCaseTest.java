package br.com.restauranteadjt.application.usecases;

import br.com.restauranteadjt.application.gateways.ReservaGateway;
import br.com.restauranteadjt.domain.entity.ReservaDomain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReservaUseCaseTest {
    @Mock
    private ReservaGateway reservaGateway;

    private ReservaUseCase reservaUseCase;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        reservaUseCase = new ReservaUseCase(reservaGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void deveCriarReserva(){
        var idReserva = "65f252a447277444c60898ae";
        var reservaDomain =  new ReservaDomain(
                LocalDate.now().plusDays(1L),
                LocalTime.of(20, 0, 0, 0),
                "Sérgio",
                "sergio@gmail.com",
                "40028922"
        );

        when(reservaGateway.create(idReserva, reservaDomain)).thenReturn(reservaDomain);

        var reservaDomainResposta = reservaUseCase.create(idReserva, reservaDomain);

        assertEquals(reservaDomain, reservaDomainResposta);

        verify(reservaGateway, times(1)).create(idReserva, reservaDomain);
    }
}
