package br.com.restauranteadjt.infrastructure.gateways;

import br.com.restauranteadjt.application.gateways.MesaGateway;
import br.com.restauranteadjt.domain.entity.MesaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.persistence.collection.ReservaCollection;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.ReservaRepository;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.RestauranteVO;
import br.com.restauranteadjt.main.exception.NaoEncontradoException;
import br.com.restauranteadjt.main.exception.StatusReservaException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MesaRepositoryGatewayIT {

    @Autowired
    private MesaGateway mesaGateway;

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
    class BuscarMesas {

        @Test
        void devePermitirListarMesasPeloIdDoRestaurantes(){
            String idRestaurante = "65efa722ed1aa9f4356dca85";
            LocalDate dataReserva = LocalDate.now().plusDays(2);
            LocalTime horaReserva = LocalTime.parse("22:00:00");
            StatusMesa statusMesa = StatusMesa.OCUPADA;
            restauranteRepository.existsById(idRestaurante);

            List<MesaDomain> mesas = mesaGateway.listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(idRestaurante, dataReserva, horaReserva, statusMesa);

            assertThat(mesas).isNotNull()
                    .isNotEmpty()
                    .hasSize(1)
                    .anySatisfy(mesa -> {
                        assertThat(mesa.emailCliente())
                                .isEqualTo("joao09@gmail.com");
                        assertThat(mesa.statusMesa())
                                .isEqualTo(StatusMesa.OCUPADA);
                    });
        }

        @Test
        void deveGerarExcecao_QuandoNaoExiste_ReservaNoRestaurante(){
            String idRestaurante = "65f30f8bb665b3d1aa9aeee3";
            LocalDate dataReserva = LocalDate.now().plusDays(6);
            LocalTime horaReserva = LocalTime.parse("18:00:00");
            StatusMesa statusMesa = StatusMesa.LIVRE;

            assertThatThrownBy(() -> mesaGateway.listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(idRestaurante, dataReserva, horaReserva, statusMesa))
                    .isInstanceOf(NaoEncontradoException.class)
                    .hasMessage(String.format(
                            "O Restaurante com id:'%s' não possui nenhuma reserva para está data:'%s' e hora:'%s' com o status:'%s'",
                            idRestaurante, dataReserva, horaReserva, statusMesa)
                    );
        }

        @Test
        void deveGerarExcecao_QuandoNaoExiste_ReservaNoRestaurante_QuandoStatusMesaForNull(){
            String idRestaurante = "65f30f8bb665b3d1aa9aeee3";
            LocalDate dataReserva = LocalDate.now().plusDays(6);
            LocalTime horaReserva = LocalTime.parse("18:00:00");
            StatusMesa statusMesa = null;

            assertThatThrownBy(() -> mesaGateway.listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(idRestaurante, dataReserva, horaReserva, statusMesa))
                    .isInstanceOf(NaoEncontradoException.class)
                    .hasMessage(String.format("O Restaurante com id:'%s' não possui nenhuma reserva para está data:'%s' e hora:'%s'",
                            idRestaurante, dataReserva, horaReserva)
                    );
        }

    }

   @Nested
    class AtualizarMesa{

        @Test
        void devePermitirAtualizarMesa() {
            String idReserva = "65f45ec843af698ea2473e29";
            StatusMesa statusMesa = StatusMesa.FINALIZADA;

            mesaGateway.update(idReserva, statusMesa);
            Optional<ReservaCollection> reservaOptional = reservaRepository.findById(idReserva);

            ReservaCollection savedReservaCollection = null;
            if (reservaOptional.isPresent()) {
                savedReservaCollection = reservaOptional.get();
            }

            assertThat(savedReservaCollection)
                    .isNotNull()
                    .isInstanceOf(ReservaCollection.class);
            assertThat(savedReservaCollection.getId())
                    .isNotNull()
                    .isEqualTo("65f45ec843af698ea2473e29");
            assertThat(savedReservaCollection.getTelefone())
                    .isNotNull()
                    .isEqualTo("11912345678");
            assertThat(savedReservaCollection.getEmail())
                    .isNotNull()
                    .isEqualTo("joao09@gmail.com");
            assertThat(savedReservaCollection.getDataReserva())
                    .isNotNull()
                    .isEqualTo(LocalDate.now().plusDays(2));
            assertThat(savedReservaCollection.getHoraReserva())
                    .isNotNull()
                    .isEqualTo(LocalTime.parse("22:00:00"));
            assertThat(savedReservaCollection.getStatusMesa())
                    .isNotNull()
                    .isEqualTo(statusMesa);
            assertThat(savedReservaCollection.getRestaurante().id())
                    .isNotNull()
                    .isEqualTo("65efa722ed1aa9f4356dca85");

        }

       @Test
       void deveGerarExcecao_QuandoReserva_NaoForEncontrada(){
           String idReserva = "48329483948938439";
           StatusMesa statusMesa = StatusMesa.FINALIZADA;

           assertThatThrownBy(() -> mesaGateway.update(idReserva, statusMesa))
                   .isInstanceOf(NaoEncontradoException.class)
                   .hasMessage(String.format("Reserva com id:'%s' não foi encontrado", idReserva));


       }
       @Test
       void deveGerarExcecao_QuandoReserva_JaPossuiStatus(){
           String idReserva = "65f45ec843af698ea2473e29";
           StatusMesa statusMesa = StatusMesa.OCUPADA;

           assertThatThrownBy(() -> mesaGateway.update(idReserva, statusMesa))
                   .isInstanceOf(StatusReservaException.class)
                   .hasMessage(String.format("Reserva com id:'%s' já possui o status:'%s'",
                           idReserva, statusMesa));


       }

    }
}


