package br.com.restauranteadjt.application.usecases;

import br.com.restauranteadjt.application.gateways.RestauranteGateway;
import br.com.restauranteadjt.domain.entity.RestauranteDomain;

public class CreateRestauranteInteractor {
    private final RestauranteGateway restauranteGateway;

    public CreateRestauranteInteractor(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

    public RestauranteDomain createRestaurante(RestauranteDomain restauranteDomain){
        return restauranteGateway.createRestaurante(restauranteDomain);
    }
}
