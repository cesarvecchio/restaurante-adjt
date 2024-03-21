package br.com.restauranteadjt.infrastructure.gateways;

import br.com.restauranteadjt.application.gateways.MesaGateway;
import br.com.restauranteadjt.application.gateways.RestauranteGateway;
import br.com.restauranteadjt.domain.entity.MesaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.gateways.mapper.MesaVOMapper;
import br.com.restauranteadjt.infrastructure.persistence.collection.ReservaCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.ReservaRepository;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.MesaVO;
import br.com.restauranteadjt.main.exception.NaoEncontradoException;
import br.com.restauranteadjt.main.exception.StatusReservaException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class MesaRepositoryGateway implements MesaGateway {
    private final MesaVOMapper mesaVOMapper;
    private final RestauranteGateway restauranteGateway;
    private final MongoTemplate mongoTemplate;
    private final ReservaRepository reservaRepository;

    public MesaRepositoryGateway(MesaVOMapper mesaVOMapper, RestauranteGateway restauranteGateway,
                                 MongoTemplate mongoTemplate, ReservaRepository reservaRepository) {
        this.mesaVOMapper = mesaVOMapper;
        this.restauranteGateway = restauranteGateway;
        this.mongoTemplate = mongoTemplate;
        this.reservaRepository = reservaRepository;
    }

    @Override
    public List<MesaDomain> listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
        String idRestaurante, LocalDate dataReserva, LocalTime horaReserva, StatusMesa statusMesa) {
        restauranteGateway.existsById(idRestaurante);

        Criteria criteria = Criteria
            .where("restaurante.id").in(idRestaurante)
            .and("dataReserva").in(dataReserva)
            .and("horaReserva").in(horaReserva);

        if (Objects.nonNull(statusMesa)) {
            criteria.and("statusMesa").in(statusMesa);
        }

        Query query = new Query();
        query.addCriteria(criteria);

        List<ReservaCollection> reservas = mongoTemplate.find(query, ReservaCollection.class);

        if (reservas.isEmpty()) {
            if (Objects.isNull(statusMesa)) {
                throw new NaoEncontradoException(
                    String.format("O Restaurante com id:'%s' não possui nenhuma reserva para está data:'%s' e hora:'%s'",
                        idRestaurante, dataReserva, horaReserva));
            }
            throw new NaoEncontradoException(String.format(
                "O Restaurante com id:'%s' não possui nenhuma reserva para está data:'%s' e hora:'%s' com o status:'%s'",
                idRestaurante, dataReserva, horaReserva, statusMesa));
        }

        return reservas.stream().map(reserva ->
                new MesaDomain(reserva.getId(), reserva.getEmail(), reserva.getStatusMesa()))
            .toList();
    }

    @Override
    public MesaDomain update(String idReserva, StatusMesa statusMesa) {
        Optional<ReservaCollection> reservaCollection = reservaRepository.findById(idReserva);
        if (reservaCollection.isEmpty()) {
            throw new NaoEncontradoException(String.format("Reserva com id:'%s' não foi encontrado", idReserva));
        }

        if (reservaCollection.get().getStatusMesa() == statusMesa) {
            throw new StatusReservaException(String.format("Reserva com id:'%s' já possui o status:'%s'",
                idReserva, statusMesa));
        }

        reservaCollection.get().setStatusMesa(statusMesa);
        ReservaCollection savedReservaCollection = reservaRepository.save(reservaCollection.get());
        return mesaVOMapper.toDomain(new MesaVO(savedReservaCollection.getId(), savedReservaCollection.getEmail(),
            savedReservaCollection.getStatusMesa()));
    }
}
