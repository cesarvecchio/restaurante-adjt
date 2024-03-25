package br.com.restauranteadjt.infrastructure.gateways;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.restauranteadjt.application.gateways.AvaliacaoGateway;
import br.com.restauranteadjt.domain.entity.AvaliacaoDomain;
import br.com.restauranteadjt.domain.enums.PontuacaoEnum;
import br.com.restauranteadjt.domain.enums.StatusMesa;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AvaliacaoRepositoryGatewayIT {

    @Autowired
    private AvaliacaoGateway avaliacaoGateway;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @BeforeEach
    void popularCollectionRestauranteEReserva() {
        List<RestauranteCollection> restaurantes = getRestauranteCollections();
        restauranteRepository.saveAll(restaurantes);

        List<ReservaCollection> reservas = getReservas();
        reservaRepository.saveAll(reservas);
    }

    private static List<RestauranteCollection> getRestauranteCollections() {
        AvaliacaoVO avaliacaoRodolfo = new AvaliacaoVO("65f4717bd6a804d8491361fd", PontuacaoEnum.PONTOS_4, "Muito bom!");

        return List.of(new RestauranteCollection("65efa722ed1aa9f4356dca85", "Burger King",
                "Av. Paulista", "Fast Food", List.of(LocalTime.parse("22:00:00")), 4, null),
            new RestauranteCollection("65f30ee66ef25c44b8d9faac", "Vivenda do Camarão",
                "Av. Raimundo Pereira de Magalhães, 1465", "Frutos do Mar", List.of(LocalTime.parse("12:00:00")), 10, List.of(avaliacaoRodolfo)),
            new RestauranteCollection("65f30f8bb665b3d1aa9aeee3", "Pizza Hut",
                "R. Carlos Weber, 344", "Pizzaria", List.of(LocalTime.parse("18:00:00")), 3, null)
        );
    }

    private static List<ReservaCollection> getReservas() {
        return List.of(
            new ReservaCollection("65f45ec843af698ea2473e29", LocalDate.now().plusDays(2),
                LocalTime.parse("22:00:00"), "João", "joao09@gmail.com", "11912345678",
                new RestauranteVO("65efa722ed1aa9f4356dca85", "Burger King"), StatusMesa.OCUPADA),
            new ReservaCollection("65f46030c68c5152198b3768", LocalDate.now().plusDays(3),
                LocalTime.parse("12:00:00"), "Kleber", "kleber12@gmail.com", "11956781234",
                new RestauranteVO("65f30ee66ef25c44b8d9faac", "Vivenda do Camarão"), StatusMesa.OCUPADA),
            new ReservaCollection("65f46226e747b50d9e531998", LocalDate.now().minusDays(2),
                LocalTime.parse("22:00:00"), "Ana", "ana@gmail.com", "11912349999",
                new RestauranteVO("65efa722ed1aa9f4356dca85", "Burger King"), StatusMesa.FINALIZADA),
            new ReservaCollection("65f461dc2cfffede8313b611", LocalDate.now().minusDays(3),
                LocalTime.parse("12:00:00"), "Márcia", "marcia@gmail.com", "11999995678",
                new RestauranteVO("65f30ee66ef25c44b8d9faac", "Vivenda do Camarão"), StatusMesa.FINALIZADA),
            new ReservaCollection("65f4717bd6a804d8491361fd", LocalDate.now().minusDays(4),
                LocalTime.parse("12:00:00"), "Rodolfo", "rodolfo@gmail.com", "11989895678",
                new RestauranteVO("65f30ee66ef25c44b8d9faac", "Vivenda do Camarão"), StatusMesa.FINALIZADA)
        );
    }

    @AfterEach
    void dropCollectionRestauranteEReserva() {
        restauranteRepository.deleteAll();
        reservaRepository.deleteAll();
    }

    @Nested
    class CriarAvaliacao {

        @Test
        void devePermitirCriarAvaliacaoRestaurante() {
            String idReserva = "65f461dc2cfffede8313b611";
            AvaliacaoDomain avaliacao = new AvaliacaoDomain(
                PontuacaoEnum.PONTOS_5,
                "Muito bom, comida maravilhosa!"
            );

            avaliacaoGateway.create(idReserva, avaliacao);

            Optional<ReservaCollection> reservaOptional = reservaRepository.findById(idReserva);
            Optional<RestauranteCollection> restauranteOptional = Optional.empty();
            RestauranteCollection restaurante = null;

            if (reservaOptional.isPresent()) {
                String idRestaurante = reservaOptional.get().getRestaurante().id();
                restauranteOptional = restauranteRepository.findById(idRestaurante);
            }
            if (restauranteOptional.isPresent()) {
                restaurante = restauranteOptional.get();
            }

            assertThat(restaurante)
                .isNotNull()
                .isInstanceOf(RestauranteCollection.class);
            assertThat(restaurante.getId())
                .isNotNull()
                .isEqualTo("65f30ee66ef25c44b8d9faac");
            assertThat(restaurante.getNome())
                .isNotNull()
                .isEqualTo("Vivenda do Camarão");
            assertThat(restaurante.getLocalizacao())
                .isNotNull()
                .isEqualTo("Av. Raimundo Pereira de Magalhães, 1465");
            assertThat(restaurante.getTipoCozinha())
                .isNotNull()
                .isEqualTo("Frutos do Mar");
            assertThat(restaurante.getHorariosFuncionamento())
                .hasSize(1);
            assertThat(restaurante.getHorariosFuncionamento().get(0))
                .isEqualTo(LocalTime.parse("12:00:00"));
            assertThat(restaurante.getCapacidade())
                .isNotNull()
                .isEqualTo(10);
            assertThat(restaurante.getAvaliacoes())
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .anySatisfy(aval -> {
                    assertThat(aval.getIdReserva())
                        .isEqualTo("65f461dc2cfffede8313b611");
                    assertThat(aval.getPontos())
                        .isEqualTo(PontuacaoEnum.PONTOS_5);
                    assertThat(aval.getComentarios())
                        .isEqualTo("Muito bom, comida maravilhosa!");
                });
        }

        @Test
        void deveGerarExcecao_Quando_CriarAvaliacaoRestaurante_IdReservaNaoExiste() {
            String idReserva = "65f45ec843af698ea2473x95";
            AvaliacaoDomain avaliacao = new AvaliacaoDomain(
                PontuacaoEnum.PONTOS_4,
                "Ok, bom pedido para quando está com pressa!"
            );

            assertThatThrownBy(() -> avaliacaoGateway.create(idReserva, avaliacao))
                .isInstanceOf(NaoEncontradoException.class)
                .hasMessage(String.format("Reserva com id:'%s' não foi encontrada", idReserva));
        }

        @Test
        void deveGerarExcecao_Quando_CriarAvaliacaoRestaurante_MesaNaoEstaComStatusFinalizada() {
            String idReserva = "65f45ec843af698ea2473e29";
            AvaliacaoDomain avaliacao = new AvaliacaoDomain(
                PontuacaoEnum.PONTOS_4,
                "Ok, bom pedido para quando está com pressa!"
            );

            assertThatThrownBy(() -> avaliacaoGateway.create(idReserva, avaliacao))
                .isInstanceOf(StatusReservaException.class)
                .hasMessage(String.format("A Reserva com id:'%s' possui o status:'%s', " +
                        "só é possivel avaliar o resturante quando o status estiver como:'%s'",
                    idReserva, StatusMesa.OCUPADA, StatusMesa.FINALIZADA)
                );
        }
    }

    @Nested
    class BuscarAvaliacoesRestaurante {

        @Test
        void devePermitirBuscarAvaliacoesRestaurante() {
            String idRestaurante = "65f30ee66ef25c44b8d9faac";
            List<AvaliacaoDomain> avaliacoes = avaliacaoGateway.listByIdRestaurante(idRestaurante);

            assertThat(avaliacoes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .anySatisfy(aval -> {
                    assertThat(aval.pontos())
                        .isEqualTo(PontuacaoEnum.PONTOS_4);
                    assertThat(aval.comentarios())
                        .isEqualTo("Muito bom!");
                });
        }

        @Test
        void deveGerarExcecao_Quando_BuscarAvaliacoesRestaurante_NaoExisteNenhumaAvaliacao() {
            String idRestaurante = "65efa722ed1aa9f4356dca85";

            assertThatThrownBy(() -> avaliacaoGateway.listByIdRestaurante(idRestaurante))
                .isInstanceOf(StatusReservaException.class)
                .hasMessage(String.format(
                    "O Restaurante com id:'%s' não possui nenhuma avaliação até o momento",
                    idRestaurante)
                );
        }
    }
}
