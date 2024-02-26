package br.com.restauranteadjt.application.gateways;

import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.domain.enums.TipoCozinhaEnum;
import br.com.restauranteadjt.domain.valueObject.Endereco;

public interface RestauranteGateway {
    RestauranteDomain create(RestauranteDomain restauranteDomain);

    RestauranteDomain findByNomeOrTipoCozinhaOrLocalizacao(String nome, TipoCozinhaEnum tipoCozinha, Endereco localizacao);

    RestauranteDomain findById(String id);
}
