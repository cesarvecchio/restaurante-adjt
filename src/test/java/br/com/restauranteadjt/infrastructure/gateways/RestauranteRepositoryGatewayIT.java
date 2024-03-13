package br.com.restauranteadjt.infrastructure.gateways;

import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.infrastructure.gateways.mapper.RestauranteCollectionMapper;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class RestauranteRepositoryGatewayIT {

    @Autowired
    private RestauranteRepository restauranteRepository;
    private final RestauranteCollectionMapper restauranteCollectionMapper = new RestauranteCollectionMapper();

    @Test
    void testandoEmbeddedMongo() {
        RestauranteDomain restauranteDomain = new RestauranteDomain("65efa722ed1aa9f4356dca85", "nome", "localizacao", "cozinha", List.of(LocalTime.now()), 10);
        RestauranteCollection restauranteCollection = restauranteCollectionMapper.toCollection(restauranteDomain);

        RestauranteCollection savedDoc = restauranteRepository.save(restauranteCollection);
        List<RestauranteCollection> allDocs = restauranteRepository.findAll();
        assertThat(allDocs).hasSize(1);

    }
}
