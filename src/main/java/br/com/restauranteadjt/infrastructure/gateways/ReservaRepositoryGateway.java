package br.com.restauranteadjt.infrastructure.gateways;

import br.com.restauranteadjt.application.gateways.ReservaGateway;
import br.com.restauranteadjt.domain.entity.ReservaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.gateways.mapper.ReservaColletionMapper;
import br.com.restauranteadjt.infrastructure.persistence.collection.ReservaCollection;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.ReservaRepository;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.RestauranteVO;
import br.com.restauranteadjt.main.exception.DataInvalidaException;
import br.com.restauranteadjt.main.exception.JaPossuiReservaException;
import br.com.restauranteadjt.main.exception.NaoEncontradoException;
import br.com.restauranteadjt.main.exception.ReservaException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;

public class ReservaRepositoryGateway implements ReservaGateway {
    private final RestauranteRepository restauranteRepository;
    private final ReservaRepository reservaRepository;
    private final ReservaColletionMapper reservaColletionMapper;

    public ReservaRepositoryGateway(RestauranteRepository restauranteRepository,
                                    ReservaRepository reservaRepository,
                                    ReservaColletionMapper reservaColletionMapper) {
        this.restauranteRepository = restauranteRepository;
        this.reservaRepository = reservaRepository;
        this.reservaColletionMapper = reservaColletionMapper;
    }

    @Override
    public ReservaDomain create(String idRestaurante, ReservaDomain reservaDomain) {
        validateDataReservaValida(LocalDateTime.of(reservaDomain.dataReserva(), reservaDomain.horaReserva()));

        RestauranteCollection restauranteCollection = restauranteRepository.findById(idRestaurante)
            .orElseThrow(() ->
                new NaoEncontradoException(
                    String.format("Restaurante com id:'%s' não foi encontrado!", idRestaurante)));

        validateDataEhHorarioReservaLivre(restauranteCollection, reservaDomain.horaReserva(), reservaDomain.dataReserva());
        validateHorarioReservaExiste(restauranteCollection, reservaDomain.horaReserva());

        ReservaCollection reservaCollection = reservaColletionMapper.toCollection(reservaDomain);
        validateJaPossuiReserva(idRestaurante, reservaCollection);

        reservaCollection.setRestaurante(new RestauranteVO(restauranteCollection.getId(),
            restauranteCollection.getNome()));
        reservaCollection.setStatusMesa(StatusMesa.OCUPADA);

        ReservaCollection savedReservaCollection = reservaRepository.save(reservaCollection);

        return reservaColletionMapper.toDomain(savedReservaCollection);
    }

    private void validateHorarioReservaExiste(RestauranteCollection restauranteCollection, LocalTime horarioReserva) {
        Optional<LocalTime> reserva = restauranteCollection.getHorariosFuncionamento().stream()
            .filter(horario -> horario.equals(horarioReserva))
            .findAny();

        if (reserva.isEmpty()) {
            throw new ReservaException(
                String.format("Restaurante com id:'%s' não possuio o horario:'%s' para reserva",
                    restauranteCollection.getId(), horarioReserva));
        }
    }

    private void validateDataEhHorarioReservaLivre(RestauranteCollection restauranteCollection, LocalTime horarioReserva,
                                                   LocalDate dataReserva) {
        Integer qtdReservas = reservaRepository.countByIdRestauranteAndHorarioReservaAndDataReserva(
            restauranteCollection.getId(), horarioReserva, dataReserva);

        if (Objects.equals(qtdReservas, restauranteCollection.getCapacidade())) {
            throw new ReservaException(String.format(
                "O Restaurante com id:'%s' não possui reserva livre para a data:'%s' e hora:'%s'",
                restauranteCollection.getId(), dataReserva, horarioReserva));
        }

    }

    private void validateJaPossuiReserva(String idRestaurante, ReservaCollection reserva) {
        if (reservaRepository.findByIdRestauranteAndReserva(idRestaurante, reserva.getHoraReserva(),
            reserva.getDataReserva(), reserva.getNome(), reserva.getEmail(), reserva.getTelefone()).isPresent()) {
            throw new JaPossuiReservaException(String.format(
                "Você já possui uma reserva agendada no restaurante com id:'%s' para a data:'%s' e hora:'%s'",
                idRestaurante, reserva.getDataReserva(), reserva.getHoraReserva()));
        }
    }

    private void validateDataReservaValida(LocalDateTime dataReserva) {
        if (dataReserva.isBefore(LocalDateTime.now())) {
            throw new DataInvalidaException("A data e hora de reserva não pode ser anterior a data e hora atual");
        }
    }
}
