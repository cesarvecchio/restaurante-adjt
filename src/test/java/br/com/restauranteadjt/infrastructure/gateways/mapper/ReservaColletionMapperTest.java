package br.com.restauranteadjt.infrastructure.gateways.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.restauranteadjt.domain.entity.ReservaDomain;
import br.com.restauranteadjt.infrastructure.persistence.collection.ReservaCollection;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class ReservaColletionMapperTest {
    private final ReservaColletionMapper reservaColletionMapper = new ReservaColletionMapper();

    @Test
    void deveConverterDomainParaCollection() {
        var domain = new ReservaDomain(
                null,
                LocalDate.now(),
                LocalTime.now(),
                "Teste",
                "teste@teste.com",
                "40028922"
        );

        var collection = reservaColletionMapper.toCollection(domain);

        assertEquals(domain.dataReserva(), collection.getDataReserva());
        assertEquals(domain.horaReserva(), collection.getHoraReserva());
        assertEquals(domain.nome(), collection.getNome());
        assertEquals(domain.email(), collection.getEmail());
        assertEquals(domain.telefone(), collection.getTelefone());
    }

    @Test
    void deveConverterCollectionParaDomain() {
        var collection = new ReservaCollection(
            LocalDate.now(),
            LocalTime.now(),
            "Teste",
            "teste@teste.com",
            "40028922"
        );

        var domain = reservaColletionMapper.toDomain(collection);

        assertEquals(collection.getId(), domain.id());
        assertEquals(collection.getDataReserva(), domain.dataReserva());
        assertEquals(collection.getHoraReserva(), domain.horaReserva());
        assertEquals(collection.getNome(), domain.nome());
        assertEquals(collection.getEmail(), domain.email());
        assertEquals(collection.getTelefone(), domain.telefone());
    }
}
