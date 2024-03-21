package br.com.restauranteadjt.infrastructure.gateways;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.restauranteadjt.application.gateways.ReservaGateway;
import br.com.restauranteadjt.domain.entity.ReservaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.persistence.collection.ReservaCollection;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.ReservaRepository;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.RestauranteVO;
import br.com.restauranteadjt.main.exception.DataInvalidaException;
import br.com.restauranteadjt.main.exception.JaPossuiReservaException;
import br.com.restauranteadjt.main.exception.ReservaException;
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
public class ReservaRepositoryGatewayIT {
    @Autowired
    private ReservaGateway reservaGateway;

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
        return List.of(new RestauranteCollection("65efa722ed1aa9f4356dca85", "Burger King",
                "Av. Paulista", "Fast Food", List.of(LocalTime.parse("22:00:00")), 4, null),
            new RestauranteCollection("65f30ee66ef25c44b8d9faac", "Vivenda do Camarão",
                "Av. Raimundo Pereira de Magalhães, 1465", "Frutos do Mar", List.of(LocalTime.parse("12:00:00")), 10, null),
            new RestauranteCollection("65f30f8bb665b3d1aa9aeee3", "Pizza Hut",
                "R. Carlos Weber, 344", "Pizzaria", List.of(LocalTime.parse("18:00:00")), 3, null),
            new RestauranteCollection("65f532a4c76eed86ae85ee3f", "Agua Doce",
                "Granja Viana", "Pub", List.of(LocalTime.parse("19:00:00")), 1, null)
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
                new RestauranteVO("65f30ee66ef25c44b8d9faac", "Vivenda do Camarão"), StatusMesa.FINALIZADA),
            new ReservaCollection("65f4717bd6a804d8491361fd", LocalDate.now().plusDays(7),
                LocalTime.parse("20:00:00"), "Anni Carolini", "anni@gmail.com", "11956289657",
                new RestauranteVO("65f532a4c76eed86ae85ee3f", "Agua Doce"), StatusMesa.OCUPADA)
        );
    }

    @AfterEach
    void dropCollectionRestauranteEReserva() {
        restauranteRepository.deleteAll();
        reservaRepository.deleteAll();
    }

    @Nested
    class CriarReserva {

        @Test
        void devePermitirCriarReserva() {
            String idRestaurante = "65f30f8bb665b3d1aa9aeee3";
            LocalDate data = LocalDate.of(2024, 6, 15);
            LocalTime horario = LocalTime.parse("18:00:00");
            ReservaDomain reservaDomain = new ReservaDomain(data, horario, "Guilherme Matos de Carvalho",
                "guilherme@gmail.com", "11981281123");

            reservaGateway.create(idRestaurante, reservaDomain);

            Optional<ReservaCollection> reservaOptional = reservaRepository
                .findByIdRestauranteAndReserva(idRestaurante, horario, data, "Guilherme Matos de Carvalho",
                    "guilherme@gmail.com", "11981281123");

            ReservaCollection reserva = null;

            if (reservaOptional.isPresent()) {
                reserva = reservaOptional.get();
            }


            /*Verificando se a reserva está correta */
            assertThat(reserva)
                .isNotNull()
                .isInstanceOf(ReservaCollection.class);
            assertThat(reserva.getId())
                .isNotNull();
            assertThat(reserva.getHoraReserva())
                .isNotNull()
                .isEqualTo(LocalTime.parse("18:00:00"));
            assertThat(reserva.getDataReserva())
                .isNotNull()
                .isEqualTo(LocalDate.of(2024, 6, 15));
            assertThat(reserva.getNome())
                .isNotNull()
                .isEqualTo("Guilherme Matos de Carvalho");
            assertThat(reserva.getEmail())
                .isNotNull()
                .isEqualTo("guilherme@gmail.com");
            assertThat(reserva.getTelefone())
                .isNotNull()
                .isEqualTo("11981281123");
            assertThat(reserva.getRestaurante().id())
                .isNotNull()
                .isEqualTo("65f30f8bb665b3d1aa9aeee3");


        }


    }

    @Nested
    class validateExceptions {

        @Test
        void deveGerarExececao_SeHorarioDaReserva_ForAnteriorADataAtual() {
            String idRestaurante = "65efa722ed1aa9f4356dca85";
            LocalDate data = LocalDate.of(2019, 6, 15);
            LocalTime horario = LocalTime.parse("22:00:00");
            ReservaDomain reservaDomain = new ReservaDomain(data, horario, "Guilherme Matos de Carvalho",
                "guilherme@gmail.com", "11981281123");

            assertThatThrownBy(() -> reservaGateway.create(idRestaurante, reservaDomain))
                .isInstanceOf(DataInvalidaException.class)
                .hasMessage("A data e hora de reserva não pode ser anterior a data e hora atual");
        }

        @Test
        void deveGerarExececao_SeHorarioDaReserva_JaExiste() {
            String idRestaurante = "65f532a4c76eed86ae85ee3f";
            LocalDate data = LocalDate.now().plusDays(2);
            LocalTime horario = LocalTime.parse("20:00:00");
            ReservaDomain reservaDomain = new ReservaDomain(data, horario, "Guilherme Matos de Carvalho",
                "guilherme@gmail.com", "11981281123");

            assertThatThrownBy(() -> reservaGateway.create(idRestaurante, reservaDomain))
                .isInstanceOf(ReservaException.class)
                .hasMessage(String.format("Restaurante com id:'%s' não possuio o horario:'%s' para reserva",
                    idRestaurante, horario));
        }

        @Test
        void deveGerarExececao_SeHorarioEhDataDaReserva_JaExiste() {
            String idRestaurante = "65f532a4c76eed86ae85ee3f";
            LocalDate data = LocalDate.now().plusDays(7);
            LocalTime horario = LocalTime.parse("20:00:00");
            ReservaDomain reservaDomain = new ReservaDomain(data, horario, "Guilherme Matos de Carvalho",
                "guilherme@gmail.com", "11981281123");

            assertThatThrownBy(() -> reservaGateway.create(idRestaurante, reservaDomain))
                .isInstanceOf(ReservaException.class)
                .hasMessage(String.format(
                    "O Restaurante com id:'%s' não possui reserva livre para a data:'%s' e hora:'%s'",
                    idRestaurante, data, horario));
        }

        @Test
        void deveGerarExececao_SeJaPossuiReserva_NaDataEhHorario() {
            String idRestaurante = "65efa722ed1aa9f4356dca85";
            LocalDate data = LocalDate.now().plusDays(2);
            LocalTime horario = LocalTime.parse("22:00:00");
            ReservaDomain reservaDomain = new ReservaDomain(data, horario, "João",
                "joao09@gmail.com", "11912345678");

            assertThatThrownBy(() -> reservaGateway.create(idRestaurante, reservaDomain))
                .isInstanceOf(JaPossuiReservaException.class)
                .hasMessage(String.format(
                    "Você já possui uma reserva agendada no restaurante com id:'%s' para a data:'%s' e hora:'%s'",
                    idRestaurante, data, horario));
        }
    }
}
