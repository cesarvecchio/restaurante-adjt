package br.com.restauranteadjt.infrastructure.gateways.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class RestauranteCollectionMapperTest {
    private final RestauranteCollectionMapper restauranteCollectionMapper = new RestauranteCollectionMapper();

    @Test
    void deveConverterDomainParaCollection() {
        var domain = new RestauranteDomain(
            "Teste",
            "Localização",
            "Tipo cozinha",
            List.of(LocalTime.now()),
            4
        );

        var collection = restauranteCollectionMapper.toCollection(domain);

        assertEquals(domain.getNome(), collection.getNome());
        assertEquals(domain.getLocalizacao(), collection.getLocalizacao());
        assertEquals(domain.getTipoCozinha(), collection.getTipoCozinha());
        assertEquals(domain.getHorariosFuncionamento(), collection.getHorariosFuncionamento());
        assertEquals(domain.getCapacidade(), collection.getCapacidade());
    }

    @Test
    void deveConverterCollectionParaDomain() {
        var collection = new RestauranteCollection(
            "Teste",
            "Localização",
            "Tipo cozinha",
            List.of(LocalTime.now()),
            4
        );

        var domain = restauranteCollectionMapper.toDomainObj(collection);

        assertEquals(collection.getNome(), domain.getNome());
        assertEquals(collection.getLocalizacao(), domain.getLocalizacao());
        assertEquals(collection.getTipoCozinha(), domain.getTipoCozinha());
        assertEquals(collection.getHorariosFuncionamento(), domain.getHorariosFuncionamento());
        assertEquals(collection.getCapacidade(), domain.getCapacidade());
    }
}
