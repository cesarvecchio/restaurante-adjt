package br.com.restauranteadjt.infrastructure.gateways;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.restauranteadjt.application.gateways.ReservaGateway;
import br.com.restauranteadjt.domain.entity.ReservaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.gateways.mapper.ReservaColletionMapper;
import br.com.restauranteadjt.infrastructure.persistence.collection.ReservaCollection;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.ReservaRepository;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.RestauranteVO;
import br.com.restauranteadjt.main.exception.DataInvalidaException;
import br.com.restauranteadjt.main.exception.JaPossuiReservaException;
import br.com.restauranteadjt.main.exception.NaoEncontradoException;
import br.com.restauranteadjt.main.exception.ReservaException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ReservaRepositoryGatewayTest {
    private ReservaGateway reservaGateway;
    @Mock
    private RestauranteRepositoryGateway restauranteRepositoryGateway;
    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private ReservaColletionMapper reservaColletionMapper;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        reservaGateway = new ReservaRepositoryGateway(restauranteRepositoryGateway, reservaRepository,
            reservaColletionMapper);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Nested
    class CriarReserva {
        @Test
        void deveGerarExcecao_QuandoCriarReserva_DataReservaAnteriorAhDataAtual() {
            var id = ObjectId.get().toString();
            var reserva = criarReservaDomainDataPassada();

            assertThatThrownBy(() -> reservaGateway.create(id, reserva))
                .isInstanceOf(DataInvalidaException.class)
                .hasMessage("A data e hora de reserva não pode ser anterior a data e hora atual");

            verify(restauranteRepositoryGateway, never()).findRestauranteCollection(any(String.class));
        }

        @Test
        void deveGerarExcecao_QuandoCriarReserva_RestauranteNaoEncontrado() {
            var id = ObjectId.get().toString();
            var reserva = criarReservaDomainDataFutura();

            when(restauranteRepositoryGateway.findRestauranteCollection(id))
                .thenThrow(new NaoEncontradoException(
                    String.format("Restaurante com id:'%s' não foi encontrado!", id)));

            assertThatThrownBy(() -> reservaGateway.create(id, reserva))
                .isInstanceOf(NaoEncontradoException.class)
                .hasMessage(String.format("Restaurante com id:'%s' não foi encontrado!", id));

            verify(restauranteRepositoryGateway, times(1)).findRestauranteCollection(any(String.class));
        }

        @Test
        void deveGerarExcecao_QuandoCriarReserva_HorarioReservaInexistente() {
            var id = ObjectId.get().toString();
            var reserva = criarReservaDomainDataFuturaHorarioInexistente();

            var restaurante = criarRestaurante();
            restaurante.setId(id);

            when(restauranteRepositoryGateway.findRestauranteCollection(id))
                .thenReturn(restaurante);


            assertThatThrownBy(() -> reservaGateway.create(id, reserva))
                .isInstanceOf(ReservaException.class)
                .hasMessage(String.format("Restaurante com id:'%s' não possuio o horario:'%s' para reserva",
                    restaurante.getId(), reserva.horaReserva()));

            verify(restauranteRepositoryGateway, times(1)).findRestauranteCollection(any(String.class));
        }

        @Test
        void deveGerarExcecao_QuandoCriarReserva_DataEhHorarioReservaOcupados() {
            var id = ObjectId.get().toString();
            var reserva = criarReservaDomainDataFutura();

            var restaurante = criarRestaurante();
            restaurante.setId(id);

            when(restauranteRepositoryGateway.findRestauranteCollection(id))
                .thenReturn(restaurante);

            when(reservaRepository.countByIdRestauranteAndHorarioReservaAndDataReserva(restaurante.getId(),
                reserva.horaReserva(), reserva.dataReserva()))
                .thenReturn(restaurante.getCapacidade());

            assertThatThrownBy(() -> reservaGateway.create(id, reserva))
                .isInstanceOf(ReservaException.class)
                .hasMessage(String.format(
                    "O Restaurante com id:'%s' não possui reserva livre para a data:'%s' e hora:'%s'",
                    restaurante.getId(), reserva.dataReserva(), reserva.horaReserva()));

            verify(restauranteRepositoryGateway, times(1)).findRestauranteCollection(any(String.class));
            verify(reservaRepository, times(1))
                .countByIdRestauranteAndHorarioReservaAndDataReserva(any(String.class), any(LocalTime.class),
                    any(LocalDate.class));
        }

        @Test
        void deveGerarExcecao_QuandoCriarReserva_JaPossuiReserva() {
            var id = ObjectId.get().toString();
            var reserva = criarReservaDomainDataFutura();

            var restaurante = criarRestaurante();
            restaurante.setId(id);

            var reservaCollection = criarReservaCollectio();

            when(restauranteRepositoryGateway.findRestauranteCollection(id))
                .thenReturn(restaurante);
            when(reservaRepository.countByIdRestauranteAndHorarioReservaAndDataReserva(restaurante.getId(),
                reserva.horaReserva(), reserva.dataReserva()))
                .thenReturn(restaurante.getCapacidade() - 1);
            when(reservaColletionMapper.toCollection(reserva))
                .thenReturn(reservaCollection);
            when(reservaRepository.findByIdRestauranteAndReserva(restaurante.getId(),
                reserva.horaReserva(), reserva.dataReserva(), reserva.nome(), reserva.email(), reserva.telefone()))
                .thenReturn(Optional.of(reservaCollection));

            assertThatThrownBy(() -> reservaGateway.create(id, reserva))
                .isInstanceOf(JaPossuiReservaException.class)
                .hasMessage(String.format("Você já possui uma reserva agendada no restaurante com id:'%s' para a data:'%s' e hora:'%s'",
                    restaurante.getId(), reservaCollection.getDataReserva(), reservaCollection.getHoraReserva()));

            verify(restauranteRepositoryGateway, times(1)).findRestauranteCollection(any(String.class));
            verify(reservaRepository, times(1))
                .countByIdRestauranteAndHorarioReservaAndDataReserva(any(String.class), any(LocalTime.class),
                    any(LocalDate.class));
            verify(reservaColletionMapper, times(1)).toCollection(any(ReservaDomain.class));
            verify(reservaRepository, times(1))
                .findByIdRestauranteAndReserva(any(String.class), any(LocalTime.class), any(LocalDate.class),
                    any(String.class), any(String.class), any(String.class));
        }

        @Test
        void deveCriarReserva() {
            var id = ObjectId.get().toString();
            var reserva = criarReservaDomainDataFutura();

            var restaurante = criarRestaurante();
            restaurante.setId(id);

            var idReservaCollection = ObjectId.get().toString();
            var reservaCollection = criarReservaCollectio();
            reservaCollection.setId(idReservaCollection);
            reservaCollection.setRestaurante(new RestauranteVO(restaurante.getId(), restaurante.getNome()));
            reservaCollection.setStatusMesa(StatusMesa.OCUPADA);

            var reservaDomain = reservaColletionMapper.toDomain(reservaCollection);

            when(restauranteRepositoryGateway.findRestauranteCollection(id))
                .thenReturn(restaurante);
            when(reservaRepository.countByIdRestauranteAndHorarioReservaAndDataReserva(restaurante.getId(),
                reserva.horaReserva(), reserva.dataReserva()))
                .thenReturn(restaurante.getCapacidade() - 1);
            when(reservaColletionMapper.toCollection(reserva))
                .thenReturn(reservaCollection);
            when(reservaRepository.findByIdRestauranteAndReserva(restaurante.getId(),
                reserva.horaReserva(), reserva.dataReserva(), reserva.nome(), reserva.email(), reserva.telefone()))
                .thenReturn(Optional.empty());
            when(reservaRepository.save(reservaCollection)).thenReturn(reservaCollection);

            var reservaDomainObtida = reservaGateway.create(id, reserva);

            assertEquals(reservaDomain, reservaDomainObtida);

            verify(restauranteRepositoryGateway, times(1)).findRestauranteCollection(any(String.class));
            verify(reservaRepository, times(1))
                .countByIdRestauranteAndHorarioReservaAndDataReserva(any(String.class), any(LocalTime.class),
                    any(LocalDate.class));
            verify(reservaColletionMapper, times(1)).toCollection(any(ReservaDomain.class));
            verify(reservaRepository, times(1))
                .findByIdRestauranteAndReserva(any(String.class), any(LocalTime.class), any(LocalDate.class),
                    any(String.class), any(String.class), any(String.class));
            verify(reservaRepository, times(1))
                .save(any(ReservaCollection.class));
        }
    }

    private ReservaDomain criarReservaDomainDataFutura() {
        return new ReservaDomain(
            LocalDate.now().plusDays(1L),
            LocalTime.of(20, 0, 0, 0),
            "Sérgio",
            "sergio@gmail.com",
            "40028922"
        );
    }

    private ReservaDomain criarReservaDomainDataPassada() {
        return new ReservaDomain(
            LocalDate.now().minusDays(1),
            LocalTime.of(20, 0, 0, 0),
            "Sérgio",
            "sergio@gmail.com",
            "40028922"
        );
    }

    private ReservaDomain criarReservaDomainDataFuturaHorarioInexistente() {
        return new ReservaDomain(
            LocalDate.now().plusDays(1L),
            LocalTime.of(21, 0, 0, 0),
            "Sérgio",
            "sergio@gmail.com",
            "40028922"
        );
    }

    private ReservaCollection criarReservaCollectio() {
        return new ReservaCollection(
            LocalDate.now().plusDays(1L),
            LocalTime.of(20, 0, 0, 0),
            "Sérgio",
            "sergio@gmail.com",
            "40028922"
        );
    }

    private RestauranteCollection criarRestaurante() {
        return new RestauranteCollection(
            "Paris 6",
            "Paulista",
            "Bistro",
            List.of(LocalTime.of(20, 0, 0, 0)),
            2
        );
    }
}
