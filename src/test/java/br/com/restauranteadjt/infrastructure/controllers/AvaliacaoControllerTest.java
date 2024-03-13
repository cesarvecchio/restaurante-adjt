package br.com.restauranteadjt.infrastructure.controllers;

import br.com.restauranteadjt.application.usecases.AvaliacaoUseCase;
import br.com.restauranteadjt.infrastructure.controllers.mapper.AvaliacaoDTOMapper;
import br.com.restauranteadjt.infrastructure.presenter.AvaliacaoPresenter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.fail;

public class AvaliacaoControllerTest {
    MockMvc mockMvc;

    private final AvaliacaoDTOMapper avaliacaoDTOMapper = new AvaliacaoDTOMapper();
    @Mock
    private AvaliacaoUseCase avaliacaoUseCase;
    private final AvaliacaoPresenter avaliacaoPresenter = new AvaliacaoPresenter();

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);

        AvaliacaoController avaliacaoController = new AvaliacaoController(avaliacaoDTOMapper, avaliacaoUseCase,
                avaliacaoPresenter);

        mockMvc = MockMvcBuilders.standaloneSetup(avaliacaoController).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class Create {
        @Test
        void create() {
            fail("Não implementado");
        }
    }
}
