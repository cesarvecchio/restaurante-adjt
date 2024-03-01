package br.com.restauranteadjt.infrastructure.persistence.repository;

import br.com.restauranteadjt.infrastructure.persistence.collection.ReservaCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends MongoRepository<ReservaCollection, String> {

    @Query("{ 'restaurante.id' :  ?0, horaReserva : ?1, dataReserva : ?2 }")
    List<ReservaCollection> findByIdRestauranteAndHorarioReservaAndDataReserva(
            String idRestaurante, LocalTime horarioReserva, LocalDate dataReserva);

    @Query("{ 'restaurante.id':  ?0, horaReserva: ?1, dataReserva: ?2, nome: ?3," +
            " email: ?4, telefone: ?5 }")
    Optional<ReservaCollection> findByIdRestauranteAndReserva(
            String idRestaurante, LocalTime horarioReserva, LocalDate dataReserva, String nome,
            String email, String telefone
    );
}
