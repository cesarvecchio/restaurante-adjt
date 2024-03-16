package br.com.restauranteadjt.infrastructure.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.restauranteadjt.application.usecases.AvaliacaoUseCase;
import br.com.restauranteadjt.domain.entity.AvaliacaoDomain;
import br.com.restauranteadjt.domain.enums.PontuacaoEnum;
import br.com.restauranteadjt.infrastructure.controllers.mapper.AvaliacaoDTOMapper;
import br.com.restauranteadjt.infrastructure.presenter.AvaliacaoPresenter;
import br.com.restauranteadjt.main.exception.ControllerExceptionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class AvaliacaoControllerTest {

    private MockMvc mockMvc;

    private final AvaliacaoDTOMapper avaliacaoDTOMapper = new AvaliacaoDTOMapper();

    private final AvaliacaoPresenter avaliacaoPresenter = new AvaliacaoPresenter();

    @Mock
    private AvaliacaoUseCase avaliacaoUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);

        AvaliacaoController avaliacaoController = new AvaliacaoController(avaliacaoDTOMapper, avaliacaoUseCase, avaliacaoPresenter);

        mockMvc = MockMvcBuilders.standaloneSetup(avaliacaoController)
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
    void deveCriarAvaliacao() throws Exception {
        // Arrange
        String idReserva = "1";
        var avaliacaoDomain = buildAvaliacaoDomain();

        when(avaliacaoUseCase.create(any(), any(AvaliacaoDomain.class)))
            .thenReturn(avaliacaoDomain);

        // Act & Assert
        mockMvc.perform(post("/avaliacoes/{idReserva}", idReserva)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(avaliacaoDomain))
            )
            .andExpect(status().isCreated())
            .andExpect(result -> {
                String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                assertThat(json).contains("Comida muito boa");
            });
    }

    @Test
    void deveListarAvaliacaoPorRestaurante() throws Exception {
        // Arrange
        String idRestaurante = "1";
        var avaliacaoDomain = buildAvaliacaoDomain();
        List<AvaliacaoDomain> avalicacoes = List.of(avaliacaoDomain);

        when(avaliacaoUseCase.listByIdRestaurante(any()))
            .thenReturn(avalicacoes);

        // Act & Assert

        mockMvc.perform(get("/avaliacoes/{idRestaurante}", idRestaurante)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(avaliacaoUseCase, times(1)).listByIdRestaurante(idRestaurante);
    }

    public static String asJsonString(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    private AvaliacaoDomain buildAvaliacaoDomain() {
        return new AvaliacaoDomain(PontuacaoEnum.PONTOS_3, "Comida muito boa");
    }
}
