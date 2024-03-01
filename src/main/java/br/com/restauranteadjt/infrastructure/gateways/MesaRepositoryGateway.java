package br.com.restauranteadjt.infrastructure.gateways;

import br.com.restauranteadjt.application.gateways.MesaGateway;
import br.com.restauranteadjt.domain.entity.MesaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.gateways.mapper.MesaVOMapper;
import br.com.restauranteadjt.infrastructure.persistence.collection.ReservaCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.ReservaRepository;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.MesaVO;
import br.com.restauranteadjt.main.exception.NaoEncontradoException;
import br.com.restauranteadjt.main.exception.StatusReservaException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MesaRepositoryGateway implements MesaGateway {
    private final MesaVOMapper mesaVOMapper;
    private final RestauranteRepository restauranteRepository;
    private final RestauranteRepositoryGateway restauranteRepositoryGateway;
    private final MongoTemplate mongoTemplate;
    private final ReservaRepository reservaRepository;

    public MesaRepositoryGateway(MesaVOMapper mesaVOMapper, RestauranteRepository restauranteRepository,
                                 RestauranteRepositoryGateway restauranteRepositoryGateway, MongoTemplate mongoTemplate,
                                 ReservaRepository reservaRepository) {
        this.mesaVOMapper = mesaVOMapper;
        this.restauranteRepository = restauranteRepository;
        this.restauranteRepositoryGateway = restauranteRepositoryGateway;
        this.mongoTemplate = mongoTemplate;
        this.reservaRepository = reservaRepository;
    }

    @Override
    public List<MesaDomain> listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
            String idRestaurante, LocalDate dataReserva, LocalTime horaReserva, StatusMesa statusMesa) {
        restauranteRepositoryGateway.existsById(idRestaurante);

        Query query = new Query();

        Criteria criteria = Criteria
                .where("restaurante.id").in(idRestaurante)
                .and("dataReserva").in(dataReserva)
                .and("horaReserva").in(horaReserva);

        if(!Objects.isNull(statusMesa)) {
            criteria.and("statusMesa").in(statusMesa);
        }

        query.addCriteria(criteria);

        List<ReservaCollection> reservas = mongoTemplate.find(query, ReservaCollection.class);

        if(reservas.isEmpty()){
            if(Objects.isNull(statusMesa)) {
                throw new NaoEncontradoException(String.format(
                        "O Restaurante com id:'%s' não possui nenhuma reserva para está data:'%s' e hora:'%s'",
                        idRestaurante, dataReserva, horaReserva));
            }
            throw new NaoEncontradoException(String.format(
                    "O Restaurante com id:'%s' não possui nenhuma reserva para está data:'%s' e hora:'%s' com o status:'%s'",
                    idRestaurante, dataReserva, horaReserva, statusMesa));
        }

        return mongoTemplate.find(query, ReservaCollection.class)
                .stream().map(reserva ->
                        new MesaDomain(reserva.getId(), reserva.getEmail(), reserva.getStatusMesa()))
                .toList();
    }

    @Override
    public MesaDomain update(String idReserva, StatusMesa statusMesa) {
        Optional<ReservaCollection> reservaCollection = reservaRepository.findById(idReserva);

        if(reservaCollection.isEmpty()){
            throw new NaoEncontradoException(String.format("Reserva com id:'%s' não foi encontrado",
                    idReserva));
        }

        if(reservaCollection.get().getStatusMesa().equals(statusMesa)){
            throw new StatusReservaException(String.format("Reserva com id:'%s' já possui o status:'%s'",
                    idReserva, statusMesa));
        }

        reservaCollection.get().setStatusMesa(statusMesa);
        ReservaCollection savedReservaCollection = reservaRepository.save(reservaCollection.get());

        return mesaVOMapper.toDomain(new MesaVO(savedReservaCollection.getId(), savedReservaCollection.getEmail(),
                savedReservaCollection.getStatusMesa()));
    }
}
