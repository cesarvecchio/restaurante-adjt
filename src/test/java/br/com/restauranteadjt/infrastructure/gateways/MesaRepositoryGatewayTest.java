package br.com.restauranteadjt.infrastructure.gateways;

import br.com.restauranteadjt.application.gateways.MesaGateway;
import br.com.restauranteadjt.domain.entity.MesaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.gateways.mapper.MesaVOMapper;
import br.com.restauranteadjt.infrastructure.persistence.collection.ReservaCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.ReservaRepository;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.MesaVO;
import br.com.restauranteadjt.main.exception.NaoEncontradoException;
import br.com.restauranteadjt.main.exception.StatusReservaException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class MesaRepositoryGatewayTest {
    private final MesaVOMapper mesaVOMapper = new MesaVOMapper();
    @Mock
    private RestauranteRepositoryGateway restauranteRepositoryGateway;
    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private ReservaRepository reservaRepository;

    private MesaGateway mesaGateway;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        mesaGateway = new MesaRepositoryGateway(mesaVOMapper, restauranteRepositoryGateway,
                mongoTemplate, reservaRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Nested
    class ListarMesasPorFiltros {
        @Test
        void deveGerarExcecao_RestauranteNaoEncontrado_QuandoListarMesasPorFiltros() {
            var idRestaurante = "1";
            var dataReserva = LocalDate.of(2024, 3, 20);
            var horaReserva = LocalTime.of(20, 0, 0, 0);
            var statusMesa = StatusMesa.OCUPADA;

            doThrow(NaoEncontradoException.class)
                    .when(restauranteRepositoryGateway).existsById(idRestaurante);

            assertThatThrownBy(() -> mesaGateway.listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
                    idRestaurante, dataReserva, horaReserva, statusMesa
            ))
                    .isInstanceOf(NaoEncontradoException.class);

            verify(restauranteRepositoryGateway, times(1)).existsById(any(String.class));
        }

        @Test
        void deveGerarExcecao_ReservaSemStatusNaoEncontrada_QuandoListarMesasPorFiltros() {
            var idRestaurante = "1";
            var dataReserva = LocalDate.of(2024, 3, 20);
            var horaReserva = LocalTime.of(20, 0, 0, 0);
            var query = buildQuery(idRestaurante, dataReserva, horaReserva, null);

            doNothing().when(restauranteRepositoryGateway).existsById(idRestaurante);

            when(mongoTemplate.find(query, ReservaCollection.class))
                    .thenReturn(List.of());

            assertThatThrownBy(() -> mesaGateway.listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
                    idRestaurante, dataReserva, horaReserva, null
            ))
                    .isInstanceOf(NaoEncontradoException.class)
                    .hasMessage(String.format(
                            "O Restaurante com id:'%s' não possui nenhuma reserva para está data:'%s' e hora:'%s'",
                            idRestaurante, dataReserva, horaReserva));

            verify(restauranteRepositoryGateway, times(1)).existsById(any(String.class));
            verify(mongoTemplate, times(1)).find(query, ReservaCollection.class);
        }

        @Test
        void deveGerarExcecao_ReservaComStatusNaoEncontrada_QuandoListarMesasPorFiltros() {
            var idRestaurante = "1";
            var dataReserva = LocalDate.of(2024, 3, 20);
            var horaReserva = LocalTime.of(20, 0, 0, 0);
            var statusMesa = StatusMesa.OCUPADA;
            var query = buildQuery(idRestaurante, dataReserva, horaReserva, statusMesa);

            doNothing().when(restauranteRepositoryGateway).existsById(idRestaurante);

            when(mongoTemplate.find(query, ReservaCollection.class))
                    .thenReturn(List.of());

            assertThatThrownBy(() -> mesaGateway.listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
                    idRestaurante, dataReserva, horaReserva, statusMesa
            ))
                    .isInstanceOf(NaoEncontradoException.class)
                    .hasMessage(String.format(
                            "O Restaurante com id:'%s' não possui nenhuma reserva para está data:'%s' e hora:'%s' com o status:'%s'",
                            idRestaurante, dataReserva, horaReserva, statusMesa));

            verify(restauranteRepositoryGateway, times(1)).existsById(any(String.class));
            verify(mongoTemplate, times(1)).find(query, ReservaCollection.class);
        }

        @Test
        void deveListarMesasPorFiltros() {
            var idRestaurante = "1";
            var dataReserva = LocalDate.of(2024, 3, 20);
            var horaReserva = LocalTime.of(20, 0, 0, 0);
            var statusMesa = StatusMesa.OCUPADA;
            var query = buildQuery(idRestaurante, dataReserva, horaReserva, statusMesa);
            var reserva = List.of(buildReserva(dataReserva, horaReserva, statusMesa));
            var mesaLista = buildMesaDomain(reserva);

            doNothing().when(restauranteRepositoryGateway).existsById(idRestaurante);

            when(mongoTemplate.find(query, ReservaCollection.class))
                    .thenReturn(reserva);

            var mesaListaObtida = mesaGateway.listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
                    idRestaurante, dataReserva, horaReserva, statusMesa);

            assertEquals(mesaLista, mesaListaObtida);

            verify(restauranteRepositoryGateway, times(1)).existsById(any(String.class));
            verify(mongoTemplate, times(1)).find(query, ReservaCollection.class);
        }
    }

    @Nested
    class AtualizaStatusReserva {
        @Test
        void deveGerarExcecao_ReservaNaoEncontrada_QuandoAtualizaStatusReserva() {
            var idReserva = "1";
            var status = StatusMesa.OCUPADA;

            when(reservaRepository.findById(idReserva)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> mesaGateway.update(idReserva, status))
                    .isInstanceOf(NaoEncontradoException.class)
                    .hasMessage(String.format("Reserva com id:'%s' não foi encontrado",
                            idReserva));

            verify(reservaRepository, times(1)).findById(idReserva);
        }

        @Test
        void deveGerarExcecao_ReservaJaPossuiStatus_QuandoAtualizaStatusReserva() {
            var idReserva = "1";
            var status = StatusMesa.OCUPADA;

            var reserva = buildReserva(
                    LocalDate.of(2024, 3, 20),
                    LocalTime.of(20, 0, 0, 0),
                    status
            );

            when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));

            assertThatThrownBy(() -> mesaGateway.update(idReserva, status))
                    .isInstanceOf(StatusReservaException.class)
                    .hasMessage(String.format("Reserva com id:'%s' já possui o status:'%s'",
                            idReserva, status));

            verify(reservaRepository, times(1)).findById(idReserva);
        }

        @Test
        void deveAtualizaStatusReserva() {
            var idReserva = "1";
            var status = StatusMesa.FINALIZADA;
            var reserva = buildReserva();
            var reservaAtualizada = buildReserva();
            reservaAtualizada.setStatusMesa(status);

            var mesaDomain = mesaVOMapper.toDomain(new MesaVO(reservaAtualizada.getId(), reservaAtualizada.getEmail(),
                    reservaAtualizada.getStatusMesa()));

            when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));

            when(reservaRepository.save(reservaAtualizada)).thenReturn(reservaAtualizada);

            var mesaObtida = mesaGateway.update(idReserva, status);

            assertEquals(mesaDomain.id(), mesaObtida.id());
            assertEquals(mesaDomain.emailCliente(), mesaObtida.emailCliente());
            assertEquals(mesaDomain.statusMesa(), mesaObtida.statusMesa());

            verify(reservaRepository, times(1)).findById(idReserva);
            verify(reservaRepository, times(1)).save(reservaAtualizada);
        }
    }

    private Query buildQuery(String idRestaurante, LocalDate dataReserva, LocalTime horaReserva, StatusMesa statusMesa){
        Query query = new Query();

        Criteria criteria = Criteria
                .where("restaurante.id").in(idRestaurante)
                .and("dataReserva").in(dataReserva)
                .and("horaReserva").in(horaReserva);

        if(!Objects.isNull(statusMesa)) {
            criteria.and("statusMesa").in(statusMesa);
        }

        query.addCriteria(criteria);

        return query;
    }

    private ReservaCollection buildReserva(LocalDate dataReserva, LocalTime horaReserva, StatusMesa statusMesa){
        ReservaCollection reservaCollection = new ReservaCollection(
                dataReserva,
                horaReserva,
                "Teste",
                 "teste@teste.com",
                "22002200"
        );
        reservaCollection.setId("1");
        reservaCollection.setStatusMesa(statusMesa);

        return reservaCollection;
    }

    private ReservaCollection buildReserva(){
        ReservaCollection reservaCollection = new ReservaCollection(
                LocalDate.of(2024, 3, 20),
                LocalTime.of(20, 0, 0, 0),
                "Teste",
                "teste@teste.com",
                "22002200"
        );
        reservaCollection.setId("1");
        reservaCollection.setStatusMesa(StatusMesa.OCUPADA);

        return reservaCollection;
    }

    private List<MesaDomain> buildMesaDomain(List<ReservaCollection> reservaCollectionList){

        return reservaCollectionList.stream().map(reservaCollection ->
                new MesaDomain(reservaCollection.getId(), reservaCollection.getEmail(),
                        reservaCollection.getStatusMesa())
        ).toList();
    }
}
