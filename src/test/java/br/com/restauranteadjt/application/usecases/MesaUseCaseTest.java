package br.com.restauranteadjt.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.restauranteadjt.application.gateways.MesaGateway;
import br.com.restauranteadjt.domain.entity.MesaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MesaUseCaseTest {
    @Mock
    private MesaGateway mesaGateway;

    private MesaUseCase mesaUseCase;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        mesaUseCase = new MesaUseCase(mesaGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Nested
    class ListarMesasPorFiltros {
        @Test
        void deveListarMesasPorFiltros() {
            var idRestaurante = "65f252a447277444c60898ae";
            var dataReserva = LocalDate.of(2024, 3, 20);
            var horaReserva = LocalTime.of(20, 0, 0, 0);
            var statusMesa = StatusMesa.OCUPADA;

            var mesaDomainLista = List.of(new MesaDomain(
                idRestaurante,
                "teste@teste.com",
                StatusMesa.OCUPADA
            ));

            when(mesaGateway.listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
                idRestaurante, dataReserva, horaReserva, statusMesa))
                .thenReturn(mesaDomainLista);

            var mesaListaObtida = mesaUseCase.listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
                idRestaurante, dataReserva, horaReserva, statusMesa);

            assertEquals(mesaDomainLista, mesaListaObtida);

            verify(mesaGateway, times(1))
                .listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
                    idRestaurante, dataReserva, horaReserva, statusMesa
                );
        }
    }

    @Nested
    class AtualizaStatusReserva {
        @Test
        void deveAtualizaStatusReserva() {
            var idReserva = "65f252a447277444c60898ae";
            var status = StatusMesa.FINALIZADA;
            var mesaDomain = new MesaDomain(
                idReserva,
                "teste@teste.com",
                status
            );

            when(mesaGateway.update(idReserva, status))
                .thenReturn(mesaDomain);

            var mesaObtida = mesaUseCase.update(idReserva, status);

            assertEquals(mesaDomain, mesaObtida);

            verify(mesaGateway, times(1)).update(idReserva, status);
        }
    }
}
