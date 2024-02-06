package br.com.restauranteadjt.infrastructure.controllers.mapper;

import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.infrastructure.controllers.dto.request.CreateRestauranteRequest;
import br.com.restauranteadjt.infrastructure.controllers.dto.response.CreateRestauranteResponse;

public class RestauranteDTOMapper {
    public CreateRestauranteResponse toResponse(RestauranteDomain domainObj){
        return new CreateRestauranteResponse(
                domainObj.nome(),
                domainObj.localizacao(),
                domainObj.tipoCozinha(),
                domainObj.horariosFuncionamento(),
                domainObj.capacidade()
        );
    }

    public RestauranteDomain toRestauranteDomain(CreateRestauranteRequest request){
        return new RestauranteDomain(
                request.nome(),
                request.localizacao(),
                request.tipoCozinha(),
                request.horariosFuncionamento(),
                request.capacidade()
        );
    }
}
