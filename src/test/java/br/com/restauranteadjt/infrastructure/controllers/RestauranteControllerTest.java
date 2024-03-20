package br.com.restauranteadjt.infrastructure.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.restauranteadjt.application.usecases.RestauranteUseCase;
import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.infrastructure.controllers.dto.request.CreateRestauranteRequest;
import br.com.restauranteadjt.infrastructure.controllers.mapper.RestauranteDTOMapper;
import br.com.restauranteadjt.infrastructure.presenter.RestaurantePresenter;
import br.com.restauranteadjt.main.exception.ControllerExceptionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class RestauranteControllerTest {

    private MockMvc mockMvc;

    private final RestauranteDTOMapper restauranteDTOMapper = new RestauranteDTOMapper();

    private final RestaurantePresenter restaurantePresenter = new RestaurantePresenter();

    @Mock
    private RestauranteUseCase restauranteUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);

        RestauranteController restauranteController = new RestauranteController(restauranteUseCase, restauranteDTOMapper, restaurantePresenter);

        mockMvc = MockMvcBuilders.standaloneSetup(restauranteController)
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
    void deveCriarRestaurante() throws Exception {
        // Arrange
        CreateRestauranteRequest createRestauranteRequest = buildCreateRestauranteRequest();
        RestauranteDomain restauranteDomain = restauranteDTOMapper.toRestauranteDomain(createRestauranteRequest);

        when(restauranteUseCase.create(any(RestauranteDomain.class)))
            .thenReturn(restauranteDomain);

        // Act & Assert
        mockMvc.perform(post("/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(createRestauranteRequest))
            )
            .andExpect(status().isCreated());

        verify(restauranteUseCase, times(1)).create(any(RestauranteDomain.class));
    }

    @Test
    void deveListarRestaurantes() throws Exception {
        // Arrange
        CreateRestauranteRequest createRestauranteRequest = buildCreateRestauranteRequest();
        RestauranteDomain restauranteDomain = restauranteDTOMapper.toRestauranteDomain(createRestauranteRequest);
        List<RestauranteDomain> resaurantes = List.of(restauranteDomain);

        when(restauranteUseCase.findByNomeOrTipoCozinhaOrLocalizacao(any(), any(), any()))
            .thenReturn(resaurantes);

        // Act & Assert
        mockMvc.perform(get("/restaurantes")
                .param("nome", "teste")
                .param("tipoCozinha", "teste")
                .param("endereco", "teste"))
            .andExpect(status().isOk());

        verify(restauranteUseCase, times(1)).findByNomeOrTipoCozinhaOrLocalizacao(any(), any(), any());
    }

    public static String asJsonString(Object object) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().writeValueAsString(object);
    }

    private CreateRestauranteRequest buildCreateRestauranteRequest() {
        return new CreateRestauranteRequest(
            "Teste",
            "Sao Paulo",
            "Teste",
            List.of(LocalTime.of(12, 0)),
            10);
    }
}
