package br.com.restauranteadjt.infrastructure.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.restauranteadjt.application.usecases.ReservaUseCase;
import br.com.restauranteadjt.domain.entity.ReservaDomain;
import br.com.restauranteadjt.infrastructure.controllers.dto.request.CreateReservaRequest;
import br.com.restauranteadjt.infrastructure.controllers.mapper.ReservaDTOMapper;
import br.com.restauranteadjt.infrastructure.presenter.ReservaPresenter;
import br.com.restauranteadjt.main.exception.ControllerExceptionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ReservaControllerTest {

    private MockMvc mockMvc;

    private final ReservaDTOMapper reservaDTOMapper = new ReservaDTOMapper();

    private final ReservaPresenter reservaPresenter = new ReservaPresenter();

    @Mock
    private ReservaUseCase reservaUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);

        ReservaController reservaController = new ReservaController(reservaDTOMapper, reservaUseCase, reservaPresenter);

        mockMvc = MockMvcBuilders.standaloneSetup(reservaController)
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

    @Test
    void deveCriarReserva() throws Exception {
        // Arrange
        String idReserva = "1";
        CreateReservaRequest createReservaRequest = buildCreateReservaRequest();
        ReservaDomain reservaDomain = reservaDTOMapper.toDomain(createReservaRequest);

        when(reservaUseCase.create(any(), any(ReservaDomain.class)))
            .thenReturn(reservaDomain);

        // Act & Assert
        mockMvc.perform(post("/reservas/{idRestaurante}", idReserva)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(createReservaRequest))
            )
            .andExpect(status().isCreated());

        verify(reservaUseCase, times(1)).create(any(), any(ReservaDomain.class));

    }

    public static String asJsonString(Object object) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().writeValueAsString(object);
    }

    private CreateReservaRequest buildCreateReservaRequest() {
        return new CreateReservaRequest(
            LocalDate.of(2021, 10, 10),
            LocalTime.of(12, 0),
            "Jose",
            "jose@hotmail.com",
            "123456789");
    }
}
