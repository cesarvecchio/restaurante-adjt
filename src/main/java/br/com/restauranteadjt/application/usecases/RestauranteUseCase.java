package br.com.restauranteadjt.application.usecases;

import br.com.restauranteadjt.application.gateways.RestauranteGateway;
import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.domain.enums.TipoCozinhaEnum;
import br.com.restauranteadjt.domain.valueObject.Endereco;

public class RestauranteUseCase {
    private final RestauranteGateway restauranteGateway;

    public RestauranteUseCase(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

    public RestauranteDomain create(RestauranteDomain restauranteDomain){
        return restauranteGateway.create(restauranteDomain);
    }

    public RestauranteDomain findByNomeOrTipoCozinhaOrLocalizacao(String nome, TipoCozinhaEnum tipoCozinha, Endereco localizacao) {
        return restauranteGateway.findByNomeOrTipoCozinhaOrLocalizacao(nome, tipoCozinha, localizacao);
    }

    public RestauranteDomain findById(String id){
        return restauranteGateway.findById(id);
    }
}
