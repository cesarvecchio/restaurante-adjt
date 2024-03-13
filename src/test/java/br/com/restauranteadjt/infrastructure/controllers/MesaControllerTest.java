package br.com.restauranteadjt.infrastructure.controllers;

import br.com.restauranteadjt.application.usecases.MesaUseCase;
import br.com.restauranteadjt.domain.entity.MesaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.controllers.dto.request.StatusMesaRequest;
import br.com.restauranteadjt.infrastructure.controllers.mapper.MesaDTOMapper;
import br.com.restauranteadjt.infrastructure.presenter.MesaPresenter;
import br.com.restauranteadjt.main.exception.ControllerExceptionHandler;
import br.com.restauranteadjt.main.exception.NaoEncontradoException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MesaControllerTest {
    MockMvc mockMvc;

    @Mock
    private MesaUseCase mesaUseCase;
    @Mock
    private MesaDTOMapper mesaDTOMapper;
    private final MesaPresenter mesaPresenter = new MesaPresenter();

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);

        MesaController mesaController = new MesaController(mesaUseCase, mesaDTOMapper, mesaPresenter);

        mockMvc = MockMvcBuilders.standaloneSetup(mesaController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();

    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class ListarMesasPorFiltros {
        @Test
        void deveListarMesasPorFiltros() throws Exception {
            var idRestaurante = ObjectId.get().toString();
            var dataReserva = LocalDate.of(2024, 4, 1);
            var horaReserva = LocalTime.of(20, 0, 0, 0);
            var statusMesa = StatusMesa.OCUPADA;

            var mesaDomain = buildMesaDomain();

            when(mesaUseCase.listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
                    idRestaurante, dataReserva, horaReserva, statusMesa
            )).thenReturn(List.of(mesaDomain));

            mockMvc.perform(get("/mesas/{idRestaurante}/{dataReserva}/{horaReserva}", idRestaurante,
                    dataReserva, horaReserva)
                    .param("statusMesa", String.valueOf(statusMesa))
            ).andExpect(status().isOk());

            verify(mesaUseCase, times(1))
                    .listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
                            any(String.class), any(LocalDate.class), any(LocalTime.class), any(StatusMesa.class));
        }

        @Test
        void deveListarMesasPorFiltros_SemStatus() throws Exception {
            var idRestaurante = ObjectId.get().toString();
            var dataReserva = LocalDate.of(2024, 4, 1);
            var horaReserva = LocalTime.of(20, 0, 0, 0);

            var mesaDomain = buildMesaDomain();

            when(mesaUseCase.listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
                    idRestaurante, dataReserva, horaReserva, null
            )).thenReturn(List.of(mesaDomain));

            mockMvc.perform(get("/mesas/{idRestaurante}/{dataReserva}/{horaReserva}", idRestaurante,
                    dataReserva, horaReserva)
            ).andExpect(status().isOk());

            verify(mesaUseCase, times(1))
                    .listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
                            any(String.class), any(LocalDate.class), any(LocalTime.class), any());
        }

        @Test
        void deveGerarExcecao_QuandoListarMesasPorFiltros_RestauranteNaoEncontrado() throws Exception {
            var idRestaurante = ObjectId.get().toString();
            var dataReserva = LocalDate.of(2024, 4, 1);
            var horaReserva = LocalTime.of(20, 0, 0, 0);
            var statusMesa = StatusMesa.OCUPADA;

            var mesaDomain = buildMesaDomain();

            when(mesaUseCase.listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
                    idRestaurante, dataReserva, horaReserva, statusMesa
            )).thenThrow(NaoEncontradoException.class);

            mockMvc.perform(get("/mesas/{idRestaurante}/{dataReserva}/{horaReserva}", idRestaurante,
                    dataReserva, horaReserva)
                    .param("statusMesa", String.valueOf(statusMesa)))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> {
                        String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                        assertThat(json).contains("Nao Encontrado Exception");
                    });

            verify(mesaUseCase, times(1))
                    .listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
                            any(String.class), any(LocalDate.class), any(LocalTime.class), any(StatusMesa.class));
        }
    }

    @Nested
    class AtualizaStatusReserva {
        @Test
        void deveAtualizaStatusReserva() throws Exception {
            var idRestaurante = ObjectId.get().toString();
            var statusMesa = new StatusMesaRequest(StatusMesa.FINALIZADA);

            var mesaDomain = buildMesaDomain(statusMesa.getStatusMesa());

            when(mesaUseCase.update(idRestaurante, statusMesa.getStatusMesa())).thenReturn(mesaDomain);

            mockMvc.perform(put("/mesas/{idRestaurante}", idRestaurante)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(statusMesa)))
                    .andExpect(status().isAccepted());

            verify(mesaUseCase, times(1)).update(idRestaurante, statusMesa.getStatusMesa());
        }
    }

    private MesaDomain buildMesaDomain(){
        return new MesaDomain(
                ObjectId.get().toString(),
                "teste@teste.com",
                StatusMesa.OCUPADA
        );
    }

    private MesaDomain buildMesaDomain(StatusMesa statusMesa){
        return new MesaDomain(
                ObjectId.get().toString(),
                "teste@teste.com",
                statusMesa
        );
    }

    public static String asJsonString(final Object object) throws JsonProcessingException {
            return new ObjectMapper().writeValueAsString(object);
    }
}
