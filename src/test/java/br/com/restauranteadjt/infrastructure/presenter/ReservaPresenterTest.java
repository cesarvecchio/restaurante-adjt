package br.com.restauranteadjt.infrastructure.presenter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.restauranteadjt.infrastructure.controllers.dto.response.ReservaResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;

class ReservaPresenterTest {
    private final ReservaPresenter reservaPresenter = new ReservaPresenter();

    @Test
    void deveRestornarResponseReserva() {
        var reserva = gerarReserva();
        var status = HttpStatusCode.valueOf(200);

        var response = reservaPresenter.toResponseEntity(reserva, status);

        assertEquals(reserva, response.getBody());
        assertEquals(status, response.getStatusCode());
    }

    private ReservaResponse gerarReserva() {
        return new ReservaResponse(
                "6600a190665effa378d304eb",
                LocalDate.now(),
                LocalTime.now(),
                "Teste",
                "teste@teste.com",
                "42208922"
        );
    }
}
