package br.com.restauranteadjt.application.gateways;

import br.com.restauranteadjt.domain.entity.RestauranteDomain;

public interface RestauranteGateway {
    RestauranteDomain createRestaurante(RestauranteDomain restauranteDomain);

}
