package br.com.restauranteadjt.infrastructure.gateways.mapper;

import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;

public class RestauranteCollectionMapper {
    public RestauranteCollection toCollection(RestauranteDomain restauranteDomain) {
        return new RestauranteCollection(
            restauranteDomain.getNome(),
            restauranteDomain.getLocalizacao(),
            restauranteDomain.getTipoCozinha(),
            restauranteDomain.getHorariosFuncionamento(),
            restauranteDomain.getCapacidade()
        );
    }

    public RestauranteDomain toDomainObj(RestauranteCollection restauranteCollection) {
        return new RestauranteDomain(
            restauranteCollection.getId(),
            restauranteCollection.getNome(),
            restauranteCollection.getLocalizacao(),
            restauranteCollection.getTipoCozinha(),
            restauranteCollection.getHorariosFuncionamento(),
            restauranteCollection.getCapacidade()
        );
    }
}
