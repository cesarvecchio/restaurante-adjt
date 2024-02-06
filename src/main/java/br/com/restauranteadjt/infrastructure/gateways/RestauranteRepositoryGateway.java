package br.com.restauranteadjt.infrastructure.gateways;

import br.com.restauranteadjt.application.gateways.RestauranteGateway;
import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.infrastructure.gateways.mapper.RestauranteCollectionMapper;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;

public class RestauranteRepositoryGateway implements RestauranteGateway {
    private final RestauranteRepository restauranteRepository;
    private final RestauranteCollectionMapper restauranteCollectionMapper;

    public RestauranteRepositoryGateway(RestauranteRepository restauranteRepository,
                                        RestauranteCollectionMapper restauranteCollectionMapper) {
        this.restauranteRepository = restauranteRepository;
        this.restauranteCollectionMapper = restauranteCollectionMapper;
    }

    @Override
    public RestauranteDomain createRestaurante(RestauranteDomain restauranteDomain) {
        RestauranteCollection restauranteCollection = restauranteCollectionMapper.toCollection(restauranteDomain);

        RestauranteCollection savedRestauranteObj = restauranteRepository.save(restauranteCollection);

        return restauranteCollectionMapper.toDomainObj(savedRestauranteObj);
    }
}
