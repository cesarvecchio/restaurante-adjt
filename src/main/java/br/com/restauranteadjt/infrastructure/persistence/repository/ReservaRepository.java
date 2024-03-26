package br.com.restauranteadjt.infrastructure.persistence.repository;

import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.persistence.collection.ReservaCollection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ReservaRepository extends MongoRepository<ReservaCollection, String> {

    @Query("{ 'restaurante.id' :  ?0, horaReserva : ?1, dataReserva : ?2, 'statusMesa': ?3 }")
    List<ReservaCollection> findByIdRestauranteAndHorarioReservaAndDataReservaAndStatusMesa(
        String idRestaurante, LocalTime horarioReserva, LocalDate dataReserva, StatusMesa statusMesa);

    @Query("{ 'restaurante.id':  ?0, horaReserva: ?1, dataReserva: ?2, nome: ?3," +
        " email: ?4, telefone: ?5 }")
    Optional<ReservaCollection> findByIdRestauranteAndReserva(
        String idRestaurante, LocalTime horarioReserva, LocalDate dataReserva, String nome,
        String email, String telefone
    );

    @Query(value = "{ 'restaurante.id' :  ?0, horaReserva : ?1, dataReserva : ?2 }", count = true)
    Integer countByIdRestauranteAndHorarioReservaAndDataReserva(
        String idRestaurante, LocalTime horarioReserva, LocalDate dataReserva);

}
