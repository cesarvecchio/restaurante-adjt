package br.com.restauranteadjt.application.usecases;

import br.com.restauranteadjt.application.gateways.RestauranteGateway;
import br.com.restauranteadjt.domain.entity.RestauranteDomain;

import java.util.List;

public class RestauranteUseCase {
    private final RestauranteGateway restauranteGateway;

    public RestauranteUseCase(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

    public RestauranteDomain create(RestauranteDomain restauranteDomain){
        return restauranteGateway.create(restauranteDomain);
    }

    public List<RestauranteDomain> findByNomeOrTipoCozinhaOrLocalizacao(String nome, String tipoCozinha, String localizacao) {
        return restauranteGateway.findByNomeOrTipoCozinhaOrLocalizacao(nome, tipoCozinha, localizacao);
    }

    public RestauranteDomain findById(String id){
        return restauranteGateway.findById(id);
    }

    public List<RestauranteDomain> findAll(){
        return restauranteGateway.findAll();
    }

}
