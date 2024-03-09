package br.com.restauranteadjt.application.gateways;

import br.com.restauranteadjt.domain.entity.RestauranteDomain;

import java.util.List;

public interface RestauranteGateway {
    RestauranteDomain create(RestauranteDomain restauranteDomain);

    List<RestauranteDomain> findByNomeOrTipoCozinhaOrLocalizacao(String nome, String tipoCozinha, String localizacao);

    RestauranteDomain findById(String id);

    void existsById(String id);
}
