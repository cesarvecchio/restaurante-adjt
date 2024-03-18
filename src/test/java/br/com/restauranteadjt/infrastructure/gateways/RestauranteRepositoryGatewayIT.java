package br.com.restauranteadjt.infrastructure.gateways;

import br.com.restauranteadjt.application.gateways.RestauranteGateway;
import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.infrastructure.gateways.mapper.RestauranteCollectionMapper;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.main.exception.CadastradoException;
import br.com.restauranteadjt.main.exception.NaoEncontradoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
class RestauranteRepositoryGatewayIT {

    @Autowired
    private RestauranteGateway restauranteGateway;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @BeforeEach
    void popularCollectionRestaurante() {
        List<RestauranteCollection> restaurantes = List.of(new RestauranteCollection("65efa722ed1aa9f4356dca85", "Burger King",
                        "Av. Paulista", "Fast Food", List.of(LocalTime.parse("22:00:00")), 4, null),
                new RestauranteCollection("65f30ee66ef25c44b8d9faac", "Vivenda do Camarão",
                        "Av. Raimundo Pereira de Magalhães, 1465", "Frutos do Mar", List.of(LocalTime.parse("12:00:00")), 10, null),
                new RestauranteCollection("65f30f8bb665b3d1aa9aeee3", "Pizza Hut",
                        "R. Carlos Weber, 344", "Pizzaria", List.of(LocalTime.parse("18:00:00")), 3, null)
        );
        restauranteRepository.saveAll(restaurantes);
    }

    @AfterEach
    void dropCollectionRestaurante() {
        restauranteRepository.deleteAll();
    }

    @Nested
    class CriarRestaurante {
        @Test
        void devePermitirCriarRestaurante() {
            RestauranteDomain restauranteDomain = new RestauranteDomain("65f315ae9f5b0f8ff3304c55", "Hi Pokee", "R. Augusta, 2068", "Comida Havaiana", List.of(LocalTime.now()), 10);
            RestauranteDomain restauranteSalvo = restauranteGateway.create(restauranteDomain);

            assertThat(restauranteSalvo)
                    .isNotNull()
                    .isInstanceOf(RestauranteDomain.class);
            assertThat(restauranteSalvo.getId())
                    .isNotNull();
            assertThat(restauranteSalvo.getNome())
                    .isNotNull()
                    .isEqualTo(restauranteDomain.getNome());
            assertThat(restauranteSalvo.getLocalizacao())
                    .isNotNull()
                    .isEqualTo(restauranteDomain.getLocalizacao());
            assertThat(restauranteSalvo.getTipoCozinha())
                    .isNotNull()
                    .isEqualTo(restauranteDomain.getTipoCozinha());
            assertThat(restauranteSalvo.getHorariosFuncionamento())
                    .hasSize(1);
            assertThat(restauranteSalvo.getHorariosFuncionamento().get(0))
                    .isEqualTo(restauranteDomain.getHorariosFuncionamento().get(0));
            assertThat(restauranteSalvo.getCapacidade())
                    .isNotNull()
                    .isEqualTo(restauranteDomain.getCapacidade());
        }

        @Test
        void deveGerarExcecao_Quando_CriarRestaurante_Nome_TipoCozinha_Localizacao_JaExiste() {
            RestauranteDomain restauranteDomain = new RestauranteDomain("65efa722ed1aa9f4356dca85", "Burger King",
                    "Av. Paulista", "Fast Food", List.of(LocalTime.parse("22:00:00")), 4);
            String nome = restauranteDomain.getNome();
            String tipoCozinha = restauranteDomain.getTipoCozinha();
            String localizacao = restauranteDomain.getLocalizacao();

            assertThatThrownBy(() -> restauranteGateway.create(restauranteDomain))
                    .isInstanceOf(CadastradoException.class)
                    .hasMessage(String.format(
                            "Restaurante com nome:'%s', tipoCozinha:'%s' e localizacao:'%s' já está cadastrado no sistema",
                            nome, tipoCozinha, localizacao));
        }
    }

