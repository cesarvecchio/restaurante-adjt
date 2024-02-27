package br.com.restauranteadjt.application.gateways;

import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.domain.enums.TipoCozinhaEnum;

import java.util.List;

public interface RestauranteGateway {
    RestauranteDomain create(RestauranteDomain restauranteDomain);

    List<RestauranteDomain> findByNomeOrTipoCozinhaOrLocalizacao(String nome, TipoCozinhaEnum tipoCozinha, String localizacao);

    RestauranteDomain findById(String id);

    List<RestauranteDomain> findAll();
}
