package br.com.restauranteadjt.infrastructure.gateways;

import br.com.restauranteadjt.application.gateways.ReservaGateway;
import br.com.restauranteadjt.domain.entity.ReservaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.gateways.mapper.ReservaColletionMapper;
import br.com.restauranteadjt.infrastructure.persistence.collection.ReservaCollection;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.ReservaRepository;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.MesaVO;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.RestauranteVO;
import br.com.restauranteadjt.main.exception.JaPossuiReservaException;
import br.com.restauranteadjt.main.exception.ReservaException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservaRepositoryGateway implements ReservaGateway {
    private final RestauranteRepository restauranteRepository;
    private final RestauranteRepositoryGateway restauranteRepositoryGateway;
    private final ReservaRepository reservaRepository;
    private final ReservaColletionMapper reservaColletionMapper;

    public ReservaRepositoryGateway(RestauranteRepository restauranteRepository,
                                    RestauranteRepositoryGateway restauranteRepositoryGateway,
                                    ReservaRepository reservaRepository,
                                    ReservaColletionMapper reservaColletionMapper) {
        this.restauranteRepository = restauranteRepository;
        this.restauranteRepositoryGateway = restauranteRepositoryGateway;
        this.reservaRepository = reservaRepository;
        this.reservaColletionMapper = reservaColletionMapper;
    }

    @Override
    public ReservaDomain create(String idRestaurante, ReservaDomain reservaDomain) {
        RestauranteCollection restauranteCollection = restauranteRepositoryGateway.findRestauranteCollection(idRestaurante);

        validateHorarioReservaExiste(restauranteCollection, reservaDomain.horaReserva());
        Integer qtdReservas = validateDataEhHorarioReservaLivre(restauranteCollection, reservaDomain.horaReserva(), reservaDomain.dataReserva());

        ReservaCollection reservaCollection = reservaColletionMapper.toCollection(reservaDomain);
        validateJaPossuiReserva(idRestaurante, reservaCollection);

        reservaCollection.setRestaurante(new RestauranteVO(restauranteCollection.getId(),
                restauranteCollection.getNome()));
        reservaCollection.setMesa(new MesaVO(++qtdReservas, StatusMesa.OCUPADA));

        ReservaCollection savedReservaCollection = reservaRepository.save(reservaCollection);

        return reservaColletionMapper.toDomain(savedReservaCollection);
    }

    @Override
    public List<ReservaDomain> findByIdRestauranteAndHorarioReservaAndDataReserva(
            String idRestaurante, LocalTime horarioReserva, LocalDate dataReserva) {
        return reservaRepository.findByIdRestauranteAndHorarioReservaAndDataReserva(
                idRestaurante, horarioReserva, dataReserva).stream()
                .map(reservaColletionMapper::toDomain).toList();
    }

    protected void validateHorarioReservaExiste(RestauranteCollection restauranteCollection, LocalTime horarioReserva){
        restauranteCollection.getHorariosFuncionamento().stream().filter(horario -> horario.equals(horarioReserva))
                .findAny()
                .orElseThrow(() -> new ReservaException(
                        String.format("Restaurante com id:'%s' não possuio o horario:'%s' para reserva",
                                restauranteCollection.getId(), horarioReserva)));
    }

    protected Integer validateDataEhHorarioReservaLivre(RestauranteCollection restauranteCollection, LocalTime horarioReserva,
                                               LocalDate dataReserva) {
        List<ReservaDomain> reservaDomainList = findByIdRestauranteAndHorarioReservaAndDataReserva(
                restauranteCollection.getId(), horarioReserva, dataReserva);

        if(reservaDomainList.size() == restauranteCollection.getCapacidade()) {
            throw new ReservaException(String.format(
                    "O Restaurante com id:'%s' não possui reserva livre para a data:'%s' e hora:'%s'",
                    restauranteCollection.getId(), dataReserva, horarioReserva));
        }

        return reservaDomainList.size();
    }

    protected void validateJaPossuiReserva(String idRestaurante, ReservaCollection reserva){
        if(reservaRepository.findByIdRestauranteAndReserva(idRestaurante, reserva.getHoraReserva(),
                reserva.getDataReserva(), reserva.getNome(), reserva.getEmail(), reserva.getTelefone()).isPresent()) {
            throw new JaPossuiReservaException(String.format(
                    "Você já possui uma reserva agendada no restaurante com id:'%s' para a data:'%s' e hora:'%s'",
                            idRestaurante, reserva.getDataReserva(), reserva.getHoraReserva()));
        }
    }
}
