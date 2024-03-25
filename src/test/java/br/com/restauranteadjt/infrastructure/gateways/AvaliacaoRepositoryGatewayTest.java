package br.com.restauranteadjt.infrastructure.gateways;

import static br.com.restauranteadjt.domain.enums.StatusMesa.FINALIZADA;
import static br.com.restauranteadjt.domain.enums.StatusMesa.LIVRE;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.restauranteadjt.application.gateways.AvaliacaoGateway;
import br.com.restauranteadjt.domain.entity.AvaliacaoDomain;
import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.domain.enums.PontuacaoEnum;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.gateways.mapper.AvaliacaoVOMapper;
import br.com.restauranteadjt.infrastructure.gateways.mapper.RestauranteCollectionMapper;
import br.com.restauranteadjt.infrastructure.persistence.collection.ReservaCollection;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.ReservaRepository;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.AvaliacaoVO;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.RestauranteVO;
import br.com.restauranteadjt.main.exception.NaoEncontradoException;
import br.com.restauranteadjt.main.exception.StatusReservaException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AvaliacaoRepositoryGatewayTest {

    private final AvaliacaoVOMapper avaliacaoVOMapper = new AvaliacaoVOMapper();
    private final RestauranteCollectionMapper restauranteCollectionMapper = new RestauranteCollectionMapper();

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    private AvaliacaoGateway avaliacaoGateway;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        avaliacaoGateway = new AvaliacaoRepositoryGateway(restauranteRepository, reservaRepository, avaliacaoVOMapper);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Nested
    class CriarAvaliacao {
        @Test
        void deveGerarExcecao_NaoEncontrado_QuandoNaoEncontrarReserva() {
            // Arrange
            String idReserva = "1";
            AvaliacaoDomain avaliacaoDomain = buildAvaliacaoDomain();

            when(reservaRepository.findById(idReserva)).thenReturn(empty());

            // Act
            assertThatThrownBy(() -> avaliacaoGateway.create(idReserva, avaliacaoDomain))
                .isInstanceOf(NaoEncontradoException.class)
                .hasMessage(String.format("Reserva com id:'%s' não foi encontrada", idReserva));

            // Assert
            verify(reservaRepository, times(1)).findById(idReserva);
        }

        @Test
        void deveGerarExcecao_StatusReserva_QuandoStatusDiferenteDeFinalizada() {
            // Arrange
            String idReserva = "1";
            AvaliacaoDomain avaliacaoDomain = buildAvaliacaoDomain();

            LocalDate dataReserva = LocalDate.of(2024, 3, 20);
            LocalTime horaReserva = LocalTime.of(20, 0, 0, 0);
            ReservaCollection reservaCollection = buildReserva(dataReserva, horaReserva, LIVRE);

            when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reservaCollection));

            // Act
            assertThatThrownBy(() -> avaliacaoGateway.create(idReserva, avaliacaoDomain))
                .isInstanceOf(StatusReservaException.class)
                .hasMessage(String.format(
                    "A Reserva com id:'%s' possui o status:'%s', só é possivel avaliar o resturante quando o status estiver como:'%s'", idReserva, LIVRE, FINALIZADA)
                );

            // Assert
            verify(reservaRepository, times(1)).findById(idReserva);
        }

        @Test
        void deveGerarExcecao_NaoEncontrado_QuandoNaoEncontrarRestaurante() {
            // Arrange
            String idReserva = "1";
            AvaliacaoDomain avaliacaoDomain = buildAvaliacaoDomain();

            LocalDate dataReserva = LocalDate.of(2024, 3, 20);
            LocalTime horaReserva = LocalTime.of(20, 0, 0, 0);
            ReservaCollection reservaCollection = buildReserva(dataReserva, horaReserva, FINALIZADA);

            when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reservaCollection));
            when(restauranteRepository.findById(idReserva)).thenReturn(empty());

            // Act
            assertThatThrownBy(() -> avaliacaoGateway.create(idReserva, avaliacaoDomain))
                .isInstanceOf(NaoEncontradoException.class)
                .hasMessage(String.format(
                    "Restaurante com id:'%s' não foi encontrado", reservaCollection.getRestaurante().id())
                );

            // Assert
            verify(reservaRepository, times(1)).findById(idReserva);
            verify(restauranteRepository, times(1)).findById(idReserva);
        }

        @Test
        void deveCriarAvaliacao_QuandoRestauranteJaTemAvaliacoes() {
            // Arrange
            String idReserva = "1";
            AvaliacaoDomain avaliacaoDomain = buildAvaliacaoDomain();
            RestauranteDomain restauranteDomain = buildRestauranteDomain();
            RestauranteCollection restauranteCollection = restauranteCollectionMapper.toCollection(restauranteDomain);
            restauranteCollection.setAvaliacoes(List.of(new AvaliacaoVO(PontuacaoEnum.PONTOS_2, "Mais ou menos")));

            LocalDate dataReserva = LocalDate.of(2024, 3, 20);
            LocalTime horaReserva = LocalTime.of(20, 0, 0, 0);
            ReservaCollection reservaCollection = buildReserva(dataReserva, horaReserva, FINALIZADA);

            when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reservaCollection));
            when(restauranteRepository.findById(idReserva)).thenReturn(Optional.of(restauranteCollection));
            when(restauranteRepository.save(restauranteCollection)).thenReturn(restauranteCollection);

            // Act
            AvaliacaoDomain avaliacao = avaliacaoGateway.create(idReserva, avaliacaoDomain);

            // Assert
            verify(reservaRepository, times(1)).findById(idReserva);
            verify(restauranteRepository, times(1)).findById(idReserva);
            verify(restauranteRepository, times(1)).save(restauranteCollection);
            assertNotNull(avaliacao);
            assertEquals("Comida muito boa", avaliacao.comentarios());
            assertEquals(PontuacaoEnum.PONTOS_3, avaliacao.pontos());

        }

        @Test
        void deveCriarAvaliacao_QuandoResuranteNaoTemAvaliacoes() {
            // Arrange
            String idReserva = "1";
            AvaliacaoDomain avaliacaoDomain = buildAvaliacaoDomain();
            RestauranteDomain restauranteDomain = buildRestauranteDomain();
            RestauranteCollection restauranteCollection = restauranteCollectionMapper.toCollection(restauranteDomain);

            LocalDate dataReserva = LocalDate.of(2024, 3, 20);
            LocalTime horaReserva = LocalTime.of(20, 0, 0, 0);
            ReservaCollection reservaCollection = buildReserva(dataReserva, horaReserva, FINALIZADA);

            when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reservaCollection));
            when(restauranteRepository.findById(idReserva)).thenReturn(Optional.of(restauranteCollection));
            when(restauranteRepository.save(restauranteCollection)).thenReturn(restauranteCollection);

            // Act
            AvaliacaoDomain avaliacao = avaliacaoGateway.create(idReserva, avaliacaoDomain);

            // Assert
            verify(reservaRepository, times(1)).findById(idReserva);
            verify(restauranteRepository, times(1)).findById(idReserva);
            verify(restauranteRepository, times(1)).save(restauranteCollection);
            assertNotNull(avaliacao);
            assertEquals("Comida muito boa", avaliacao.comentarios());
            assertEquals(PontuacaoEnum.PONTOS_3, avaliacao.pontos());
        }
    }

    @Nested
    class ListarAvaliacoesPorIdRestaurante {
        @Test
        void deveGerarExcecao_StatusReserva_QuandoRestauranteNaoTemAvaliacoes() {
            //Arrange
            String idRestaurante = "1";
            RestauranteDomain restauranteDomain = buildRestauranteDomain();
            RestauranteCollection restauranteCollection = restauranteCollectionMapper.toCollection(restauranteDomain);

            when(restauranteRepository.findById(idRestaurante)).thenReturn(Optional.of(restauranteCollection));

            //Act
            assertThatThrownBy(() -> avaliacaoGateway.listByIdRestaurante(idRestaurante))
                .isInstanceOf(StatusReservaException.class)
                .hasMessage(String.format(
                    "O Restaurante com id:'%s' não possui nenhuma avaliação até o momento", idRestaurante)
                );

            //Assert
            verify(restauranteRepository, times(1)).findById(idRestaurante);
        }

        @Test
        void deveListarAvaliacoesRestaurante() {
            //Arrange
            String idRestaurante = "1";
            RestauranteDomain restauranteDomain = buildRestauranteDomain();
            RestauranteCollection restauranteCollection = restauranteCollectionMapper.toCollection(restauranteDomain);
            restauranteCollection.setAvaliacoes(List.of(new AvaliacaoVO(PontuacaoEnum.PONTOS_2, "Mais ou menos")));

            when(restauranteRepository.findById(idRestaurante)).thenReturn(Optional.of(restauranteCollection));
            when(restauranteRepository.findById(idRestaurante)).thenReturn(Optional.of(restauranteCollection));

            //Act
            List<AvaliacaoDomain> avaliacoes = avaliacaoGateway.listByIdRestaurante(idRestaurante);

            //Assert
            verify(restauranteRepository, times(1)).findById(idRestaurante);
            assertEquals(1, avaliacoes.size());
        }
    }

    private AvaliacaoDomain buildAvaliacaoDomain() {
        return new AvaliacaoDomain(PontuacaoEnum.PONTOS_3, "Comida muito boa");
    }

    private ReservaCollection buildReserva(LocalDate dataReserva, LocalTime horaReserva, StatusMesa statusMesa) {
        ReservaCollection reservaCollection = new ReservaCollection(
            dataReserva,
            horaReserva,
            "Teste",
            "teste@teste.com",
            "22002200"
        );
        reservaCollection.setId("1");
        reservaCollection.setStatusMesa(statusMesa);
        reservaCollection.setRestaurante(new RestauranteVO("1", "Teste"));

        return reservaCollection;
    }

    private RestauranteDomain buildRestauranteDomain() {
        return new RestauranteDomain(
            "Teste",
            "Localizacao",
            "tipoCozinha",
            List.of(LocalTime.of(10, 0, 0, 0)),
            2
        );
    }

}