    @Nested
    class BuscarRestaurante {

        @Test
        void devePermitirBuscarRestaurante() {
            String id = "65f30ee66ef25c44b8d9faac";
            RestauranteDomain restauranteEncontrado = restauranteGateway.findById(id);

            assertThat(restauranteEncontrado)
                    .isNotNull()
                    .isInstanceOf(RestauranteDomain.class);
            assertThat(restauranteEncontrado.getId())
                    .isNotNull()
                    .isEqualTo(id);
            assertThat(restauranteEncontrado.getNome())
                    .isNotNull()
                    .isEqualTo("Vivenda do Camarão");
            assertThat(restauranteEncontrado.getLocalizacao())
                    .isNotNull()
                    .isEqualTo("Av. Raimundo Pereira de Magalhães, 1465");
            assertThat(restauranteEncontrado.getTipoCozinha())
                    .isNotNull()
                    .isEqualTo("Frutos do Mar");
            assertThat(restauranteEncontrado.getHorariosFuncionamento())
                    .hasSize(1);
            assertThat(restauranteEncontrado.getHorariosFuncionamento().get(0))
                    .isEqualTo(LocalTime.parse("12:00:00"));
            assertThat(restauranteEncontrado.getCapacidade())
                    .isNotNull()
                    .isEqualTo(10);
        }

        @Test
        void deveGerarExcecao_Quando_BuscarRestaurante_IdNaoExiste() {
            String id = "65f30ee66ef25c44b8d9faax";

            assertThatThrownBy(() -> restauranteGateway.findById(id))
                    .isInstanceOf(NaoEncontradoException.class)
                    .hasMessage(String.format("Restaurante com id:'%s' não foi encontrado!", id));
        }

        @ParameterizedTest
        @CsvSource(value={"Pizza Hut; ; ", " ;Pizzaria; ", " ; ;R. Carlos Weber, 344"}, delimiter = ';')
        void devePermitirBuscarRestaurantePorNomeEOuTipoCozinhaEOuLocalizacao_InformandoSomenteNome(String nome, String tipoCozinha, String localizacao) {
            List<RestauranteDomain> restaurantesEncontrados = restauranteGateway.findByNomeOrTipoCozinhaOrLocalizacao(nome, tipoCozinha, localizacao);
            RestauranteDomain primeiroRestauranteEncontadro = restaurantesEncontrados.get(0);

            assertThat(restaurantesEncontrados)
                    .isNotEmpty()
                    .hasSize(1);
            assertThat(primeiroRestauranteEncontadro.getId())
                    .isNotNull()
                    .isEqualTo("65f30f8bb665b3d1aa9aeee3");
            assertThat(primeiroRestauranteEncontadro.getNome())
                    .isNotNull()
                    .isEqualTo("Pizza Hut");
            assertThat(primeiroRestauranteEncontadro.getLocalizacao())
                    .isNotNull()
                    .isEqualTo("R. Carlos Weber, 344");
            assertThat(primeiroRestauranteEncontadro.getTipoCozinha())
                    .isNotNull()
                    .isEqualTo("Pizzaria");
            assertThat(primeiroRestauranteEncontadro.getHorariosFuncionamento())
                    .hasSize(1);
            assertThat(primeiroRestauranteEncontadro.getHorariosFuncionamento().get(0))
                    .isEqualTo(LocalTime.parse("18:00:00"));
            assertThat(primeiroRestauranteEncontadro.getCapacidade())
                    .isNotNull()
                    .isEqualTo(3);
        }
    }

    @Test
    void deveGerarExcecao_VerificaSeRestauranteExiste_NaoExiste() {
        String id = "65f30ee66ef25c44b8d9faax";

        assertThatThrownBy(() -> restauranteGateway.existsById(id))
                .isInstanceOf(NaoEncontradoException.class)
                .hasMessage(String.format("Restaurante com id:'%s' não foi encontrado!", id));
    }
}
