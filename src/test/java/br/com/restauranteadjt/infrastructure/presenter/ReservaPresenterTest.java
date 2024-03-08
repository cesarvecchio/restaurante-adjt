package br.com.restauranteadjt.infrastructure.presenter;

import br.com.restauranteadjt.infrastructure.controllers.dto.response.ReservaResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReservaPresenterTest {
    private final ReservaPresenter reservaPresenter = new ReservaPresenter();

    @Test
    void deveRestornarResponseReserva(){
        var reserva = gerarReserva();
        var status = HttpStatusCode.valueOf(200);

        var response = reservaPresenter.toResponseEntity(reserva, status);

        assertEquals(reserva, response.getBody());
        assertEquals(status, response.getStatusCode());
    }

    private ReservaResponse gerarReserva(){
        return new ReservaResponse(
                LocalDate.now(),
                LocalTime.now(),
                "Teste",
                "teste@teste.com",
                "42208922"
        );
    }
}
