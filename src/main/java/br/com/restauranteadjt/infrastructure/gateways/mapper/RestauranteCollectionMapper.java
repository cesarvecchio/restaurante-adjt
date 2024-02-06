package br.com.restauranteadjt.infrastructure.gateways.mapper;

import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;

public class RestauranteCollectionMapper {
    public RestauranteCollection toCollection(RestauranteDomain restauranteDomain) {
        return new RestauranteCollection(
                restauranteDomain.nome(),
                restauranteDomain.localizacao(),
                restauranteDomain.tipoCozinha(),
                restauranteDomain.horariosFuncionamento(),
                restauranteDomain.capacidade()
        );
    }

    public RestauranteDomain toDomainObj(RestauranteCollection restauranteCollection){
        return new RestauranteDomain(
                restauranteCollection.getNome(),
                restauranteCollection.getLocalizacao(),
                restauranteCollection.getTipoCozinha(),
                restauranteCollection.getHorariosFuncionamento(),
                restauranteCollection.getCapacidade()
        );
    }
}
